package kr.kieran.leaderboards.model;

public enum LeaderboardType
{

    OWN_FACTION(""),
    ALL_PLAYERS(""),
    ALL_FACTIONS("Factions"),

    ;

    private final String name;
    public String getName() { return name; }

    LeaderboardType(String name)
    {
        this.name = name;
    }

}
