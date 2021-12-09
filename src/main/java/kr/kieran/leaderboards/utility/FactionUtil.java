package kr.kieran.leaderboards.utility;

import com.massivecraft.factions.entity.Faction;

public class FactionUtil
{

    public static boolean isSystemFaction(Faction faction)
    {
        String id = faction.getId();
        return id == null || id.equals("none") || id.equals("safezone") || id.equals("warzone") || id.equals("Treasure") || id.equals("RaidOutpost");
    }

}
