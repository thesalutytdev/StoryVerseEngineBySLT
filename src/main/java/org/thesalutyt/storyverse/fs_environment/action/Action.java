package org.thesalutyt.storyverse.fs_environment.action;

import java.util.function.Consumer;

public class Action {
    private Consumer<ActionPacketData> actionFunc;
    private ActionEvent event;

    public Action(Consumer<ActionPacketData> act, ActionEvent ev) {
        this.actionFunc = act;
        this.event = ev;
    }

    public Consumer<ActionPacketData> getActionFunc() {
        return this.actionFunc;
    }

    public ActionEvent getEvent() {
        return this.event;
    }
}
