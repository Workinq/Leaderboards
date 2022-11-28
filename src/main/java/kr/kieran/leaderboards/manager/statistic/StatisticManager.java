package kr.kieran.leaderboards.manager.statistic;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.entry.LeaderboardEntry;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public abstract class StatisticManager<T>
{

    protected final LeaderboardsPlugin plugin;
    protected final BukkitTask task;

    // CACHE
    protected final Map<LeaderboardStatistic, List<LeaderboardEntry<T>>> entries = new HashMap<>();
    public List<LeaderboardEntry<T>> getEntriesBy(LeaderboardStatistic statistic) { return entries.get(statistic); }

    public StatisticManager(LeaderboardsPlugin plugin, Class<T> type)
    {
        this.plugin = plugin;
        String typeName = type.getSimpleName();

        plugin.getLogger().log(Level.INFO, "Fetching initial leaderboard (" + typeName + ") values from database, this can take at most 10 seconds...");

        BukkitRunnable runnable = this.getUpdateTask();

        // Run the task synchronously first time to make sure all the database values are loaded before placeholders are registered
        CompletableFuture.runAsync(runnable).whenComplete((unused, throwable) -> {
            plugin.getLogger().log(Level.INFO, "Successfully fetched initial leaderboard (" + typeName + ") values from the database");
            this.registerPlaceholders();
            plugin.getLogger().log(Level.INFO, "Successfully registered hologram placeholders for the " + typeName + " leaderboard");
        });

        //Executors.newSingleThreadExecutor().submit(runnable).get(10, TimeUnit.SECONDS);

        long taskDelay = plugin.getConfig().getLong("tasks.update-frequency") * 20L;
        this.task = runnable.runTaskTimerAsynchronously(plugin, taskDelay, taskDelay);
    }

    // TODO: Add an item saying there are no players to show in the leaderboard if it's empty
    public abstract BukkitRunnable getUpdateTask();

    // Hologram placeholders
    protected abstract void registerPlaceholders();
    protected void register(String placeholder, PlaceholderReplacer replacer)
    {
        HologramsAPI.registerPlaceholder(plugin, placeholder, 100.0d, replacer);
    }

    public void disable()
    {
        task.cancel();
        entries.clear();
    }

}
