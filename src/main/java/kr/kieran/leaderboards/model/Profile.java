package kr.kieran.leaderboards.model;

import java.util.UUID;

public class Profile
{

    private final UUID uniqueId;
    public UUID getUniqueId() { return uniqueId; }

    // Store the player's last checked block (for afk checking)
    private PlayerLocation location;
    public PlayerLocation getLastLocation() { return location; }
    public void setLastLocation(PlayerLocation location) { this.location = location; }

    private long timePlayed;
    public long getTimePlayed() { return timePlayed; }
    public void setTimePlayed(long timePlayed) { this.timePlayed = timePlayed; }

    private int mobKills;
    public int getMobKills() { return mobKills; }
    public void setMobKills(int mobKills) { this.mobKills = mobKills; }

    private int blocksBroken;
    public int getBlocksBroken() { return blocksBroken; }
    public void setBlocksBroken(int blocksBroken) { this.blocksBroken = blocksBroken; }

    private int oresMined;
    public int getOresMined() { return oresMined; }
    public void setOresMined(int oresMined) { this.oresMined = oresMined; }

    private int woodMined;
    public int getWoodMined() { return woodMined; }
    public void setWoodMined(int woodMined) { this.woodMined = woodMined; }

    private int cropsHarvested;
    public int getCropsHarvested() { return cropsHarvested; }
    public void setCropsHarvested(int cropsHarvested) { this.cropsHarvested = cropsHarvested; }

    private int fishCaught;
    public int getFishCaught() { return fishCaught; }
    public void setFishCaught(int fishCaught) { this.fishCaught = fishCaught; }

    // TODO: Make a way to dynamically store statistics about the player rather than having to rewrite lots of code

    public Profile(UUID uniqueId) { this(uniqueId, 0L, 0, 0, 0, 0, 0, 0); }
    public Profile(UUID uniqueId, long timePlayed, int mobKills, int blocksBroken, int oresMined, int woodMined, int cropsHarvested, int fishCaught)
    {
        this.uniqueId = uniqueId;
        this.timePlayed = timePlayed;
        this.mobKills = mobKills;
        this.blocksBroken = blocksBroken;
        this.oresMined = oresMined;
        this.woodMined = woodMined;
        this.cropsHarvested = cropsHarvested;
        this.fishCaught = fishCaught;
    }

}
