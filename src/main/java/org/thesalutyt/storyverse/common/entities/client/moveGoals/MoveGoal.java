package org.thesalutyt.storyverse.common.entities.client.moveGoals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class MoveGoal extends Goal {
    protected final CreatureEntity mob;
    protected double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    private static int counter = 1000;
    public boolean finished = false;
    public MoveGoal(MobEntity creature, BlockPos pos, double speedIn) {
        mob = (CreatureEntity) creature;
        speedModifier = speedIn;
        posX = pos.getX();
        posY = pos.getY();
        posZ = pos.getZ();
    }
    public MoveGoal(MobEntity creature, double x, double y, double z, float speedIn) {
        mob = (CreatureEntity) creature;
        speedModifier = Double.parseDouble(String.valueOf(speedIn));
        posX = x;
        posY = y;
        posZ = z;
    }

    public boolean canUse() {
        return true;
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        // this.mob.goalSelector.addGoal(counter, new MoveGoal(this.mob, new BlockPos(this.posX, this.posY, this.posZ), this.speedModifier));
        // counter++;
    }
    public boolean canContinueToUse() {
        return false;
    }
}
