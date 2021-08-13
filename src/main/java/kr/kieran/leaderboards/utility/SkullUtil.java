package kr.kieran.leaderboards.utility;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import kr.kieran.leaderboards.LeaderboardsPlugin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkullUtil
{

    private static final Map<UUID, String> TEXTURE_CACHE = new HashMap<>();
    private static final LeaderboardsPlugin PLUGIN = JavaPlugin.getPlugin(LeaderboardsPlugin.class);

    public static boolean textureExists(UUID uniqueId) { return TEXTURE_CACHE.containsKey(uniqueId); }
    public static void cacheTexture(UUID uniqueId, String texture) { TEXTURE_CACHE.put(uniqueId, texture); }
    public static void cacheTexture(Player player)
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Property property = Iterables.getFirst(entityPlayer.getProfile().getProperties().get("textures"), null);
        if (property != null) TEXTURE_CACHE.put(player.getUniqueId(), property.getValue());
    }

    public static ItemStack getSkullItem(UUID uuid, String name)
    {
        // Args
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        String texture = TEXTURE_CACHE.getOrDefault(uuid, null);

        // Get the skull texture for the specified player
        if (texture != null)
        {
            net.minecraft.server.v1_8_R3.ItemStack vanillaStack = CraftItemStack.asNMSCopy(head);
            NBTTagCompound baseCompound = vanillaStack.getTag();
            if (baseCompound == null) baseCompound = new NBTTagCompound();
            GameProfile profile = new GameProfile(uuid, name);
            profile.getProperties().put("textures", new Property("textures", texture));
            NBTTagCompound skullOwner = new NBTTagCompound();
            GameProfileSerializer.serialize(skullOwner, profile);
            baseCompound.set("SkullOwner", skullOwner);
            vanillaStack.setTag(baseCompound);
            return CraftItemStack.asCraftCopy(CraftItemStack.asBukkitCopy(vanillaStack));
        }

        // Meta
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(name);
        head.setItemMeta(skullMeta);

        // Cache
        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> {
            String textureLine = SkullUtil.getSkullTexture(head);
            if (textureLine != null) TEXTURE_CACHE.putIfAbsent(uuid, textureLine);
        }, 20L);
        return head;
    }

    public static String getSkullTexture(ItemStack itemStack)
    {
        net.minecraft.server.v1_8_R3.ItemStack vanillaStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound baseCompound = vanillaStack.getTag();
        if (baseCompound == null) return null;
        NBTTagCompound skullOwner = baseCompound.getCompound("SkullOwner");
        if (skullOwner == null) return null;
        GameProfile profile = GameProfileSerializer.deserialize(skullOwner);
        if (profile == null) return null;
        Property property = Iterables.getFirst(profile.getProperties().get("textures"), null);
        return (property == null) ? null : property.getValue();
    }

}
