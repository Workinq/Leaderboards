package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ProfileStatisticListener implements Listener
{

    private final LeaderboardsPlugin plugin;

    public ProfileStatisticListener(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void block(BlockBreakEvent event)
    {
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;
        profile.setBlocksBroken(profile.getBlocksBroken() + 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void block(BlockPlaceEvent event)
    {
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;
        profile.setBlocksPlaced(profile.getBlocksPlaced() + 1);

        // If the block placed was a spawner, let's also increase the player's mob spawner count
        if (event.getBlockPlaced().getType() == Material.MOB_SPAWNER)
        {
            profile.setSpawnersPlaced(profile.getSpawnersPlaced() + 1);
        }
    }

//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void envoy(SupplyCrateOpenEvent event)
//    {
//        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
//        if (profile == null) return;
//        profile.setEnvoyClaims(profile.getEnvoyClaims() + 1);
//    }

}
