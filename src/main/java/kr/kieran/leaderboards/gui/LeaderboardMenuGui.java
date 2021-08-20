package kr.kieran.leaderboards.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import dev.triumphteam.gui.components.GuiAction;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.type.PopulateGui;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

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
            case OWN_FACTION:
                return event -> {
                    Faction faction = MPlayer.get(player).getFaction();
                    if (faction.isSystemFaction())
                    {
                        player.sendMessage(Color.color(plugin.getConfig().getString("messages.invalid-faction")));
                        return;
                    }
                    this.open(type);
                };
            case ALL_FACTIONS:
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
