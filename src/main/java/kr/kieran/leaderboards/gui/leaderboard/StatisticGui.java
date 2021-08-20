package kr.kieran.leaderboards.gui.leaderboard;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.gui.type.leaderboard.FactionGui;
import kr.kieran.leaderboards.gui.type.leaderboard.PlayerGui;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.stream.Collectors;

public abstract class StatisticGui extends PopulateGui
{

    protected final LeaderboardsPlugin plugin;
    protected final LeaderboardType type;

    public StatisticGui(LeaderboardsPlugin plugin, LeaderboardType type, String path, Player player)
    {
        super(plugin, path, player);

        // Assign
        this.plugin = plugin;
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
                return event -> new PlayerGui(plugin, statistic.getNiceName(), type)
                {
                    @Override
                    public List<LeaderboardEntry<OfflinePlayer>> getEntries()
                    {
                        return plugin.getPlayerManager().getEntriesBy(statistic);
                    }
                }.open(player, this);
            case ALL_FACTIONS:
                return event -> new FactionGui(plugin, statistic.getNiceName(), type)
                {
                    @Override
                    public List<LeaderboardEntry<Faction>> getEntries()
                    {
                        return plugin.getFactionManager().getEntriesBy(statistic);
                    }
                }.open(player, this);
            case OWN_FACTION:
                Faction faction = MPlayer.get(player).getFaction();
                return event -> new PlayerGui(plugin, statistic.getNiceName(), type)
                {
                    @Override
                    public List<LeaderboardEntry<OfflinePlayer>> getEntries()
                    {
                        return plugin.getPlayerManager()
                                .getEntriesBy(statistic)
                                .stream()
                                .filter(entry -> MPlayer.get(entry.getRepresented().getUniqueId().toString()).getFaction() == faction)
                                .collect(Collectors.toList());
                    }
                }.open(player, this);
        }
        return null;
    }

}
