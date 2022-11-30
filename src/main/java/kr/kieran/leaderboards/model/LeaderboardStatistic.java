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

    // BLOCK STATISTICS
    BLOCKS_BROKEN("Blocks Broken", "blocks_broken", LeaderboardStatistic::insertCommas),
    BLOCKS_TRAVELLED("Blocks Travelled", "blocks_travelled", value -> insertCommas(value / 100)),
    ORES_MINED("Ores Mined", "ores_mined", LeaderboardStatistic::insertCommas),
    WOOD_MINED("Wood Mined", "wood_mined", LeaderboardStatistic::insertCommas),

    // FARMING STATISTICS
    CROPS_HARVESTED("Crops Harvested", "crops_harvested", LeaderboardStatistic::insertCommas),
    FISH_CAUGHT("Fish Caught", "fish_caught", LeaderboardStatistic::insertCommas),

    // End of list
    ;

    private final String niceName;
    public String getNiceName() { return niceName; }

    private final String columnName;
    public String getColumnName() { return columnName; }

    private final Function<Integer, String> formattedValue;
    public Function<Integer, String> getFormattedValue() { return formattedValue; }

    LeaderboardStatistic(String niceName, String columnName, Function<Integer, String> formattedValue)
    {
        this.niceName = niceName;
        this.columnName = columnName;
        this.formattedValue = formattedValue;
    }

    private static String insertCommas(int value) { return String.format("%,d", value); }

}
