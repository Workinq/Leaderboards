package kr.kieran.leaderboards.command;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.LeaderboardMenuGui;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderboardsCommand implements CommandExecutor
{

    private final LeaderboardsPlugin plugin;

    public LeaderboardsCommand(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Color.color(plugin.getConfig().getString("messages.not-a-player")));
            return true;
        }

        Player player = (Player) sender;
        // Check if the player's profile was loaded successfully at login
        if (plugin.getProfileManager().get(player.getUniqueId()) == null)
        {
            player.sendMessage(Color.color(plugin.getConfig().getString("messages.profile-not-loaded")));
            return true;
        }

        // Open the leaderboards menu gui
        LeaderboardMenuGui menuGui = new LeaderboardMenuGui(plugin, player);
        menuGui.open(player);
        return true;
    }

}
