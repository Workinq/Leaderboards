package kr.kieran.leaderboards.utility;

import org.bukkit.Material;

import java.util.EnumSet;

public class MaterialUtil
{

    private static final EnumSet<Material> ORE_MATERIALS = EnumSet.of(Material.EMERALD_ORE, Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE, Material.GOLD_ORE, Material.LAPIS_ORE, Material.IRON_ORE, Material.COAL_ORE, Material.QUARTZ_ORE);
    public static boolean isOre(Material material) { return ORE_MATERIALS.contains(material); }

    private static final EnumSet<Material> LOG_MATERIALS = EnumSet.of(Material.LOG, Material.LOG_2);
    public static boolean isLog(Material material) { return LOG_MATERIALS.contains(material); }

}
