package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import me.jordan.canetop.canetop;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jdgames.koth.entity.MPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerConnectionListener implements Listener
{

    private final LeaderboardsPlugin plugin;

    public PlayerConnectionListener(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void login(AsyncPlayerPreLoginEvent event)
    {
        UUID uniqueId = event.getUniqueId();
        try (
                Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT `leaderboards_players`.`time_played`, `leaderboards_players`.`blocks_broken`, `leaderboards_players`.`blocks_placed`, `leaderboards_players`.`spawners_placed`, `leaderboards_players`.`lms_wins`, `leaderboards_players`.`envoy_claims` FROM `leaderboards_players` WHERE `leaderboards_players`.`unique_id` = ?;")
        )
        {
            statement.setString(1, uniqueId.toString());
            ResultSet resultSet = statement.executeQuery();

            Profile profile;
            if (!resultSet.next())
            {
                // Create a new profile in the database as one didn't already exist
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO `leaderboards_players` (`leaderboards_players`.`unique_id`) VALUES (?);");
                insertStatement.setString(1, uniqueId.toString());
                insertStatement.executeUpdate();

                // Assign the profile object
                profile = new Profile(uniqueId);
            }
            else
            {
                profile = new Profile(
                        uniqueId,
                        resultSet.getLong("time_played"),
                        resultSet.getInt("blocks_broken"),
                        resultSet.getInt("blocks_placed"),
                        resultSet.getInt("spawners_placed"),
                        resultSet.getInt("lms_wins"),
                        resultSet.getInt("envoy_claims")
                );
            }

            // Add the profile to registry
            plugin.getProfileManager().add(uniqueId, profile);
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to load leaderboard profile of '" + uniqueId.toString() + "': " + e.getMessage());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Failed to load your leaderboard profile.");
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Profile profile = plugin.getProfileManager().get(uniqueId);
        if (profile == null) return;

        // Execute
        plugin.newChain()
                .async(() -> {
                    try (
                            Connection connection = plugin.getDatabaseManager().getConnection();
                            PreparedStatement statement = connection.prepareStatement("UPDATE `leaderboards_players` SET `leaderboards_players`.`time_connected` = ?, `leaderboards_players`.`time_played` = ?, `leaderboards_players`.`mob_kills` = ?, `leaderboards_players`.`player_deaths` = ?, `leaderboards_players`.`player_kills` = ?, `leaderboards_players`.`blocks_broken` = ?, `leaderboards_players`.`blocks_placed` = ?, `leaderboards_players`.`blocks_travelled` = ?, `leaderboards_players`.`cane_broken` = ?, `leaderboards_players`.`spawners_placed` = ?, `leaderboards_players`.`lms_wins` = ?, `leaderboards_players`.`envoy_claims` = ?, `leaderboards_players`.`koth_wins` = ? WHERE `leaderboards_players`.`unique_id` = ?;")
                    )
                    {
                        statement.setLong(1, player.getStatistic(Statistic.PLAY_ONE_TICK));
                        statement.setLong(2, profile.getTimePlayed());
                        statement.setInt(3, player.getStatistic(Statistic.MOB_KILLS));
                        statement.setInt(4, player.getStatistic(Statistic.DEATHS));
                        statement.setInt(5, player.getStatistic(Statistic.PLAYER_KILLS));
                        statement.setInt(6, profile.getBlocksBroken());
                        statement.setInt(7, profile.getBlocksPlaced());
                        statement.setInt(8, player.getStatistic(Statistic.WALK_ONE_CM));
                        statement.setInt(9, new canetop().getScore(uniqueId)); // TODO: Log sugar cane broken
                        statement.setInt(10, profile.getSpawnersPlaced());
                        statement.setInt(11, profile.getLmsWins());
                        statement.setInt(12, profile.getEnvoyClaims());
                        statement.setInt(13, MPlayer.get(player).getKothWins());
                        statement.setString(14, uniqueId.toString());
                        statement.executeUpdate();
                    }
                    catch (SQLException e)
                    {
                        plugin.getLogger().log(Level.SEVERE, "Failed to update profile information for '" + player.getName() + "': " + e.getMessage());
                    }
                })
                .execute();
    }

}
