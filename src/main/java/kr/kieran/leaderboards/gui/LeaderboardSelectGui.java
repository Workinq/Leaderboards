package kr.kieran.leaderboards.gui;

import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.leaderboard.BlockGui;
import kr.kieran.leaderboards.gui.leaderboard.CombatGui;
import kr.kieran.leaderboards.gui.leaderboard.TimeGui;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.model.LeaderboardType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeaderboardSelectGui extends PopulateGui
{

    private final LeaderboardType type;

    public LeaderboardSelectGui(LeaderboardsPlugin plugin, LeaderboardType type, Player player)
    {
        super(plugin, "guis.lb-menu", player);

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
            case TIME: return event -> new TimeGui(plugin, type, player).open(player, this);
            case COMBAT: return event -> new CombatGui(plugin, type, player).open(player, this);
            case BLOCK: return event -> new BlockGui(plugin, type, player).open(player, this);
        }
        return null;
    }

    private enum Action
    {
        TIME, COMBAT, BLOCK
    }

}
