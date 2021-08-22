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

    private int spawnersPlaced;
    public int getSpawnersPlaced() { return spawnersPlaced; }
    public void setSpawnersPlaced(int spawnersPlaced) { this.spawnersPlaced = spawnersPlaced; }

    private int envoyClaims;
    public int getEnvoyClaims() { return envoyClaims; }
    public void setEnvoyClaims(int envoyClaims) { this.envoyClaims = envoyClaims; }

    public Profile(UUID uniqueId) { this(uniqueId, 0L, 0, 0); }
    public Profile(UUID uniqueId, long timePlayed, int spawnersPlaced, int envoyClaims)
    {
        this.uniqueId = uniqueId;
        this.timePlayed = timePlayed;
        this.spawnersPlaced = spawnersPlaced;
        this.envoyClaims = envoyClaims;
    }

}
