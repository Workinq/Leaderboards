package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class ProfileStatisticListener implements Listener
{

    private final LeaderboardsPlugin plugin;

    public ProfileStatisticListener(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void blockPlace(BlockPlaceEvent event)
    {
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void blockBreak(BlockBreakEvent event)
    {
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;

        // Increment
        profile.setBlocksBroken(profile.getBlocksBroken() + 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void kill(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null) return;
        Player killer = event.getEntity().getKiller();
        Profile profile = plugin.getProfileManager().get(killer.getUniqueId());
        if (profile == null) return;
        if (!(entity instanceof Monster)) return;

        // Increment
        profile.setMobKills(profile.getMobKills() + 1);
    }

}
