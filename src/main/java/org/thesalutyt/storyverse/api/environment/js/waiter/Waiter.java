package org.thesalutyt.storyverse.api.environment.js.waiter;

import org.mozilla.javascript.BaseFunction;

import java.util.ArrayList;

public abstract class Waiter {
    public boolean condition;
    public Runnable code;
    public ArrayList<BaseFunction> jsCode = new ArrayList<>();

    public Waiter(boolean condition, Runnable code) {
        this.condition = condition;
        this.code = code;
    }

    public Waiter(boolean condition, ArrayList<BaseFunction> jsCode) {
        this.condition = condition;
        this.jsCode = jsCode;
    }

    public abstract void run();
    public abstract void tick();
}
