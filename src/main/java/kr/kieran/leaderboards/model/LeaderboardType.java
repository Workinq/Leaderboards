package kr.kieran.leaderboards.model;

public enum LeaderboardType
{

    OWN_ISLAND(""),
    ALL_PLAYERS(""),
    ALL_ISLANDS("Islands"),

    ;

    private final String name;
    public String getName() { return name; }

    LeaderboardType(String name)
    {
        this.name = name;
    }

}
