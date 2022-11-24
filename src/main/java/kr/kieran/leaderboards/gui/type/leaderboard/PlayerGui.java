package kr.kieran.leaderboards.gui.type.leaderboard;

import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.entry.IndexedLeaderboardEntry;
import kr.kieran.leaderboards.model.entry.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import kr.kieran.leaderboards.utility.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerGui extends LeaderboardGui<OfflinePlayer>
{

    public PlayerGui(LeaderboardsPlugin plugin, LeaderboardStatistic statistic, LeaderboardType type, Player player, List<LeaderboardEntry<OfflinePlayer>> entries)
    {
        super(plugin, statistic, type, player, entries);

        // Populate
        this.populateGui();
    }

    @Override
    public GuiItem getOwnItem()
    {
        // Args
        IndexedLeaderboardEntry<OfflinePlayer> indexedEntry = null;

        // Check for a matching entry
        for (int i = 0; i < entries.size(); i++)
        {
            LeaderboardEntry<OfflinePlayer> entry = entries.get(i);
            if (!entry.getRepresented().getUniqueId().equals(player.getUniqueId())) continue;
            indexedEntry = new IndexedLeaderboardEntry<>(entry.getRepresented(), entry.getValue(), i + 1);
        }

        // Return the item using the below method
        if (indexedEntry == null) return null;
        return this.getItemFrom(indexedEntry.getIndex(), indexedEntry, true);
    }

    @Override
    public GuiItem getItemFrom(int index, LeaderboardEntry<OfflinePlayer> entry, boolean own)
    {
        OfflinePlayer player = entry.getRepresented();
        return getBuilderFrom(player)
                .itemName(Color.color(plugin.getConfig().getString("items.player-entry.name").replace("%index%", String.valueOf(index)).replace("%name%", player.getName())))
                .itemLore(Color.color(plugin.getConfig().getStringList("items.player-entry.lore").stream().map(text -> text.replace("%value%", statistic.getFormattedValue().apply(entry.getValue()))).collect(Collectors.toList())))
                .asGuiItem();
    }

    private SkullBuilder getBuilderFrom(OfflinePlayer player)
    {
        String texture = plugin.getTextureManager().getSkullTexture(plugin.getTextureManager().getSkullItem(player.getUniqueId(), player.getName()));
        SkullBuilder head;
        if (texture == null) head = SkullBuilder.from(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).owner(player);
        else head = SkullBuilder.from(texture);
        return head;
    }

}
