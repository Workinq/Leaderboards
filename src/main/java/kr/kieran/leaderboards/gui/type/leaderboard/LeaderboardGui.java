package kr.kieran.leaderboards.gui.type.leaderboard;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.gui.type.CacheGui;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class LeaderboardGui<T> extends CacheGui
{

    // Store the rank slots, in order from 1 to 9
    private static final int[] SLOTS = new int[]{14, 22, 23, 24, 30, 31, 32, 33, 34, 35};

    protected final LeaderboardStatistic statistic;

    public LeaderboardGui(LeaderboardsPlugin plugin, LeaderboardStatistic statistic, LeaderboardType type, Player player)
    {
        super(plugin, plugin.getConfig().getInt("guis.leaderboard.rows"), Color.color(plugin.getConfig().getString("guis.leaderboard.name").replace("%stat%", statistic.getNiceName()).replace("%type%", type.getName())), InteractionModifier.VALUES, player);

        // Assign
        this.statistic = statistic;
    }

    protected void populateGui()
    {
        // Get a list of entries but trim the list to 9 as that's all that can fit
        List<LeaderboardEntry<T>> entries = this.getEntries();
        for (int i = 0; i < Math.min(9, entries.size()); i++)
        {
            LeaderboardEntry<T> entry = entries.get(i);
            GuiItem item = this.getItemFrom(i + 1, entry);
            this.setItem(SLOTS[i], item);
        }

        // Display own item
        this.setItem(plugin.getConfig().getInt("guis.leaderboard.score-slot"), this.getOwnItem());

        // Filler
        this.getFiller().fill(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName(" ").asGuiItem());
    }

    public abstract GuiItem getOwnItem();
    public abstract GuiItem getItemFrom(int index, LeaderboardEntry<T> entry);
    public abstract List<LeaderboardEntry<T>> getEntries();

}
