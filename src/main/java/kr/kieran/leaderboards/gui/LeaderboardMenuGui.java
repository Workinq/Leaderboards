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
        Action action = Action.valueOf(actionRaw);
        switch (action)
        {
            case OWN_FACTION:
                return event -> {
                    LeaderboardSelectGui selectGui = new LeaderboardSelectGui(plugin, LeaderboardType.OWN_FACTION);
                    selectGui.open(event.getWhoClicked());
                };
            case ALL_PLAYERS:
                return event -> {
                    LeaderboardSelectGui selectGui = new LeaderboardSelectGui(plugin, LeaderboardType.ALL_PLAYERS);
                    selectGui.open(event.getWhoClicked());
                };
            case ALL_FACTIONS:
                return event -> {
                    LeaderboardSelectGui selectGui = new LeaderboardSelectGui(plugin, LeaderboardType.ALL_FACTIONS);
                    selectGui.open(event.getWhoClicked());
                };
        }
        return null;
    }

    private enum Action
    {
        OWN_FACTION, ALL_PLAYERS, ALL_FACTIONS
    }

}
