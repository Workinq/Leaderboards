package kr.kieran.leaderboards.gui.type.leaderboard;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.IndexedLeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stellardev.galacticskyblock.entity.APlayer;
import org.stellardev.galacticskyblock.entity.Island;

import java.util.List;
import java.util.stream.Collectors;

public abstract class IslandGui extends LeaderboardGui<Island>
{

    public IslandGui(LeaderboardsPlugin plugin, LeaderboardStatistic statistic, LeaderboardType type, Player player)
    {
        super(plugin, statistic, type, player);

        // Populate
        this.populateGui();
    }

    @Override
    public GuiItem getOwnItem()
    {
        // Args
        Island island = APlayer.get(player).getIsland();
        List<LeaderboardEntry<Island>> entries = this.getEntries();
        IndexedLeaderboardEntry<Island> indexedEntry = null;

        // Check for a matching entry
        for (int i = 0; i < entries.size(); i++)
        {
            LeaderboardEntry<Island> entry = entries.get(i);
            if (entry.getRepresented() != island) continue;
            indexedEntry = new IndexedLeaderboardEntry<>(entry.getRepresented(), entry.getValue(), i + 1);
        }

        // Return the item using the below method
        if (indexedEntry == null) return null;
        return this.getItemFrom(indexedEntry.getIndex(), indexedEntry, true);
    }

    @Override
    public GuiItem getItemFrom(int index, LeaderboardEntry<Island> entry, boolean own)
    {
        Island island = entry.getRepresented();
        ItemStack item = this.getItemFrom(index, own);
        return ItemBuilder
                .from(item)
                .setName(Color.color(plugin.getConfig().getString("items.island-entry.name").replace("%index%", String.valueOf(index)).replace("%name%", island.getName())))
                .setLore(Color.color(plugin.getConfig().getStringList("items.island-entry.lore")
                        .stream()
                        .map(text -> text.replace("%size%", String.valueOf(island.getAPlayers().size())).replace("%value%", statistic.getFormattedValue().apply(entry.getValue())))
                        .collect(Collectors.toList())))
                .asGuiItem();
    }

    private ItemStack getItemFrom(int index, boolean own)
    {
        return getItemFrom(own ? plugin.getConfig().getString("items.own-score-block") : plugin.getConfig().getString("items.index-to-block." + index));
    }

    private static ItemStack getItemFrom(String input)
    {
        String[] split = input.split(":");
        return new ItemStack(Material.getMaterial(split[0]), 1, Short.parseShort(split[1]));
    }

}
