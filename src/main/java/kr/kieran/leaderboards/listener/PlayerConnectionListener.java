package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnectionListener implements Listener
{

    private final LeaderboardsPlugin plugin;

    public PlayerConnectionListener(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void login(PlayerLoginEvent event)
    {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED)  return;
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        plugin.newChain()
                .asyncFirst(() -> plugin.getProfileManager().load(uniqueId, true))
                // A check is done within the leaderboard command to see if the profile has been loaded
                .abortIfNull()
                .syncLast(profile -> plugin.getProfileManager().add(uniqueId, profile))
                .execute();
    }

    @EventHandler
    public void quit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Profile profile = plugin.getProfileManager().get(uniqueId);
        if (profile == null) return;

        // Execute
        plugin.newChain()
                .async(() -> plugin.getProfileManager().save(player, profile))
                .sync(() -> plugin.getProfileManager().remove(uniqueId))
                .execute();
    }

}
