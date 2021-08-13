package kr.kieran.leaderboards.gui.type.leaderboard;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public abstract class LeaderboardGui<T> extends BaseGui
{

    // Store the rank slots, in order from 1 to 9
    private static final int[] SLOTS = new int[]{14, 22, 23, 24, 30, 31, 32, 33, 34, 35};

    protected final LeaderboardsPlugin plugin;

    public LeaderboardGui(LeaderboardsPlugin plugin, String statistic, LeaderboardType type)
    {
        super(plugin.getConfig().getInt("guis.leaderboard.rows"), Color.color(plugin.getConfig().getString("guis.leaderboard.name").replace("%stat%", statistic).replace("%type%", type.getName())), InteractionModifier.VALUES);

        // Assign
        this.plugin = plugin;
    }

    protected void populateGui()
    {
        // Get a list of entries but trim the list to 9 as that's all that can fit
        List<LeaderboardEntry<T>> entries = this.getEntries().stream().limit(9).collect(Collectors.toList());
        for (int i = 0; i < entries.size(); i++)
        {
            LeaderboardEntry<T> entry = entries.get(i);
            GuiItem item = this.getItemFrom(i + 1, entry);
            this.setItem(SLOTS[i], item);
        }

        // Filler
        this.getFiller().fill(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName(" ").asGuiItem());
    }

    public abstract GuiItem getItemFrom(int index, LeaderboardEntry<T> entry);
    public abstract List<LeaderboardEntry<T>> getEntries();

}
