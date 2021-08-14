package kr.kieran.leaderboards.gui.leaderboard;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.entity.Player;

public class TimeGui extends StatisticGui
{

    public TimeGui(LeaderboardsPlugin plugin, LeaderboardType type, Player player)
    {
        super(plugin, type, "guis.lb-time", player);
    }

}
