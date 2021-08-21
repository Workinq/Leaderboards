package kr.kieran.leaderboards.task;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.PlayerLocation;
import kr.kieran.leaderboards.model.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileTimePlayedTask extends BukkitRunnable
{

    private final LeaderboardsPlugin plugin;

    public ProfileTimePlayedTask(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        for (Profile profile : plugin.getProfileManager().getProfiles())
        {
            Player player = plugin.getServer().getPlayer(profile.getUniqueId());
            // TODO: Should probably remove the profile entry if the player is null
            if (player == null) continue;

            // Get the player's locations
            Location location = player.getLocation();
            PlayerLocation currentLocation = new PlayerLocation(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
            PlayerLocation lastLocation = profile.getLastLocation();

            // The player must've just joined, store their location and move on
            if (lastLocation == null)
            {
                profile.setLastLocation(currentLocation);
                continue;
            }

            // The player is afk so we'll skip over them
            if (profile.getLastLocation().equals(currentLocation)) continue;
            profile.setTimePlayed(profile.getTimePlayed() + plugin.getConfig().getInt("tasks.time-check-frequency"));
            profile.setLastLocation(currentLocation);
        }
    }

}
