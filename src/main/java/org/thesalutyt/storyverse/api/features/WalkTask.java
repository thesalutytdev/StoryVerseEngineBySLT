package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

public class WalkTask implements EnvResource {
    private final BlockPos pos;
    private final MobEntity entity;
    private final MobController controller;

    @Documentate(
            desc = "Makes entity walk to some position"
    )
    public WalkTask(BlockPos pos, MobEntity entity, MobController controller){
        this.pos = pos;
        this.entity = entity;
        this.controller = controller;
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
    public MobEntity getEntity() {
        return entity;
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
}
