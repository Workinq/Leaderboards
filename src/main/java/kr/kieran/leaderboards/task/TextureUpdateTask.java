package kr.kieran.leaderboards.task;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
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
            // Loop over any data that needs to be updated
            for (Iterator<Map.Entry<UUID, String>> iterator = plugin.getTextureManager().getUpdates().entrySet().iterator(); iterator.hasNext();)
            {
                Map.Entry<UUID, String> entry = iterator.next();

                // Set parameters
                statement.setString(1, entry.getValue());
                statement.setString(2, entry.getKey().toString());

                // Add to batch & remove entry
                statement.addBatch();
                iterator.remove();
            }

            // Commit
            statement.executeBatch();
        }
        catch (SQLException e)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to update skull textures: " + e.getMessage());
        }
    }

}
