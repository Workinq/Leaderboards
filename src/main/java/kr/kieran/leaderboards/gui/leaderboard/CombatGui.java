package kr.kieran.leaderboards.gui.leaderboard;

import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.entity.Player;

public class CombatGui extends StatisticGui
{

    public CombatGui(LeaderboardsPlugin plugin, LeaderboardType type, Player player)
    {
        super(plugin, type, "guis.lb-combat", player);
    }

}
