package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.mozilla.javascript.RhinoException;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public class EventLoop {
    private final LoopExecutor executor;

    public EventLoop () {
        executor = new LoopExecutor();

        executor.start();

    }

    public void close () {
        executor.terminate();
        try {
            executor.join();
        } catch (final InterruptedException e) {
            System.out.println("Shit happens");
        }
    }

    public void runImmediate (Runnable task) {
        executor.runImmediate(task);
    }

    public Integer runTimeout (Runnable task, Integer delay) {
        return executor.runTimeout(task, delay);
    }

    public void resetTimeout (Integer id) {
        executor.resetTimeout(id);
    }

    public Integer runInterval (Runnable task, Integer delay) {
        return executor.runInterval(task, delay);
    }

    public void resetInterval (Integer id) {
        executor.resetInterval(id);
    }

    private static class LoopExecutor extends Thread {
        private static class TimedTask {
            public Runnable task;
            public Integer delta;
            public Integer initialDelta;

            public TimedTask (Runnable task, Integer delta) {
                this.task = task;
                this.delta = delta;
                this.initialDelta = delta;
            }
        }

        private boolean terminate = false;
        public ReentrantLock loopLock = new ReentrantLock();
        private final ArrayDeque<Runnable> taskQueue = new ArrayDeque<>();
        private final HashMap<Integer, TimedTask> timeouts = new HashMap<>();
        private int timeoutCounter = 0;
        private final HashMap<Integer, TimedTask> intervals = new HashMap<>();
        private int intervalCounter = 0;
        private static final int LOOP_SLEEP_DELAY = 10;

        private String printLoopError (Exception e) {
            if (RhinoException.class.isInstance(e)) {
                RhinoException re = (RhinoException) e;
                return String.format(
                        "RhinoException:\n%s\nIn source %s\nOn line %d",
                        re.getMessage(),
                        re.sourceName(),
                        re.lineNumber()
                );
            } else {
                StackTraceElement[] st = e.getStackTrace();
                String[] str_st = new String[st.length];

                for (int i=0; i<st.length; i++) {
                    str_st[i] = st[i].toString();
                }

                return String.format(
                        "Java exception:\n%s\n%s",
                        e.getMessage(),
                        String.join("\n", str_st)
                );
            }
        }

        public void run () {
            while (!terminate) {
                loopLock.lock();

                // loop through taskQueue and execute runnables
                while (!taskQueue.isEmpty()) {
                    Runnable task = taskQueue.remove();
                    try {
                        task.run();
                    } catch (final Exception e) {
                        System.out.println(printLoopError(e));
                    }
                }
                // loop through scheduled tasks and add them into task queue
                for (Map.Entry<Integer, TimedTask> pair : timeouts.entrySet()) {
                    TimedTask task = pair.getValue();
                    task.delta -= LOOP_SLEEP_DELAY;
                    if (task.delta <= 0) {
                        timeouts.remove(pair.getKey());
                        taskQueue.addLast(task.task);
                    }
                }

                // loop through intervals
                for (Map.Entry<Integer, TimedTask> pair : intervals.entrySet()) {
                    TimedTask task = pair.getValue();
                    task.delta -= LOOP_SLEEP_DELAY;
                    if (task.delta <= 0) {
                        task.delta = task.initialDelta;
                        taskQueue.addLast(task.task);
                    }
                }

                loopLock.unlock();
                // sleep
                try {
                    Thread.sleep(LOOP_SLEEP_DELAY);
                } catch (final InterruptedException e) {
                    System.out.println("Shit happens");
                }
            }
        }

        public void terminate () {
            terminate = true;
        }

        public void runImmediate (Runnable task) {
            loopLock.lock();

            taskQueue.addLast(task);

            loopLock.unlock();
        }

        public Integer runTimeout (Runnable task, Integer delay) {
            int id = this.timeoutCounter;
            this.timeoutCounter++;
            if (this.timeoutCounter == Integer.MAX_VALUE) {
                this.timeoutCounter = 0;
            }

            loopLock.lock();

            timeouts.put(id, new TimedTask(task, delay));

            loopLock.unlock();

            return id;
        }

        public void resetTimeout (Integer tId) {
            loopLock.lock();

            TimedTask task = timeouts.get(tId);
            if (task != null) {
                timeouts.remove(tId);
            }

            loopLock.unlock();
        }

        public Integer runInterval (Runnable task, Integer delay) {
            int id = this.intervalCounter;
            this.intervalCounter++;
            if (this.intervalCounter == Integer.MAX_VALUE) {
                this.intervalCounter = 0;
            }

            loopLock.lock();

            intervals.put(id, new TimedTask(task, delay));

            loopLock.unlock();

            return id;
        }

        public void resetInterval (Integer iId) {
            loopLock.lock();

            TimedTask task = intervals.get(iId);
            if (task != null) {
                intervals.remove(iId);
            }

            loopLock.unlock();
        }
    }
}