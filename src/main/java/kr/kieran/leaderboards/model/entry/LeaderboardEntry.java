package kr.kieran.leaderboards.model.entry;

public class LeaderboardEntry<T>
{

    private final T represented;
    public T getRepresented() { return represented; }

    private final int value;
    public int getValue() { return value; }

    public LeaderboardEntry(T represented, int value)
    {
        this.represented = represented;
        this.value = value;
    }

}
