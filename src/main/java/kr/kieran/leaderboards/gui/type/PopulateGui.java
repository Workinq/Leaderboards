package kr.kieran.leaderboards.gui.type;

import dev.triumphteam.gui.builder.item .ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.components.InteractionModifier;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class PopulateGui extends CacheGui
{

    private final String path;

    protected PopulateGui(LeaderboardsPlugin plugin, String path, Player player)
    {
        super(plugin, plugin.getConfig().getInt(path + ".rows"), Color.color(plugin.getConfig().getString(path + ".name")), InteractionModifier.VALUES, player);

        // Assign
        this.path = path;
    }

    protected void populateGui()
    {
        // Populate the gui
        for (String key : plugin.getConfig().getConfigurationSection(path + ".items").getKeys(false))
        {
            // Args
            String itemPath = path + ".items." + key;
            int slot = plugin.getConfig().getInt(itemPath + ".slot");

            // Item
            this.setItem(slot, ItemBuilder
                    .from(new ItemStack(Material.getMaterial(plugin.getConfig().getString(itemPath + ".material")), 1, (short) plugin.getConfig().getInt(itemPath + ".data")))
                    .setName(Color.color(plugin.getConfig().getString(itemPath + ".name")))
                    .setLore(Color.color(plugin.getConfig().getStringList(itemPath + ".lore")))
                    .asGuiItem());

            if (!plugin.getConfig().isSet(itemPath + ".action")) continue;
            GuiAction<InventoryClickEvent> action = this.getAction(plugin.getConfig().getString(itemPath + ".action"));
            if (action != null) this.addSlotAction(slot, action);
        }

        // Fill the gui
        this.getFiller().fill(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName(" ").asGuiItem());
    }

    protected abstract GuiAction<InventoryClickEvent> getAction(String actionRaw);

}
