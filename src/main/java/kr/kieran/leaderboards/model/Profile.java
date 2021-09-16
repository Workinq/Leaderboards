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

    private int deaths;
    public int getDeaths() { return deaths; }
    public void setDeaths(int deaths) { this.deaths = deaths; }

    private int playerKills;
    public int getPlayerKills() { return playerKills; }
    public void setPlayerKills(int playerKills) { this.playerKills = playerKills; }

    private int blocksBroken;
    public int getBlocksBroken() { return blocksBroken; }
    public void setBlocksBroken(int blocksBroken) { this.blocksBroken = blocksBroken; }

    private int blocksPlaced;
    public int getBlocksPlaced() { return blocksPlaced; }
    public void setBlocksPlaced(int blocksPlaced) { this.blocksPlaced = blocksPlaced; }

    private int spawnersPlaced;
    public int getSpawnersPlaced() { return spawnersPlaced; }
    public void setSpawnersPlaced(int spawnersPlaced) { this.spawnersPlaced = spawnersPlaced; }

    private int envoyClaims;
    public int getEnvoyClaims() { return envoyClaims; }
    public void setEnvoyClaims(int envoyClaims) { this.envoyClaims = envoyClaims; }

    public Profile(UUID uniqueId) { this(uniqueId, 0L, 0, 0, 0, 0, 0, 0, 0); }
    public Profile(UUID uniqueId, long timePlayed, int mobKills, int deaths, int playerKills, int blocksBroken, int blocksPlaced, int spawnersPlaced, int envoyClaims)
    {
        this.uniqueId = uniqueId;
        this.timePlayed = timePlayed;
        this.mobKills = mobKills;
        this.deaths = deaths;
        this.playerKills = playerKills;
        this.blocksBroken = blocksBroken;
        this.blocksPlaced = blocksPlaced;
        this.spawnersPlaced = spawnersPlaced;
        this.envoyClaims = envoyClaims;
    }

}
