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
        switch (type)
        {
            case OWN_FACTION:
            {
                Faction faction = MPlayer.get(player).getFaction();
                if (faction.isSystemFaction())
                {
                    player.sendMessage(Color.color(plugin.getConfig().getString("messages.invalid-faction")));
                    return null;
                }
                return this.open(type);
            }
            case ALL_FACTIONS:
            case ALL_PLAYERS:
                return this.open(type);
        }
        return null;
    }

    private GuiAction<InventoryClickEvent> open(LeaderboardType type)
    {
        return event -> {
            LeaderboardSelectGui selectGui = new LeaderboardSelectGui(plugin, type, player);
            selectGui.open(player);
        };
    }

}
