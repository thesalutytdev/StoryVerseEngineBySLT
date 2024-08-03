package org.thesalutyt.storyverse.api.environment.js.waiter;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.util.ArrayList;

public class WaitCondition extends Waiter implements EnvResource, JSResource {
    public static ArrayList<WaitCondition> activeWaiters = new ArrayList<>();
    public Thread thread;
    public Runnable action;
    public ArrayList<BaseFunction> action_ = new ArrayList<>();
    public int waitedTicks = 0;
    private int mode;

    public WaitCondition(boolean condition, Runnable action) {
        super(condition, action);
        activeWaiters.add(this);
        this.action = action;
        this.mode = 0;
    }

    public WaitCondition(boolean condition, ArrayList<BaseFunction> action) {
        super(condition, action);
        activeWaiters.add(this);
        this.action_ = action;
        this.mode = 1;
    }

    @Override
    public void tick() {
        if (mode == 0) {
            thread.start();
        }
        if (mode == 1) {
            this.thread = new Thread(() -> {
                if (!activeWaiters.contains(this)) {
                    return;
                }
                while (!condition) {
                    this.waitedTicks++;
                }
                EventLoop.getLoopInstance().runImmediate(() -> {
                    for (BaseFunction f : action_) {
                        f.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(),
                                SVEngine.interpreter.getScope(),
                                new Object[]{this.waitedTicks});
                    }
                    action_.clear();
                    activeWaiters.remove(this);
                });
            });
            thread.start();
        }
    }

    @Override
    public String getClassName() {
        return "WaitCondition";
    }

    @Override
    public String getResourceId() {
        return "WaitCondition";
    }

    @Override
    public void run() {

    }

    public static void tick(Integer tick) {
        for (WaitCondition w : activeWaiters) {
            w.tick();
        }
    }
}
