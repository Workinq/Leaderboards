package kr.kieran.leaderboards.manager.statistic;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.entry.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.scheduler.BukkitRunnable;
import org.stellardev.galacticskyblock.coll.IslandColl;
import org.stellardev.galacticskyblock.entity.APlayer;
import org.stellardev.galacticskyblock.entity.Island;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class IslandStatisticManager extends StatisticManager<Island>
{

    public IslandStatisticManager(LeaderboardsPlugin plugin)
    {
        super(plugin, Island.class);
    }

    @Override
    public BukkitRunnable getUpdateTask()
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
                        List<LeaderboardEntry<Island>> entries = new LinkedList<>();
                        for (Island island : IslandColl.get().getAll())
                        {
                            if (island.isNone()) continue;
                            int total = 0;
                            for (APlayer aplayer : island.getAPlayers())
                            {
                                try (PreparedStatement statement = connection.prepareStatement("SELECT `leaderboards_players`.`" + statistic.getColumnName() + "` FROM `leaderboards_players` WHERE `leaderboards_players`.`unique_id` = ?;"))
                                {
                                    statement.setString(1, aplayer.getId());
                                    ResultSet resultSet = statement.executeQuery();
                                    if (!resultSet.next()) continue;
                                    total += resultSet.getInt(statistic.getColumnName());
                                }
                            }
                            entries.add(new LeaderboardEntry<>(island, total));
                        }

                        // Sort entries
                        entries = entries
                                .stream()
                                .sorted((first, last) -> Integer.compare(last.getValue(), first.getValue()))
                                .collect(Collectors.toList());

                        IslandStatisticManager.this.entries.put(statistic, entries);
                    }
                }
                catch (SQLException e)
                {
                    plugin.getLogger().log(Level.SEVERE, "Failed to update cache (island): " + e.getMessage());
                }
            }
        };
    }

    @Override
    protected void registerPlaceholders()
    {
        for (LeaderboardStatistic statistic : LeaderboardStatistic.values())
        {
            for (int i = 0; i < plugin.getConfig().getInt("placeholder.limit"); i++)
            {
                int finalIndex = i;
                this.register(plugin.getConfig().getString("placeholder.island.entry").replace("%statistic%", statistic.name()).replace("%index%", String.valueOf(i + 1)), () -> {
                    try
                    {
                        List<LeaderboardEntry<Island>> leaderboardEntries = entries.get(statistic);
                        if (leaderboardEntries == null) return Color.color(plugin.getConfig().getString("placeholder.still-loading").replace("%index%", String.format("%,d", finalIndex + 1)));
                        LeaderboardEntry<Island> entry = leaderboardEntries.get(finalIndex);
                        Island island = entry.getRepresented();
                        if (island == null) return Color.color(plugin.getConfig().getString("placeholder.island.bad-island"));

                        return Color.color(plugin.getConfig().getString("placeholder.island.format")
                                .replace("%index%", String.format("%,d", finalIndex + 1))
                                .replace("%island%", island.getName())
                                .replace("%value%", statistic.getFormattedValue().apply(entry.getValue())));
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        return Color.color(plugin.getConfig().getString("placeholder.out-of-bounds").replace("%index%", String.format("%,d", finalIndex + 1)));
                    }
                });
            }
        }


    }

}
