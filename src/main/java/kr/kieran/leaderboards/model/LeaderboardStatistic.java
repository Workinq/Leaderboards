package kr.kieran.leaderboards.model;

import kr.kieran.leaderboards.utility.TimeUtil;

import java.util.function.Function;

public enum LeaderboardStatistic
{

    // TIME STATISTICS
    TIME_CONNECTED("Time Connected", "time_connected", TimeUtil::formatTicks),
    TIME_PLAYED("Time Played", "time_played", TimeUtil::formatSeconds),

    // COMBAT STATISTICS
    MOB_KILLS("Mobs Kills", "mob_kills", LeaderboardStatistic::insertCommas),
    PLAYER_DEATHS("Player Deaths", "player_deaths", LeaderboardStatistic::insertCommas),
    PLAYER_KILLS("Player Kills", "player_kills", LeaderboardStatistic::insertCommas),

    // BLOCK STATISTICS
    BLOCKS_BROKEN("Blocks Broken", "blocks_broken", LeaderboardStatistic::insertCommas),
    BLOCKS_PLACED("Blocks Placed", "blocks_placed", LeaderboardStatistic::insertCommas),
    BLOCKS_TRAVELLED("Blocks Travelled", "blocks_travelled", value -> { return insertCommas(value / 100); }),
    SPAWNERS_PLACED("Mob Spawners Placed", "spawners_placed", LeaderboardStatistic::insertCommas),

    // EVENT STATISTICS
    EVENT_WINS("Event Wins", "event_wins", LeaderboardStatistic::insertCommas),

    ;

    private final String niceName;
    public String getNiceName() { return niceName; }

    private final String columnName;
    public String getColumnName() { return columnName; }

    private final Function<Integer, String> formattedValue;
    public Function<Integer, String> getFormattedValue() { return this.formattedValue; }

    LeaderboardStatistic(String niceName, String columnName, Function<Integer, String> formattedValue)
    {
        this.niceName = niceName;
        this.columnName = columnName;
        this.formattedValue = formattedValue;
    }

    private static String insertCommas(int value) { return String.format("%,d", value); }

}
