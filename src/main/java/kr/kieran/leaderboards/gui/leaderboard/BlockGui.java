package kr.kieran.leaderboards.gui.leaderboard;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardType;

public class BlockGui extends StatisticGui
{

    public BlockGui(LeaderboardsPlugin plugin, LeaderboardType type)
    {
        super(plugin, type, "guis.lb-block");
    }

}
