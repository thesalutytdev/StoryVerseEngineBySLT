package org.thesalutyt.storyverse.forge.common.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ambient.AmbientCreature;

public class MoveGoal extends Goal {
    protected final AmbientCreature mob;
    protected double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    private static int counter = 1000;
    public boolean finished = false;
    public MoveGoal(Mob creature, BlockPos pos, double speedIn) {
        mob = (AmbientCreature) creature;
        speedModifier = speedIn;
        posX = pos.getX();
        posY = pos.getY();
        posZ = pos.getZ();
    }
    public MoveGoal(Mob creature, double x, double y, double z, float speedIn) {
        mob = (AmbientCreature) creature;
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
