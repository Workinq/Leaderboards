package kr.kieran.leaderboards.gui.type.leaderboard;

import com.massivecraft.factions.entity.Faction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.model.LeaderboardEntry;
import kr.kieran.leaderboards.model.LeaderboardType;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public abstract class FactionGui extends LeaderboardGui<Faction>
{

    public FactionGui(LeaderboardsPlugin plugin, String statistic, LeaderboardType type)
    {
        super(plugin, statistic, type);

        // Populate
        this.populateGui();
    }

    @Override
    public GuiItem getItemFrom(int index, LeaderboardEntry<Faction> entry)
    {
        Faction faction = entry.getRepresented();
        ItemStack item = getItemFrom(plugin.getConfig().getString("items.index-to-block." + index));
        return ItemBuilder
                .from(item)
                .setName(Color.color(plugin.getConfig().getString("items.faction-entry.name").replace("%index%", String.valueOf(index)).replace("%name%", faction.getName())))
                .setLore(Color.color(plugin.getConfig().getStringList("items.faction-entry.lore")
                        .stream()
                        .map(text -> text.replace("%size%", String.valueOf(faction.getMPlayers().size())).replace("%value%", statistic.getFormattedValue().apply(entry.getValue())))
                        .collect(Collectors.toList())))
                .asGuiItem();
    }

    private static ItemStack getItemFrom(String input)
    {
        String[] split = input.split(":");
        return new ItemStack(Material.getMaterial(split[0]), 1, Short.parseShort(split[1]));
    }

}
