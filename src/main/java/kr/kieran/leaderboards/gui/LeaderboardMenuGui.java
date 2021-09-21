package kr.kieran.leaderboards.gui;

import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.stellardev.galacticskyblock.entity.APlayer;
import org.stellardev.galacticskyblock.entity.Island;

public class LeaderboardMenuGui extends PopulateGui
{

    public LeaderboardMenuGui(LeaderboardsPlugin plugin, Player player)
    {
        super(plugin, "guis.menu", player);

        // Populate
        this.populateGui();
    }

    @Override
    protected GuiAction<InventoryClickEvent> getAction(String actionRaw)
    {
        LeaderboardType type = LeaderboardType.valueOf(actionRaw);
        switch (type)
        {
            case OWN_ISLAND:
                return event -> {
                    Island island = APlayer.get(player).getIsland();
                    if (island.isNone())
                    {
                        player.sendMessage(Color.color(plugin.getConfig().getString("messages.invalid-island")));
                        return;
                    }
                    this.open(type);
                };
            case ALL_ISLANDS:
            case ALL_PLAYERS:
                return event -> this.open(type);
        }
        return null;
    }

    private void open(LeaderboardType type)
    {
        LeaderboardSelectGui selectGui = new LeaderboardSelectGui(plugin, type, player);
        selectGui.open(player, this);
    }

}
