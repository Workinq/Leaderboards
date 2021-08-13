package kr.kieran.leaderboards.manager;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StatisticManager<T>
{

    protected final LeaderboardsPlugin plugin;
    private final BukkitTask task;

    // CACHE
    protected final Map<LeaderboardStatistic, List<LeaderboardEntry<T>>> entries = new HashMap<>();
    public List<LeaderboardEntry<T>> getEntriesBy(LeaderboardStatistic statistic) { return this.entries.get(statistic); }

    public StatisticManager(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
        this.task = this.getUpdateTask();
    }

    public abstract BukkitTask getUpdateTask();

    public void disable()
    {
        this.task.cancel();
        this.entries.clear();
    }

}
