package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextureCacheListener implements Listener
{

    private final LeaderboardsPlugin plugin;

    public TextureCacheListener(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        plugin.newChain()
                .syncFirst(() -> plugin.getTextureManager().getTextureFrom(player))
                .abortIfNull()
                .storeAsData("texture")
                .async(texture -> {
                    try (
                            Connection connection = plugin.getDatabaseManager().getConnection();
                            PreparedStatement statement = connection.prepareStatement("INSERT IGNORE INTO `leaderboards_skulls` (`leaderboards_skulls`.`unique_id`, `leaderboards_skulls`.`texture`) VALUES (?, ?);")
                    )
                    {
                        statement.setString(1, player.getUniqueId().toString());
                        statement.setString(2, texture);
                        statement.executeUpdate();
                        return Boolean.TRUE;
                    }
                    catch (SQLException e)
                    {
                        return null;
                    }
                })
                .abortIfNull()
                .<String>returnData("texture")
                .syncLast(texture -> plugin.getTextureManager().add(player.getUniqueId(), texture))
                .execute();
    }

}
