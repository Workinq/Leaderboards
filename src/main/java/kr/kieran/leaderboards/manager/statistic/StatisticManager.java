package kr.kieran.leaderboards.manager.statistic;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
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
        this.registerPlaceholders();
    }

    public abstract BukkitTask getUpdateTask();

    // Hologram placeholders
    protected abstract void registerPlaceholders();
    protected void register(String placeholder, PlaceholderReplacer replacer)
    {
        HologramsAPI.registerPlaceholder(plugin, placeholder, 100.0d, replacer);
    }

    public void disable()
    {
        this.task.cancel();
        this.entries.clear();
    }

}
