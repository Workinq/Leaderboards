package kr.kieran.leaderboards.manager;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager
{

    // Constants
    public static final String UPDATE_STATEMENT = "UPDATE `leaderboards_players` SET `leaderboards_players`.`time_connected` = ?, `leaderboards_players`.`time_played` = ?, `leaderboards_players`.`mob_kills` = ?, `leaderboards_players`.`blocks_broken` = ?, `leaderboards_players`.`blocks_travelled` = ?, `leaderboards_players`.`ores_mined` = ?, `leaderboards_players`.`wood_mined` = ?, `leaderboards_players`.`crops_harvested` = ?, `leaderboards_players`.`fish_caught` = ? WHERE `leaderboards_players`.`unique_id` = ?;";

    // DI
    private final LeaderboardsPlugin plugin;

    // Cache
    private final Map<UUID, Profile> profiles = new HashMap<>();
    public Collection<Profile> getProfiles() { return Collections.unmodifiableCollection(profiles.values()); }

    public ProfileManager(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void add(UUID uniqueId, Profile profile) { profiles.put(uniqueId, profile); }
    public void remove(UUID uniqueId) { profiles.remove(uniqueId); }
    public Profile get(UUID uniqueId) { return profiles.get(uniqueId); }

    public Profile load(UUID uniqueId, boolean create)
    {
        try (
                Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement profileSelectStatement = connection.prepareStatement("SELECT `leaderboards_players`.`time_played`, `leaderboards_players`.`mob_kills`, `leaderboards_players`.`blocks_broken`, `leaderboards_players`.`ores_mined`, `leaderboards_players`.`wood_mined`, `leaderboards_players`.`crops_harvested`, `leaderboards_players`.`fish_caught` FROM `leaderboards_players` WHERE `leaderboards_players`.`unique_id` = ?;")
        )
        {
            profileSelectStatement.setString(1, uniqueId.toString());
            ResultSet resultSet = profileSelectStatement.executeQuery();

            // If a profile doesn't exist, and we're asked to create one, submit one to the database
            if (!resultSet.next() && create) return this.insertNewProfile(uniqueId);

            // The profile exists in the database, create one with the loaded values
            return new Profile(
                    uniqueId,
                    resultSet.getLong("time_played"),
                    resultSet.getInt("mob_kills"),
                    resultSet.getInt("blocks_broken"),
                    resultSet.getInt("ores_mined"),
                    resultSet.getInt("wood_mined"),
                    resultSet.getInt("crops_harvested"),
                    resultSet.getInt("fish_caught")
            );
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Failed to load leaderboard profile of '" + uniqueId.toString() + "': " + e.getMessage());
            return null;
        }
    }

    private Profile insertNewProfile(UUID uniqueId)
    {
        // Create a new profile in the database as one didn't already exist
        try (
                Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement profileCreateStatement = connection.prepareStatement("INSERT INTO `leaderboards_players` (`leaderboards_players`.`unique_id`) VALUES (?);")
        )
        {
            profileCreateStatement.setString(1, uniqueId.toString());
            profileCreateStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Failed to insert a new profile into the database for '" + uniqueId + "'");
            e.printStackTrace();
            return null;
        }

        return new Profile(uniqueId);
    }

    public void save(Player player, Profile profile)
    {
        try (Connection connection = plugin.getDatabaseManager().getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT))
            {
                this.setParameters(statement, player, profile);
                statement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Failed to update profile for '" + player.getName() + "': " + e.getMessage());
        }
    }

    public void setParameters(PreparedStatement statement, Player player, Profile profile) throws SQLException
    {
        // Set parameter values
        statement.setLong(1, player.getStatistic(Statistic.PLAY_ONE_TICK));
        statement.setLong(2, profile.getTimePlayed());
        statement.setInt(3, profile.getMobKills());
        statement.setInt(4, profile.getBlocksBroken());
        statement.setInt(5, player.getStatistic(Statistic.WALK_ONE_CM));
        statement.setInt(6, profile.getOresMined());
        statement.setInt(7, profile.getWoodMined());
        statement.setInt(8, profile.getCropsHarvested());
        statement.setInt(9, profile.getFishCaught());
        statement.setString(10, profile.getUniqueId().toString());
    }

    public void disable() { profiles.clear(); }

}
