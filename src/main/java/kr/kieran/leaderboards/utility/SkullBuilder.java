package kr.kieran.leaderboards.utility;

import dev.triumphteam.gui.builder.item.BaseItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class SkullBuilder extends BaseItemBuilder<dev.triumphteam.gui.builder.item.ItemBuilder>
{

    public SkullBuilder(ItemStack item)
    {
        super(item);
    }

    public static SkullBuilder from(ItemStack itemStack)
    {
        return new SkullBuilder(itemStack);
    }

    public static SkullBuilder from(String texture)
    {
        UUID hashAsId = new UUID(texture.hashCode(), texture.hashCode());
        ItemStack modified = Bukkit.getUnsafe().modifyItemStack(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3), "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + texture + "\"}]}}}");
        return new SkullBuilder(modified);
    }

    public SkullBuilder itemName(String name)
    {
        ItemMeta meta = this.getMeta();
        meta.setDisplayName(name);
        this.setMeta(meta);
        return this;
    }

    public SkullBuilder itemLore(List<String> lore)
    {
        ItemMeta meta = this.getMeta();
        meta.setLore(lore);
        this.setMeta(meta);
        return this;
    }

    public SkullBuilder owner(OfflinePlayer player)
    {
        SkullMeta skullMeta = (SkullMeta) this.getMeta();
        skullMeta.setOwner(player.getName());
        this.setMeta(skullMeta);
        return this;
    }

}
