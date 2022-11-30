package kr.kieran.leaderboards.manager.statistic;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.entry.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PlayerStatisticManager extends StatisticManager<OfflinePlayer>
{

    // Constants
    private static final String STATISTIC_SELECT_STATEMENT = "SELECT `leaderboards_players`.`unique_id`, `leaderboards_players`.`%s` FROM `leaderboards_players` ORDER BY `leaderboards_players`.`%s`;";
    private static final UnaryOperator<String> STATISTIC_SELECT_FUNCTION = columnName ->  String.format(STATISTIC_SELECT_STATEMENT, columnName, columnName);

    public PlayerStatisticManager(LeaderboardsPlugin plugin)
    {
        super(plugin, OfflinePlayer.class);
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
                        List<LeaderboardEntry<OfflinePlayer>> entries = new LinkedList<>();
                        try (PreparedStatement statement = connection.prepareStatement(STATISTIC_SELECT_FUNCTION.apply(statistic.getColumnName())))
                        {
                            ResultSet resultSet = statement.executeQuery();
                            while (resultSet.next())
                            {
                                OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(resultSet.getString("unique_id")));
                                if (!player.hasPlayedBefore()) continue;
                                entries.add(new LeaderboardEntry<>(player, resultSet.getInt(statistic.getColumnName())));
                            }
                        }

                        // Sort entries
                        entries = entries
                                .stream()
                                .sorted((first, last) -> Integer.compare(last.getValue(), first.getValue()))
                                .collect(Collectors.toList());

                        PlayerStatisticManager.this.entries.put(statistic, entries);
                    }
                }
                catch (SQLException e)
                {
                    plugin.getLogger().log(Level.SEVERE, "Failed to update player leaderboard values", e);
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
                String formattedIndexString = String.format("%,d", i + 1);
                this.register(plugin.getConfig().getString("placeholder.player.entry").replace("%statistic%", statistic.name()).replace(INDEX_PLACEHOLDER, String.valueOf(i + 1)), () -> {
                    try
                    {
                        List<LeaderboardEntry<OfflinePlayer>> leaderboardEntries = entries.get(statistic);
                        if (leaderboardEntries == null) return Color.color(plugin.getConfig().getString("placeholder.still-loading").replace(INDEX_PLACEHOLDER, formattedIndexString));
                        LeaderboardEntry<OfflinePlayer> entry = leaderboardEntries.get(finalIndex);
                        OfflinePlayer player = entry.getRepresented();
                        if (player == null) return Color.color(plugin.getConfig().getString("placeholder.player.bad-player"));

                        return Color.color(plugin.getConfig().getString("placeholder.player.format")
                                .replace(INDEX_PLACEHOLDER, formattedIndexString)
                                .replace("%player%", player.getName())
                                .replace("%value%", statistic.getFormattedValue().apply(entry.getValue())));
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        return Color.color(plugin.getConfig().getString("placeholder.out-of-bounds").replace(INDEX_PLACEHOLDER, formattedIndexString));
                    }
                });
            }
        }
    }

}
