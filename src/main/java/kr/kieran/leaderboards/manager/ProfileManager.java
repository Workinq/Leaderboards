package kr.kieran.leaderboards.manager;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import mc.ultimatecore.skills.HyperSkills;
import mc.ultimatecore.skills.objects.SkillType;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ProfileManager
{

    private final LeaderboardsPlugin plugin;
    public static final String UPDATE_STATEMENT = "UPDATE `leaderboards_players` SET `leaderboards_players`.`time_connected` = ?, `leaderboards_players`.`time_played` = ?, `leaderboards_players`.`mob_kills` = ?, `leaderboards_players`.`blocks_broken` = ?, `leaderboards_players`.`blocks_travelled` = ?, `leaderboards_players`.`ores_mined` = ?, `leaderboards_players`.`wood_mined` = ?, `leaderboards_players`.`crops_harvested` = ?, `leaderboards_players`.`fish_caught` = ?, `leaderboards_players`.`skill_xp` = ? WHERE `leaderboards_players`.`unique_id` = ?;";

    private final Map<UUID, Profile> profiles = new HashMap<>();
    public Collection<Profile> getProfiles() { return this.profiles.values(); }

    public ProfileManager(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void add(UUID uniqueId, Profile profile) { this.profiles.put(uniqueId, profile); }
    public void remove(UUID uniqueId) { this.profiles.remove(uniqueId); }
    public Profile get(UUID uniqueId) { return this.profiles.get(uniqueId); }

    public void setParameters(PreparedStatement statement, Profile profile, Player player) throws SQLException
    {
        // Fetch the total XP from UltimateSKills plugin
        UUID uniqueId = player.getUniqueId();
        double totalXp = Arrays.stream(SkillType.values()).mapToDouble(skillType -> HyperSkills.getInstance().getApi().getXP(uniqueId, skillType)).sum();

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
        statement.setDouble(10, totalXp);
        statement.setString(11, profile.getUniqueId().toString());
    }

    public void save(Connection connection, Player player, Profile profile) throws SQLException
    {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT))
        {
            this.setParameters(statement, profile, player);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to update profile information for '" + player.getName() + "': " + e.getMessage());
        }
    }

    public void disable() { this.profiles.clear(); }

}
