package kr.kieran.leaderboards.utility;

import java.util.concurrent.TimeUnit;

public class TimeUtil
{

    public static String formatTicks(int ticks) { return formatTime(ticks * 50L); }
    public static String formatSeconds(int seconds) { return formatTime(seconds * 1000L); }
    public static String formatTime(long millis)
    {
        StringBuilder builder = new StringBuilder();

        // Calculate days, hours, minutes and seconds
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        // Append each time unit if it's not 0
        if (days >= 1L) builder.append(days).append("d ");
        if (hours >= 1L) builder.append(hours).append("h ");
        if (minutes >= 1L) builder.append(minutes).append("m ");
        if (seconds >= 1L) builder.append(seconds).append("s");

        // Return
        return builder.length() == 0 ? "0s" : builder.toString().trim();
    }

}
