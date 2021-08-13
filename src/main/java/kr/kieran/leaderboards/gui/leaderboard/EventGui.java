package kr.kieran.leaderboards.gui.leaderboard;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardType;

public class EventGui extends StatisticGui
{

    public EventGui(LeaderboardsPlugin plugin, LeaderboardType type)
    {
        super(plugin, type, "guis.lb-event");
    }

}
