package org.thesalutyt.storyverse.api.environment.js.waiter;

public class WaiterData {
    public Waiter self;
    public boolean condition;
    public boolean isJS;

    public WaiterData(Waiter self, boolean isJS) {
        this.self = self;
        this.condition = self.condition;
        this.isJS = isJS;
    }
}
