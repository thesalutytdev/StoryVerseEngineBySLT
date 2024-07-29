package org.thesalutyt.storyverse.forge.api.features.managment;

import net.minecraft.core.BlockPos;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.annotation.Documentate;
import org.thesalutyt.storyverse.forge.api.environment.js.resource.EnvResource;
import net.minecraft.world.entity.Mob;

import java.util.UUID;

public class WalkTask extends ScriptableObject implements EnvResource {
    private final BlockPos pos;
    private final Mob entity;
    private final MobController controller;
    private final UUID taskID;
    public static WalkTask task = null;

    @Documentate(
            desc = "Makes entity walk to some position"
    )
    public WalkTask(BlockPos pos, Mob entity, MobController controller){
        this.pos = pos;
        this.entity = entity;
        this.controller = controller;
        this.taskID = UUID.randomUUID();
        task = this;
    }

    @Documentate(
            desc = "Returns entity's position"
    )
    public BlockPos getPos() {
        return pos;
    }

    @Documentate(
            desc = "Returns entity"
    )
    public Mob getEntity() {
        return entity;
    }
    public UUID getTaskID() {
        return this.taskID;
    }
    public String getTaskStringID() {
        return this.taskID.toString();
    }
    public WalkTask getTask() {
        return this;
    }
    public static WalkTask getTaskByID(String taskID) {
        return task;
    }
    @Documentate(
            desc = "Returns entity's controller"
    )
    public MobController getController() {
        return controller;
    }
    @Override
    public String getResourceId() {
        return "WalkTask";
    }

    @Override
    public String getClassName() {
        return "WalkTask";
    }
}
