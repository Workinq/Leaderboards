package kr.kieran.leaderboards.model;

public enum LeaderboardStatistic
{

    // TIME STATISTICS
    TIME_CONNECTED("Time Connected", "time_connected"),
    TIME_PLAYED("Time Played", "time_played"),

    // COMBAT STATISTICS
    MOB_KILLS("Mobs Kills", "mob_kills"),
    PLAYER_DEATHS("Player Deaths", "player_deaths"),
    PLAYER_KILLS("Player Kills", "player_kills"),

    // BLOCK STATISTICS
    BLOCKS_BROKEN("Blocks Broken", "blocks_broken"),
    BLOCKS_PLACED("Blocks Placed", "blocks_placed"),
    BLOCKS_TRAVELLED("Blocks Travelled", "blocks_travelled"),
    CANE_BROKEN("Sugar Cane Broken", "cane_broken"),
    SPAWNERS_PLACED("Mob Spawners Placed", "spawners_placed"),

    // EVENT STATISTICS
    LMS_WINS("LMS Wins", "lms_wins"),
    ENVOY_CLAIMS("Envoys Claimed", "envoy_claims"),
    KOTH_WINS("KoTH Wins", "koth_wins"),

    ;

    private final String niceName;
    public String getNiceName() { return niceName; }

    private final String columnName;
    public String getColumnName() { return columnName; }

    LeaderboardStatistic(String niceName, String columnName)
    {
        this.niceName = niceName;
        this.columnName = columnName;
    }
}
