package kr.kieran.leaderboards.listener;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.Profile;
import kr.kieran.leaderboards.utility.MaterialUtil;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Crops;

public class ProfileStatisticListener implements Listener
{

    private final LeaderboardsPlugin plugin;

    public ProfileStatisticListener(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void blockBreak(BlockBreakEvent event)
    {
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;

        // Increment
        profile.setBlocksBroken(profile.getBlocksBroken() + 1);

        // Check if the material is an ore
        Material material = event.getBlock().getType();
        if (MaterialUtil.isOre(material))
        {
            profile.setOresMined(profile.getOresMined() + 1);
        }

        // Check if the material is a log
        if (MaterialUtil.isLog(material))
        {
            profile.setWoodMined(profile.getWoodMined() + 1);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void harvest(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;

        // Check if block is a crop
        if (block instanceof Crops)
        {
            Crops crop = (Crops) block;
            if (crop.getState() != CropState.RIPE) return;

            // Increment
            profile.setCropsHarvested(profile.getCropsHarvested() + 1);
            return;
        }

        // Check if block is a pumpkin or melon
        if (block.getType() == Material.PUMPKIN || block.getType() == Material.MELON_BLOCK)
        {
            profile.setCropsHarvested(profile.getCropsHarvested() + 1);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void fish(PlayerFishEvent event)
    {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        Profile profile = plugin.getProfileManager().get(event.getPlayer().getUniqueId());
        if (profile == null) return;

        // Increment
        profile.setFishCaught(profile.getFishCaught() + 1);
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
