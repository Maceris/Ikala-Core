package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Tests for {@link PluginThreads} and the thread search in {@link UnloadCheck}. */
class PluginThreadsTest {
    private static final String OWNER = "ThreadsTest";

    @Test
    void stopAllStopsExecutorsAndThreads() throws InterruptedException {
        ExecutorService pool = PluginThreads.fixedPool(OWNER, "pool", 2);
        ScheduledExecutorService timer = PluginThreads.scheduled(OWNER, "timer");
        CountDownLatch started = new CountDownLatch(2);
        pool.submit(
                () -> {
                    started.countDown();
                    Thread.sleep(60_000);
                    return null;
                });
        timer.scheduleAtFixedRate(() -> {}, 0, 10, TimeUnit.MILLISECONDS);
        Thread lone =
                PluginThreads.thread(
                        OWNER,
                        "lone",
                        () -> {
                            started.countDown();
                            try {
                                Thread.sleep(60_000);
                            } catch (InterruptedException e) {
                                // Stopping, as asked
                            }
                        });
        lone.start();
        assertTrue(started.await(5, TimeUnit.SECONDS));

        assertEquals(0, PluginThreads.stopAll(OWNER));

        assertTrue(pool.isTerminated());
        assertTrue(timer.isTerminated());
        assertFalse(lone.isAlive());
        assertEquals(0, PluginThreads.stopAll(OWNER), "Nothing is left to stop");
    }

    @Test
    void threadsAreNamedAndDaemons() throws Exception {
        ExecutorService single = PluginThreads.singleThread(OWNER, "worker");
        try {
            Thread thread = single.submit(Thread::currentThread).get(5, TimeUnit.SECONDS);
            assertEquals(OWNER + "/worker-1", thread.getName());
            assertTrue(thread.isDaemon());
        } finally {
            PluginThreads.stopAll(OWNER);
        }
    }

    @Test
    void otherOwnersAreLeftRunning() throws InterruptedException {
        ExecutorService mine = PluginThreads.singleThread(OWNER, "mine");
        ExecutorService theirs = PluginThreads.singleThread("Other", "theirs");
        try {
            PluginThreads.stopAll(OWNER);
            assertTrue(mine.isTerminated());
            assertFalse(theirs.isShutdown());
        } finally {
            PluginThreads.stopAll("Other");
        }
    }

    @Test
    void stuckThreadsAreCounted() throws InterruptedException {
        CountDownLatch release = new CountDownLatch(1);
        Thread stubborn =
                PluginThreads.thread(
                        OWNER,
                        "stubborn",
                        () -> {
                            while (release.getCount() > 0) {
                                // Ignores interrupts, like a native call or a busy loop
                                Thread.onSpinWait();
                            }
                        });
        stubborn.start();
        try {
            assertEquals(1, PluginThreads.stopAll(OWNER));
        } finally {
            release.countDown();
            stubborn.join(5000);
        }
    }

    @Test
    void unloadCheckFindsThreadsByContextLoader() throws InterruptedException {
        ClassLoader loader = new ClassLoader("plugin:Probe", null) {};
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        Thread thread =
                new Thread(
                        () -> {
                            started.countDown();
                            try {
                                release.await();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        },
                        "probe");
        thread.setContextClassLoader(loader);
        thread.start();
        try {
            assertTrue(started.await(5, TimeUnit.SECONDS));
            List<String> found = UnloadCheck.threadsRunning(loader.getName(), loader);
            assertTrue(found.stream().anyMatch(name -> name.startsWith("probe")), found::toString);
        } finally {
            release.countDown();
            thread.join(5000);
        }
    }

    @Test
    void pluginLoadersAreNamedForTheirPlugin() {
        assertEquals("plugin:Example", PluginClassLoader.loaderName("Example"));
    }
}
