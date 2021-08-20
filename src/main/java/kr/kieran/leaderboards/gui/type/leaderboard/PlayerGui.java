package kr.kieran.leaderboards.gui.type.leaderboard;

import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
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
