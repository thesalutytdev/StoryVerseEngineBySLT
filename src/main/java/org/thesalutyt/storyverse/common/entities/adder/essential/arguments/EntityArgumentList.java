package org.thesalutyt.storyverse.common.entities.adder.essential.arguments;

import org.mozilla.javascript.BaseFunction;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.quests.goal.Goal;

import java.util.ArrayList;

public class EntityArgumentList {
    public boolean invisible;
    public boolean invulnerable;
    public boolean silent;
    public ArrayList<BaseFunction> onTick = new ArrayList<>();
    public ArrayList<Goal> goals = new ArrayList<>();
    public boolean canPickUp;
    public ArrayList<BaseFunction> onPickup = new ArrayList<>();
    public ArrayList<BaseFunction> onInteract = new ArrayList<>();
    public ArrayList<BaseFunction> onDeath = new ArrayList<>();
    public boolean canOpenDoor;
    public boolean isAttackable;
    public String model;
    public String skin;
    public String animation;

    public EntityArgumentList() {}

    public EntityArgumentList setInvisible(boolean invisible) {
        this.invisible = invisible;
        return this;
    }

    public EntityArgumentList setOpenDoor(boolean canOpenDoor) {
        this.canOpenDoor = canOpenDoor;
        return this;
    }

    public EntityArgumentList setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
        return this;
    }

    public EntityArgumentList setSilent(boolean silent) {
        this.silent = silent;
        return this;
    }

    public EntityArgumentList setCanPickUp(boolean canPickUp) {
        this.canPickUp = canPickUp;
        return this;
    }

    public EntityArgumentList setAttackable(boolean isAttackable) {
        this.isAttackable = isAttackable;
        return this;
    }

    public EntityArgumentList setOnTick(BaseFunction onTick) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onTick.add(onTick);
        });
        return this;
    }

    public EntityArgumentList setOnPickup(BaseFunction onPickup) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onPickup.add(onPickup);
        });
        return this;
    }

    public EntityArgumentList setOnInteract(BaseFunction onInteract) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onInteract.add(onInteract);
        });
        return this;
    }

    public EntityArgumentList setOnDeath(BaseFunction onDeath) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onDeath.add(onDeath);
        });
        return this;
    }

    public EntityArgumentList setModel(String model) {
        this.model = model;
        return this;
    }

    public EntityArgumentList setSkin(String skin) {
        this.skin = skin;
        return this;
    }

    public EntityArgumentList setAnimation(String animation) {
        this.animation = animation;
        return this;
    }
}
