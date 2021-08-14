package kr.kieran.leaderboards.gui;

import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeaderboardMenuGui extends PopulateGui
{

    private final LeaderboardsPlugin plugin;

    public LeaderboardMenuGui(LeaderboardsPlugin plugin, Player player)
    {
        super(plugin, "guis.menu", player);

        // Assign
        this.plugin = plugin;

        // Populate
        this.populateGui();
    }

    @Override
    protected GuiAction<InventoryClickEvent> getAction(String actionRaw)
    {
        LeaderboardType type = LeaderboardType.valueOf(actionRaw);
        return event -> {
            LeaderboardSelectGui selectGui = new LeaderboardSelectGui(plugin, type, player);
            selectGui.open(player);
        };
    }

}
