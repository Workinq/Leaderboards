package kr.kieran.leaderboards.manager;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PlayerStatisticManager extends StatisticManager<OfflinePlayer>
{

    public PlayerStatisticManager(LeaderboardsPlugin plugin)
    {
        super(plugin);
    }

    @Override
    public BukkitTask getUpdateTask()
    {
        return new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try (Connection connection = plugin.getDatabaseManager().getConnection())
                {
                    for (LeaderboardStatistic statistic : LeaderboardStatistic.values())
                    {
                        List<LeaderboardEntry<OfflinePlayer>> entries = new LinkedList<>();
                        try (PreparedStatement statement = connection.prepareStatement("SELECT `leaderboards_players`.`unique_id`, `leaderboards_players`.`" + statistic.getColumnName() + "` FROM `leaderboards_players` ORDER BY `leaderboards_players`.`" + statistic.getColumnName() + "`;"))
                        {
                            ResultSet resultSet = statement.executeQuery();
                            while (resultSet.next()) entries.add(new LeaderboardEntry<>(plugin.getServer().getOfflinePlayer(UUID.fromString(resultSet.getString("unique_id"))), resultSet.getInt(statistic.getColumnName())));
                        }

                        // Sort entries
                        entries = entries
                                .stream()
                                .sorted((first, last) -> Integer.compare(last.getValue(), first.getValue()))
                                .collect(Collectors.toList());

                        // Replace the cache
                        PlayerStatisticManager.this.entries.put(statistic, entries);
                    }
                }
                catch (SQLException e)
                {
                    plugin.getLogger().log(Level.SEVERE, "Failed to update cache (player): " + e.getMessage());
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, plugin.getConfig().getLong("update-frequency") * 20L);
    }

}
