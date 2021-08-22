package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
                PreparedStatement statement = connection.prepareStatement("SELECT `leaderboards_players`.`time_played`, `leaderboards_players`.`spawners_placed`, `leaderboards_players`.`envoy_claims` FROM `leaderboards_players` WHERE `leaderboards_players`.`unique_id` = ?;")
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
                        resultSet.getInt("spawners_placed"),
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
                    try (Connection connection = plugin.getDatabaseManager().getConnection())
                    {
                        plugin.getProfileManager().save(connection, player, profile);
                    }
                    catch (SQLException e)
                    {
                        plugin.getLogger().log(Level.SEVERE, "Failed to update profile information for '" + player.getName() + "': " + e.getMessage());
                    }
                })
                .execute();
    }

}
