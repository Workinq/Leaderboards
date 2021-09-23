package kr.kieran.leaderboards.manager;

import com.infamous.infamousevents.Infamous;
import com.infamous.infamousevents.data.Account;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        Optional<Account> account = Infamous.getInstance().getAccountController().find(player.getUniqueId());
        try (
                PreparedStatement statement = connection.prepareStatement("UPDATE `leaderboards_players` SET `leaderboards_players`.`time_connected` = ?, `leaderboards_players`.`time_played` = ?, `leaderboards_players`.`mob_kills` = ?, `leaderboards_players`.`player_deaths` = ?, `leaderboards_players`.`player_kills` = ?, `leaderboards_players`.`blocks_broken` = ?, `leaderboards_players`.`blocks_placed` = ?, `leaderboards_players`.`blocks_travelled` = ?, `leaderboards_players`.`spawners_placed` = ?, `leaderboards_players`.`event_wins` = ? WHERE `leaderboards_players`.`unique_id` = ?;")
        )
        {
            statement.setLong(1, player.getStatistic(Statistic.PLAY_ONE_TICK));
            statement.setLong(2, profile.getTimePlayed());
            statement.setInt(3, profile.getMobKills());
            statement.setInt(4, profile.getDeaths());
            statement.setInt(5, profile.getPlayerKills());
            statement.setInt(6, profile.getBlocksBroken());
            statement.setInt(7, profile.getBlocksPlaced());
            statement.setInt(8, player.getStatistic(Statistic.WALK_ONE_CM));
            statement.setInt(9, profile.getSpawnersPlaced());
            statement.setInt(10, account.map(Account::getWins).orElse(0));
            statement.setString(11, profile.getUniqueId().toString());
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to update profile information for '" + player.getName() + "': " + e.getMessage());
        }
    }

    public void disable() { this.profiles.clear(); }

}
