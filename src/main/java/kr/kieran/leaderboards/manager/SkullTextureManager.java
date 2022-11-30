package kr.kieran.leaderboards.manager;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkullTextureManager
{

    private final LeaderboardsPlugin plugin;

    // Texture cache
    private final Map<UUID, String> textures = new HashMap<>();
    public Map<UUID, String> getTextures() { return Collections.unmodifiableMap(textures); }

    public SkullTextureManager(LeaderboardsPlugin plugin)
    {
        this.plugin = plugin;
        this.registerTextures();
    }

    private void registerTextures()
    {
        plugin.newChain()
                .async(() -> {
                    try (
                            Connection connection = plugin.getDatabaseManager().getConnection();
                            PreparedStatement statement = connection.prepareStatement("SELECT `leaderboards_skulls`.`unique_id`, `leaderboards_skulls`.`texture` FROM `leaderboards_skulls`;")
                    )
                    {
                        ResultSet resultSet = statement.executeQuery();
                        while (resultSet.next())
                        {
                            textures.put(
                                    UUID.fromString(resultSet.getString("unique_id")),
                                    resultSet.getString("texture")
                            );
                        }
                    }
                    catch (SQLException e)
                    {
                        plugin.getLogger().severe("Failed to register cached skull textures: " + e.getMessage());
                    }
                })
                .execute();
    }

    public String get(UUID uniqueId) { return textures.get(uniqueId); }
    public void add(UUID uniqueId, String texture) { textures.put(uniqueId, texture); }

    public String getTextureFrom(Player player)
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Property property = Iterables.getFirst(entityPlayer.getProfile().getProperties().get("textures"), null);
        return property == null ? null : property.getValue();
    }

    public ItemStack getSkullItem(UUID uuid, String name)
    {
        // Args
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        String texture = textures.getOrDefault(uuid, null);

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
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            String textureLine = this.getSkullTexture(head);
            if (textureLine != null) textures.putIfAbsent(uuid, textureLine);
        }, 20L);
        return head;
    }

    public String getSkullTexture(ItemStack itemStack)
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

    public void disable() { textures.clear(); }

}
