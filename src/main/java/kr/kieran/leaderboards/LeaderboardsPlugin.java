package kr.kieran.leaderboards;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import kr.kieran.leaderboards.command.LeaderboardsCommand;
import kr.kieran.leaderboards.database.Database;
import kr.kieran.leaderboards.listener.PlayerConnectionListener;
import kr.kieran.leaderboards.listener.ProfileStatisticListener;
import kr.kieran.leaderboards.manager.FactionStatisticManager;
import kr.kieran.leaderboards.manager.PlayerStatisticManager;
import kr.kieran.leaderboards.manager.ProfileManager;
import kr.kieran.leaderboards.task.ProfileSaveTask;
import kr.kieran.leaderboards.task.ProfileTimePlayedTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class LeaderboardsPlugin extends JavaPlugin
{

    // DATABASE
    private Database database;
    public Database getDatabaseManager() { return database; }

    // MANAGER: PROFILE
    private ProfileManager profileManager;
    public ProfileManager getProfileManager() { return profileManager; }

    // MANAGER: FACTION STATISTIC
    private FactionStatisticManager factionManager;
    public FactionStatisticManager getFactionManager() { return factionManager; }

    // MANAGER: PLAYER STATISTIC
    private PlayerStatisticManager playerManager;
    public PlayerStatisticManager getPlayerManager() { return playerManager; }

    // TASK CHAIN
    private TaskChainFactory taskChain;
    public <T> TaskChain<T> newChain() { return taskChain.newChain(); }

    @Override public void onLoad() { this.saveDefaultConfig(); }

    @Override
    public void onEnable()
    {
        // Task
        this.taskChain = BukkitTaskChainFactory.create(this);

        // Register
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();
        this.registerTasks();
    }

    @Override
    public void onDisable()
    {
        // Ensure all tasks are complete
        this.taskChain.shutdown(60, TimeUnit.SECONDS);

        // Manager shutdown
        this.playerManager.disable();
        this.factionManager.disable();
        this.profileManager.disable();
        this.database.disable();
    }

    private void registerManagers()
    {
        this.database = new Database(this);
        this.profileManager = new ProfileManager(this);
        this.factionManager = new FactionStatisticManager(this);
        this.playerManager = new PlayerStatisticManager(this);
    }

    private void registerListeners()
    {
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ProfileStatisticListener(this), this);
    }

    private void registerCommands()
    {
        this.getCommand("leaderboards").setExecutor(new LeaderboardsCommand(this));
    }

    private void registerTasks()
    {
        new ProfileSaveTask(this).runTaskTimer(this, this.getConfig().getInt("profile-update-frequency") * 20L, this.getConfig().getInt("profile-update-frequency") * 20L);
        new ProfileTimePlayedTask(this).runTaskTimer(this, 0L, this.getConfig().getInt("time-check-frequency") * 20L);
    }

}
