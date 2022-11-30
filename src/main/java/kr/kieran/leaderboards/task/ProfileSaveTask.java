package kr.kieran.leaderboards.task;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.manager.ProfileManager;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            try (PreparedStatement statement = connection.prepareStatement(ProfileManager.UPDATE_STATEMENT))
            {
                // Add all the players that need updating to a batch statement
                for (Profile profile : plugin.getProfileManager().getProfiles())
                {
                    Player player = plugin.getServer().getPlayer(profile.getUniqueId());
                    if (player == null) continue;
                    plugin.getProfileManager().setParameters(statement, player, profile);
                    statement.addBatch();
                }

                // Execute
                statement.executeBatch();
            }
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("An error occurred whilst saving player profiles: " + e.getMessage());
        }
    }

}
