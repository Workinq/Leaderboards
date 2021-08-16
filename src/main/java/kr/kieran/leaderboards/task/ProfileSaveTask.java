package kr.kieran.leaderboards.task;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class ProfileSaveTask extends BukkitRunnable
{

    private final LeaderboardsPlugin plugin;

    public ProfileSaveTask(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        try (Connection connection = plugin.getDatabaseManager().getConnection())
        {
            for (Profile profile : plugin.getProfileManager().getProfiles())
            {
                Player player = plugin.getServer().getPlayer(profile.getUniqueId());
                if (player == null) continue;
                plugin.getProfileManager().save(connection, player, profile);
            }
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to open database connection: " + e.getMessage());
        }
    }

}
