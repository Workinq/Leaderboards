package kr.kieran.leaderboards.gui.type;

import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.BaseGui;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class CacheGui extends BaseGui
{

    protected final LeaderboardsPlugin plugin;
    protected final Player player;

    private boolean closeFromClick = false;
    public void setCloseFromClick(boolean closeFromClick) { this.closeFromClick = closeFromClick; }

    protected CacheGui(LeaderboardsPlugin plugin, int rows, String title, Set<InteractionModifier> interactionModifiers, Player player)
    {
        super(rows, title, interactionModifiers);

        // Assign
        this.plugin = plugin;
        this.player = player;

        // Action
        this.setCloseGuiAction(event -> {
            if (this.closeFromClick) return;
            PopulateGui gui = plugin.getGuiManager().get(player);
            if (gui == null) return;
            gui.openLater(player);
        });
    }

    public void open(HumanEntity player, PopulateGui gui)
    {
        gui.setCloseFromClick(true);

        // Cache gui
        plugin.getGuiManager().add((Player) player, gui);

        // Open next gui
        this.open(player);
    }

    public void openLater(HumanEntity player) { this.openLater(player, false); }
    public void openLater(HumanEntity player, boolean closeFromClick)
    {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            this.closeFromClick = closeFromClick;
            this.open(player);
        }, 2L);
    }

}
