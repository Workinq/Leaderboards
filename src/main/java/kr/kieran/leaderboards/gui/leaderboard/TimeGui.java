package kr.kieran.leaderboards.gui.leaderboard;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardType;

public class TimeGui extends StatisticGui
{

    public TimeGui(LeaderboardsPlugin plugin, LeaderboardType type)
    {
        super(plugin, type, "guis.lb-time");
    }

}
