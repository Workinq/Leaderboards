package kr.kieran.leaderboards.manager;

import com.massivecraft.factions.entity.MPlayerStats;
import com.massivecraft.factions.entity.PlayerStats;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import me.jordan.canetop.canetop;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jdgames.koth.entity.MPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ProfileManager
{

    private final LeaderboardsPlugin plugin;

    private final Map<UUID, Profile> profiles = new HashMap<>();
    public Collection<Profile> getProfiles() { return this.profiles.values(); }

    public ProfileManager(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void add(UUID uniqueId, Profile profile) { this.profiles.put(uniqueId, profile); }
    public void remove(UUID uniqueId) { this.profiles.remove(uniqueId); }
    public Profile get(UUID uniqueId) { return this.profiles.get(uniqueId); }

    public void save(Connection connection, Player player, Profile profile) throws SQLException
    {
        try (
                PreparedStatement statement = connection.prepareStatement("UPDATE `leaderboards_players` SET `leaderboards_players`.`time_connected` = ?, `leaderboards_players`.`time_played` = ?, `leaderboards_players`.`mob_kills` = ?, `leaderboards_players`.`player_deaths` = ?, `leaderboards_players`.`player_kills` = ?, `leaderboards_players`.`blocks_broken` = ?, `leaderboards_players`.`blocks_placed` = ?, `leaderboards_players`.`blocks_travelled` = ?, `leaderboards_players`.`cane_broken` = ?, `leaderboards_players`.`spawners_placed` = ?, `leaderboards_players`.`lms_wins` = ?, `leaderboards_players`.`envoy_claims` = ?, `leaderboards_players`.`koth_wins` = ? WHERE `leaderboards_players`.`unique_id` = ?;")
        )
        {
            PlayerStats stats = MPlayerStats.get().getPlayerStats(player);
            statement.setLong(1, player.getStatistic(Statistic.PLAY_ONE_TICK));
            statement.setLong(2, profile.getTimePlayed());
            statement.setInt(3, stats.getMobsKilled().intValue());
            statement.setInt(4, stats.getDeaths().intValue());
            statement.setInt(5, stats.getPlayersKilled().intValue());
            statement.setInt(6, stats.getBlocksBroken().intValue());
            statement.setInt(7, stats.getBlocksPlaced().intValue());
            statement.setInt(8, player.getStatistic(Statistic.WALK_ONE_CM));
            statement.setInt(9, new canetop().getScore(profile.getUniqueId()));
            statement.setInt(10, profile.getSpawnersPlaced());
            statement.setInt(11, profile.getLmsWins());
            statement.setInt(12, profile.getEnvoyClaims());
            statement.setInt(13, MPlayer.get(player).getKothWins());
            statement.setString(14, profile.getUniqueId().toString());
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to update profile information for '" + player.getName() + "': " + e.getMessage());
        }
    }

    public void disable() { this.profiles.clear(); }

}
