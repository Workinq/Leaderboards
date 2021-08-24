package kr.kieran.leaderboards.manager.statistic;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class FactionStatisticManager extends StatisticManager<Faction>
{

    public FactionStatisticManager(LeaderboardsPlugin plugin)
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
                        List<LeaderboardEntry<Faction>> entries = new LinkedList<>();
                        for (Faction faction : FactionColl.get().getAll())
                        {
                            if (faction.isSystemFaction()) continue;
                            int total = 0;
                            for (MPlayer mplayer : faction.getMPlayers())
                            {
                                try (PreparedStatement statement = connection.prepareStatement("SELECT `leaderboards_players`.`" + statistic.getColumnName() + "` FROM `leaderboards_players` WHERE `leaderboards_players`.`unique_id` = ?;"))
                                {
                                    statement.setString(1, mplayer.getId());
                                    ResultSet resultSet = statement.executeQuery();
                                    if (!resultSet.next()) continue;
                                    total += resultSet.getInt(statistic.getColumnName());
                                }
                            }
                            entries.add(new LeaderboardEntry<>(faction, total));
                        }

                        // Sort entries
                        entries = entries
                                .stream()
                                .sorted((first, last) -> Integer.compare(last.getValue(), first.getValue()))
                                .collect(Collectors.toList());

                        FactionStatisticManager.this.entries.put(statistic, entries);
                    }
                }
                catch (SQLException e)
                {
                    plugin.getLogger().log(Level.SEVERE, "Failed to update cache (faction): " + e.getMessage());
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, plugin.getConfig().getLong("update-frequency") * 20L);
    }

    @Override
    protected void registerPlaceholders()
    {
        for (LeaderboardStatistic statistic : LeaderboardStatistic.values())
        {
            for (int i = 0; i < plugin.getConfig().getInt("placeholder.limit"); i++)
            {
                int finalIndex = i;
                this.register(plugin.getConfig().getString("placeholder.faction.entry").replace("%statistic%", statistic.name()).replace("%index%", String.valueOf(i + 1)), () -> {
                    try
                    {
                        LeaderboardEntry<Faction> entry = entries.get(statistic).get(finalIndex);
                        Faction faction = entry.getRepresented();
                        if (faction == null) return plugin.getConfig().getString("placeholder.faction.bad-faction");

                        return Color.color(plugin.getConfig().getString("placeholder.faction.format")
                                .replace("%index%", String.format("%,d", finalIndex + 1))
                                .replace("%faction%", faction.getName())
                                .replace("%value%", statistic.getFormattedValue().apply(entry.getValue())));
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        return Color.color(plugin.getConfig().getString("placeholder.out-of-bounds"));
                    }
                });
            }
        }


    }

}
