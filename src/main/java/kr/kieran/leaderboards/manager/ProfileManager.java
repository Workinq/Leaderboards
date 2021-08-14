package kr.kieran.leaderboards.manager;

import kr.kieran.leaderboards.model.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager
{

    private final Map<UUID, Profile> profiles = new HashMap<>();

    public void add(UUID uniqueId, Profile profile) { this.profiles.put(uniqueId, profile); }
    public void remove(UUID uniqueId) { this.profiles.remove(uniqueId); }
    public Profile get(UUID uniqueId) { return this.profiles.get(uniqueId); }

    public void disable() { this.profiles.clear(); }

}
