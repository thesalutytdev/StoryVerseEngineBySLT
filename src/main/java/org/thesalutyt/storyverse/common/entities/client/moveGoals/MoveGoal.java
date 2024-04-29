package org.thesalutyt.storyverse.common.entities.client.moveGoals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class MoveGoal extends Goal {
    protected final CreatureEntity mob;
    protected double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;

    public MoveGoal(MobEntity creature, BlockPos pos, double speedIn) {
        mob = (CreatureEntity) creature;
        speedModifier = speedIn;
        posX = pos.getX();
        posY = pos.getY();
        posZ = pos.getZ();
    }

    public boolean canUse() {
        return true;
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }
    public boolean canContinueToUse() {
        return false;
    }
}
