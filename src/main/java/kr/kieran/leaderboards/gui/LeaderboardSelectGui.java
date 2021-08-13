package kr.kieran.leaderboards.gui;

import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.leaderboard.BlockGui;
import kr.kieran.leaderboards.gui.leaderboard.CombatGui;
import kr.kieran.leaderboards.gui.leaderboard.EventGui;
import kr.kieran.leaderboards.gui.leaderboard.TimeGui;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeaderboardSelectGui extends PopulateGui
{

    private final LeaderboardsPlugin plugin;
    private final LeaderboardType type;

    public LeaderboardSelectGui(LeaderboardsPlugin plugin, LeaderboardType type)
    {
        super(plugin, "guis.lb-menu");

        // Assign
        this.plugin = plugin;
        this.type = type;

        // Populate
        this.populateGui();
    }

    @Override
    protected GuiAction<InventoryClickEvent> getAction(String actionRaw)
    {
        Action action = Action.valueOf(actionRaw);
        switch (action)
        {
            case TIME: return event -> new TimeGui(plugin, type).open(event.getWhoClicked());
            case COMBAT: return event -> new CombatGui(plugin, type).open(event.getWhoClicked());
            case BLOCK: return event -> new BlockGui(plugin, type).open(event.getWhoClicked());
            case EVENT: return event -> new EventGui(plugin, type).open(event.getWhoClicked());
        }
        return null;
    }

    private enum Action
    {
        TIME, COMBAT, BLOCK, EVENT
    }

}
