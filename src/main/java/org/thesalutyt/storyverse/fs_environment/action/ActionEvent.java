package org.thesalutyt.storyverse.fs_environment.action;

import org.thesalutyt.storyverse.annotations.Documentate;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;

public class ActionEvent {
    public int type = 0;

    public ActionEvent() {
    }

    @Documentate(
            desc = "Create default action event. Activates on PlayAction keybinding."
    )
    public static ActionEvent DEF() {
        return new ActionEvent();
    }

    @Documentate(
            desc = "Creates on message sent action event. Activates when a message with defined keywords is sent."
    )
    public static ActionEvent MSG_SENT(String[] keyWords) {
        return new MessageSentActionEvent(keyWords);
    }

    @Documentate(
            desc = "Creates on ticks passed (delay) action event. Activates when timer in ticks is finished."
    )
    public static ActionEvent DELAY(int ticks) {
        return new DelayActionEvent(ticks);
    }

    @Documentate(
            desc = "Creates when entity is in radius of coordinates event. Activates when entity is in radius of coordinates."
    )
    public static ActionEvent POSITIONED(Supplier<Entity> entity, Supplier<double[]> position, double radius) {
        return new PositionedActionEvent(entity, position, radius);
    }

    public static class PositionedActionEvent extends ActionEvent {
        public int type = 3;
        public Supplier<Entity> entity;
        public Supplier<double[]> position;
        public double radius;

        public PositionedActionEvent(Supplier<Entity> entity, Supplier<double[]> position, double radius) {
            this.type = 3;
            super.type = 3;
            this.entity = entity;
            this.position = position;
            this.radius = radius;
        }
    }

    public static class DelayActionEvent extends ActionEvent {
        public final int ticks;
        public int type = 2;

        public DelayActionEvent(int ticks) {
            this.type = 2;
            super.type = 2;
            this.ticks = ticks;
        }
    }

    public static class MessageSentActionEvent extends ActionEvent {
        public final String[] keyWords;
        public int type = 1;

        public MessageSentActionEvent(String[] keyWords) {
            this.type = 1;
            super.type = 1;
            this.keyWords = keyWords;
        }
    }
}

