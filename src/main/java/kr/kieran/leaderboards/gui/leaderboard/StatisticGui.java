package kr.kieran.leaderboards.gui.leaderboard;

import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.gui.type.leaderboard.IslandGui;
import kr.kieran.leaderboards.gui.type.leaderboard.PlayerGui;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.stellardev.galacticskyblock.entity.APlayer;
import org.stellardev.galacticskyblock.entity.Island;

import java.util.List;
import java.util.stream.Collectors;

public abstract class StatisticGui extends PopulateGui
{

    protected final LeaderboardType type;

    public StatisticGui(LeaderboardsPlugin plugin, LeaderboardType type, String path, Player player)
    {
        super(plugin, path, player);

        // Assign
        this.type = type;

        // Populate
        this.populateGui();
    }

    @Override
    protected GuiAction<InventoryClickEvent> getAction(String actionRaw)
    {
        LeaderboardStatistic statistic = LeaderboardStatistic.valueOf(actionRaw);
        switch (type)
        {
            case ALL_PLAYERS:
                return event -> new PlayerGui(plugin, statistic, type, player)
                {
                    @Override
                    public List<LeaderboardEntry<OfflinePlayer>> getEntries()
                    {
                        return plugin.getPlayerManager().getEntriesBy(statistic);
                    }
                }.open(player, this);
            case ALL_ISLANDS:
                return event -> new IslandGui(plugin, statistic, type, player)
                {
                    @Override
                    public List<LeaderboardEntry<Island>> getEntries()
                    {
                        return plugin.getIslandManager().getEntriesBy(statistic);
                    }
                }.open(player, this);
            case OWN_ISLAND:
                Island island = APlayer.get(player).getIsland();
                return event -> new PlayerGui(plugin, statistic, type, player)
                {
                    @Override
                    public List<LeaderboardEntry<OfflinePlayer>> getEntries()
                    {
                        return plugin.getPlayerManager()
                                .getEntriesBy(statistic)
                                .stream()
                                .filter(entry -> APlayer.get(entry.getRepresented().getUniqueId().toString()).getIsland() == island)
                                .collect(Collectors.toList());
                    }
                }.open(player, this);
        }
        return null;
    }

}
