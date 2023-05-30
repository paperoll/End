package me.ht9.end.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Threading
{
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static double timedSyncRun(Runnable runnable)
    {
        long startTime = System.currentTimeMillis();
        runnable.run();
        return (System.currentTimeMillis() - startTime) / 1000.0D;
    }

    public static <T> Future<T> runTask(Callable<T> callable)
    {
        return executor.submit(callable);
    }
}