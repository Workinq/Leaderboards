package kr.kieran.leaderboards.task;

import kr.kieran.leaderboards.utility.EmptyCallable;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CompletableTask extends BukkitRunnable
{

    private boolean completed = false;
    private final EmptyCallable callable;

    public CompletableTask(EmptyCallable callable)
    {
        this.callable = callable;
    }

    public void complete()
    {
        if (this.completed) return;
        this.callable.call();
        this.completed = true;
    }

}
