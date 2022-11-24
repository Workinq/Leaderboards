package kr.kieran.leaderboards.model.entry;

public class IndexedLeaderboardEntry<T> extends LeaderboardEntry<T>
{

    private final int index;
    public int getIndex() { return index; }

    public IndexedLeaderboardEntry(T represented, int value, int index)
    {
        super(represented, value);

        // Assign
        this.index = index;
    }

}
