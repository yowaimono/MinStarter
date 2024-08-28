package com.reservoir.core.utils;

import java.util.concurrent.*;

public class Go {
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

    // 无返回值的执行方法
    public static void Run(Runnable task) {
        executor.submit(task);
    }

    // 有返回值的执行方法，返回 Future<T>
    public static <T> Future<T> Run(Callable<T> task) {
        return executor.submit(task);
    }

    // 延迟执行任务
    public static void RunDelayed(Runnable task, long delay, TimeUnit unit) {
        scheduledExecutor.schedule(task, delay, unit);
    }

    // 周期性执行任务
    public static void RunAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduledExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    // 关闭执行器
    public static void shutdown() {
        executor.shutdown();
        scheduledExecutor.shutdown();
    }

}