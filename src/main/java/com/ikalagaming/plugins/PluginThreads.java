package com.ikalagaming.plugins;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Threads and executors that belong to a plugin. The plugin manager stops them when the plugin
 * unloads, after its {@link Plugin#onUnload()}, so a plugin that forgets one doesn't keep running
 * code from a jar that is gone, or keep its classes in memory. Plugins should still shut their
 * executors down themselves, in order, where that matters; this is the backstop.
 *
 * <p>Threads are daemon threads named {@code <plugin>/<name>-<n>}, so they show who owns them in a
 * thread dump.
 *
 * @author Ches Burks
 */
@Slf4j
public final class PluginThreads {
    /** How long unloading waits for a plugin's threads to finish after interrupting them. */
    static final Duration STOP_WAIT = Duration.ofSeconds(5);

    /** Guards the maps. */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /** Executors by owning plugin. */
    private static final Map<String, List<ExecutorService>> EXECUTORS = new HashMap<>();

    /** Threads made directly, by owning plugin. */
    private static final Map<String, List<Thread>> THREADS = new HashMap<>();

    /**
     * Make threads for a plugin. Threads made by it are stopped with the plugin, but only if they
     * run in an executor from {@link #track} or one of the methods here; use {@link #thread} for a
     * lone thread.
     *
     * @param owner The plugin.
     * @param name What the threads do, for their names.
     * @return The factory.
     */
    public static ThreadFactory factory(@NonNull String owner, @NonNull String name) {
        final AtomicInteger count = new AtomicInteger();
        final String prefix = owner + "/" + name + "-";
        return task -> {
            Thread thread = new Thread(task, prefix + count.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
    }

    /**
     * Make a fixed size thread pool owned by a plugin.
     *
     * @param owner The plugin.
     * @param name What the threads do, for their names.
     * @param threads How many threads.
     * @return The pool.
     */
    public static ExecutorService fixedPool(
            @NonNull String owner, @NonNull String name, int threads) {
        return track(owner, Executors.newFixedThreadPool(threads, factory(owner, name)));
    }

    /**
     * Make a single thread executor owned by a plugin.
     *
     * @param owner The plugin.
     * @param name What the thread does, for its name.
     * @return The executor.
     */
    public static ExecutorService singleThread(@NonNull String owner, @NonNull String name) {
        return track(owner, Executors.newSingleThreadExecutor(factory(owner, name)));
    }

    /**
     * Make a single thread scheduled executor owned by a plugin.
     *
     * @param owner The plugin.
     * @param name What the thread does, for its name.
     * @return The executor.
     */
    public static ScheduledExecutorService scheduled(@NonNull String owner, @NonNull String name) {
        return track(owner, Executors.newSingleThreadScheduledExecutor(factory(owner, name)));
    }

    /**
     * Make a thread owned by a plugin, not yet started. It is interrupted and waited for when the
     * plugin unloads.
     *
     * @param owner The plugin.
     * @param name The thread name, after the plugin's.
     * @param task What it runs.
     * @return The thread.
     */
    public static Thread thread(@NonNull String owner, @NonNull String name, Runnable task) {
        Thread thread = new Thread(task, owner + "/" + name);
        thread.setDaemon(true);
        LOCK.lock();
        try {
            List<Thread> threads = THREADS.computeIfAbsent(owner, ignored -> new ArrayList<>());
            threads.removeIf(old -> old.getState() == Thread.State.TERMINATED);
            threads.add(thread);
        } finally {
            LOCK.unlock();
        }
        return thread;
    }

    /**
     * Have an executor made elsewhere stopped with a plugin, like one with a custom queue.
     *
     * @param <E> The kind of executor.
     * @param owner The plugin.
     * @param executor The executor.
     * @return The executor.
     */
    public static <E extends ExecutorService> E track(@NonNull String owner, @NonNull E executor) {
        LOCK.lock();
        try {
            List<ExecutorService> executors =
                    EXECUTORS.computeIfAbsent(owner, ignored -> new ArrayList<>());
            executors.removeIf(ExecutorService::isTerminated);
            executors.add(executor);
        } finally {
            LOCK.unlock();
        }
        return executor;
    }

    /**
     * Stop everything a plugin owns: interrupt its threads and running tasks, drop queued tasks,
     * and wait a little for them to finish. Anything still running afterward is logged.
     *
     * @param owner The plugin.
     * @return How many threads or executors were still running when the wait ran out.
     */
    static int stopAll(@NonNull String owner) {
        List<ExecutorService> executors;
        List<Thread> threads;
        LOCK.lock();
        try {
            executors = EXECUTORS.remove(owner);
            threads = THREADS.remove(owner);
        } finally {
            LOCK.unlock();
        }
        if (executors == null) {
            executors = List.of();
        }
        if (threads == null) {
            threads = List.of();
        }
        int stopped = 0;
        for (ExecutorService executor : executors) {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
                ++stopped;
            }
        }
        for (Thread thread : threads) {
            if (thread.isAlive()) {
                thread.interrupt();
                ++stopped;
            }
        }
        if (stopped > 0) {
            log.info("Stopping {} threads or executors left running by {}", stopped, owner);
        }
        final long deadline = System.nanoTime() + STOP_WAIT.toNanos();
        int stuck = 0;
        try {
            for (ExecutorService executor : executors) {
                if (!executor.awaitTermination(remaining(deadline), TimeUnit.NANOSECONDS)) {
                    log.warn("An executor of {} did not stop in time: {}", owner, executor);
                    ++stuck;
                }
            }
            for (Thread thread : threads) {
                thread.join(Math.max(1, TimeUnit.NANOSECONDS.toMillis(remaining(deadline))));
                if (thread.isAlive()) {
                    log.warn("The thread {} of {} did not stop in time", thread.getName(), owner);
                    ++stuck;
                }
            }
        } catch (InterruptedException e) {
            log.warn("Interrupted while stopping the threads of {}", owner);
            Thread.currentThread().interrupt();
        }
        return stuck;
    }

    /**
     * The time left before a deadline.
     *
     * @param deadline The deadline, from {@link System#nanoTime()}.
     * @return The nanoseconds left, at least zero.
     */
    private static long remaining(long deadline) {
        return Math.max(0, deadline - System.nanoTime());
    }

    /** Static helpers only. */
    private PluginThreads() {}
}
