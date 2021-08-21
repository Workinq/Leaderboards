package kr.kieran.leaderboards.gui.type.leaderboard;

import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.IndexedLeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardStatistic;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import kr.kieran.leaderboards.utility.SkullBuilder;
import kr.kieran.leaderboards.utility.SkullUtil;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PlayerGui extends LeaderboardGui<OfflinePlayer>
{

    public PlayerGui(LeaderboardsPlugin plugin, LeaderboardStatistic statistic, LeaderboardType type, Player player)
    {
        super(plugin, statistic, type, player);

        // Populate
        this.populateGui();
    }

    @Override
    public GuiItem getOwnItem()
    {
        // Args
        List<LeaderboardEntry<OfflinePlayer>> entries = this.getEntries();
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
        return this.getItemFrom(indexedEntry.getIndex(), indexedEntry);
    }

    @Override
    public GuiItem getItemFrom(int index, LeaderboardEntry<OfflinePlayer> entry)
    {
        OfflinePlayer player = entry.getRepresented();
        return getBuilderFrom(player)
                .setName(Color.color(plugin.getConfig().getString("items.player-entry.name").replace("%index%", String.valueOf(index)).replace("%name%", player.getName())))
                .setLore(Color.color(plugin.getConfig().getStringList("items.player-entry.lore").stream().map(text -> text.replace("%value%", statistic.getFormattedValue().apply(entry.getValue()))).collect(Collectors.toList())))
                .asGuiItem();
    }

    private static SkullBuilder getBuilderFrom(OfflinePlayer player)
    {
        String texture = SkullUtil.getSkullTexture(SkullUtil.getSkullItem(player.getUniqueId(), player.getName()));
        SkullBuilder head;
        if (texture == null) head = SkullBuilder.from(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).owner(player);
        else head = SkullBuilder.from(texture);
        return head;
    }

}
