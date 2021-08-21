package kr.kieran.leaderboards.task;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class TextureUpdateTask extends BukkitRunnable
{

    private final LeaderboardsPlugin plugin;

    public TextureUpdateTask(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        try (
                Connection connection = plugin.getDatabaseManager().getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE `leaderboards_skulls` SET `leaderboards_skulls`.`texture` = ? WHERE `leaderboards_skulls`.`unique_id` = ?;")
        )
        {
            for (Map.Entry<UUID, String> entry : plugin.getTextureManager().getTextures().entrySet())
            {
                // Set parameters
                statement.setString(1, entry.getValue());
                statement.setString(2, entry.getKey().toString());

                // Add to batch & clear parameters
                statement.addBatch();
                statement.clearParameters();
            }

            // Execute
            statement.executeBatch();
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to update skull textures: " + e.getMessage());
        }
    }

}
