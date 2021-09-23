package kr.kieran.leaderboards;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import kr.kieran.leaderboards.command.LeaderboardsCommand;
import kr.kieran.leaderboards.database.Database;
import kr.kieran.leaderboards.listener.PlayerConnectionListener;
import kr.kieran.leaderboards.listener.ProfileStatisticListener;
import kr.kieran.leaderboards.listener.TextureCacheListener;
import kr.kieran.leaderboards.manager.statistic.IslandStatisticManager;
import kr.kieran.leaderboards.manager.statistic.PlayerStatisticManager;
import kr.kieran.leaderboards.manager.PreviousGuiManager;
import kr.kieran.leaderboards.manager.ProfileManager;
import kr.kieran.leaderboards.manager.SkullTextureManager;
import kr.kieran.leaderboards.task.ProfileSaveTask;
import kr.kieran.leaderboards.task.ProfileTimePlayedTask;
import kr.kieran.leaderboards.task.TextureUpdateTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LeaderboardsPlugin extends JavaPlugin
{

    // DATABASE
    private Database database;
    public Database getDatabaseManager() { return database; }

    // MANAGER: PROFILE
    private ProfileManager profileManager;
    public ProfileManager getProfileManager() { return profileManager; }

    // MANAGER: ISLAND STATISTIC
    private IslandStatisticManager islandManager;
    public IslandStatisticManager getIslandManager() { return islandManager; }

    // MANAGER: PLAYER STATISTIC
    private PlayerStatisticManager playerManager;
    public PlayerStatisticManager getPlayerManager() { return playerManager; }

    // MANAGER: PREVIOUS GUI
    private PreviousGuiManager guiManager;
    public PreviousGuiManager getGuiManager() { return guiManager; }

    // MANAGER: SKULL TEXTURES
    private SkullTextureManager textureManager;
    public SkullTextureManager getTextureManager() { return textureManager; }

    // TASK CHAIN
    private TaskChainFactory taskChain;
    public <T> TaskChain<T> newChain() { return taskChain.newChain(); }

    // TASKS
    private final Set<BukkitTask> tasks = new HashSet<>();

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
        this.tasks.forEach(BukkitTask::cancel);
        this.taskChain.shutdown(60, TimeUnit.SECONDS);

        // Manager shutdown
        this.textureManager.disable();
        this.guiManager.disable();
        this.playerManager.disable();
        this.islandManager.disable();
        this.profileManager.disable();
        this.database.disable();
    }

    private void registerManagers()
    {
        this.database = new Database(this);
        this.profileManager = new ProfileManager(this);
        this.islandManager = new IslandStatisticManager(this);
        this.playerManager = new PlayerStatisticManager(this);
        this.guiManager = new PreviousGuiManager();
        this.textureManager = new SkullTextureManager(this);
    }

    private void registerListeners()
    {
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ProfileStatisticListener(this), this);
        this.getServer().getPluginManager().registerEvents(new TextureCacheListener(this), this);
    }

    private void registerCommands()
    {
        this.getCommand("leaderboards").setExecutor(new LeaderboardsCommand(this));
    }

    private void registerTasks()
    {
        this.tasks.add(new ProfileSaveTask(this).runTaskTimerAsynchronously(this, this.getConfig().getInt("tasks.profile-update-frequency") * 20L, this.getConfig().getInt("tasks.profile-update-frequency") * 20L));
        this.tasks.add(new ProfileTimePlayedTask(this).runTaskTimer(this, 0L, this.getConfig().getInt("tasks.time-check-frequency") * 20L));
        this.tasks.add(new TextureUpdateTask(this).runTaskTimerAsynchronously(this, this.getConfig().getInt("tasks.texture-update-frequency") * 20L, this.getConfig().getInt("tasks.texture-update-frequency") * 20L));
    }

}
