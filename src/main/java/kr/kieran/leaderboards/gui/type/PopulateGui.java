package kr.kieran.leaderboards.gui.type;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.BaseGui;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import kr.kieran.leaderboards.utility.Color;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class PopulateGui extends BaseGui
{

    private final LeaderboardsPlugin plugin;
    private final String path;

    public PopulateGui(LeaderboardsPlugin plugin, String path)
    {
        super(plugin.getConfig().getInt(path + ".rows"), Color.color(plugin.getConfig().getString(path + ".name")), InteractionModifier.VALUES);

        // Assign
        this.plugin = plugin;
        this.path = path;
    }

    protected void populateGui()
    {
        // Populate the gui
        for (String key : plugin.getConfig().getConfigurationSection(this.path + ".items").getKeys(false))
        {
            // Args
            String path = this.path + ".items." + key;
            int slot = plugin.getConfig().getInt(path + ".slot");

            // Item
            this.setItem(slot, ItemBuilder
                    .from(new ItemStack(Material.getMaterial(plugin.getConfig().getString(path + ".material")), 1, (short) plugin.getConfig().getInt(path + ".data")))
                    .setName(Color.color(plugin.getConfig().getString(path + ".name")))
                    .setLore(Color.color(plugin.getConfig().getStringList(path + ".lore")))
                    .asGuiItem());

            if (!plugin.getConfig().isSet(path + ".action")) continue;
            GuiAction<InventoryClickEvent> action = this.getAction(plugin.getConfig().getString(path + ".action"));
            if (action != null) this.addSlotAction(slot, action);
        }

        // Fill the gui
        this.getFiller().fill(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName(" ").asGuiItem());
    }

    protected abstract GuiAction<InventoryClickEvent> getAction(String actionRaw);

    public void openLater(HumanEntity player)
    {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> this.open(player), 2L);
    }

}
