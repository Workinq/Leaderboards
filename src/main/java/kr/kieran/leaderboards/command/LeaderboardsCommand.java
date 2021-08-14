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
        // Check that whoever is executing the command is an actual player (can't open a gui for a console)
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Color.color(plugin.getConfig().getString("messages.not-a-player")));
            return true;
        }

        // Open the leaderboards menu gui
        Player player = (Player) sender;
        LeaderboardMenuGui menuGui = new LeaderboardMenuGui(plugin, player);
        menuGui.open(player);
        return true;
    }

}
