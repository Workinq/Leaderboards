package kr.kieran.leaderboards.model;

import java.util.UUID;

public class Profile
{

    private final UUID uniqueId;
    public UUID getUniqueId() { return uniqueId; }

    private long timePlayed;
    public long getTimePlayed() { return timePlayed; }
    public void setTimePlayed(long timePlayed) { this.timePlayed = timePlayed; }

    private int blocksBroken;
    public int getBlocksBroken() { return blocksBroken; }
    public void setBlocksBroken(int blocksBroken) { this.blocksBroken = blocksBroken; }

    private int blocksPlaced;
    public int getBlocksPlaced() { return blocksPlaced; }
    public void setBlocksPlaced(int blocksPlaced) { this.blocksPlaced = blocksPlaced; }

    private int spawnersPlaced;
    public int getSpawnersPlaced() { return spawnersPlaced; }
    public void setSpawnersPlaced(int spawnersPlaced) { this.spawnersPlaced = spawnersPlaced; }

    private int lmsWins;
    public int getLmsWins() { return lmsWins; }
    public void setLmsWins(int lmsWins) { this.lmsWins = lmsWins; }

    private int envoyClaims;
    public int getEnvoyClaims() { return envoyClaims; }
    public void setEnvoyClaims(int envoyClaims) { this.envoyClaims = envoyClaims; }

    public Profile(UUID uniqueId) { this(uniqueId, 0L, 0, 0, 0, 0 ,0); }
    public Profile(UUID uniqueId, long timePlayed, int blocksBroken, int blocksPlaced, int spawnersPlaced, int lmsWins, int envoyClaims)
    {
        this.uniqueId = uniqueId;
        this.timePlayed = timePlayed;
        this.blocksPlaced = blocksPlaced;
        this.blocksBroken = blocksBroken;
        this.spawnersPlaced = spawnersPlaced;
        this.lmsWins = lmsWins;
        this.envoyClaims = envoyClaims;
    }

}
