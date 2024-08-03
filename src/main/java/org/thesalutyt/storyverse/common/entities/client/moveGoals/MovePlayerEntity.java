package org.thesalutyt.storyverse.common.entities.client.moveGoals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.HashMap;
import java.util.Set;

public class MovePlayerEntity extends Goal {
    public static HashMap<MobEntity, MovePlayerEntity> movePlayerEntities = new HashMap<>();
    public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;
    protected final CreatureEntity mob;
    protected double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;

    public MovePlayerEntity(MobEntity creature, BlockPos vector3d, double speedIn) {
        mob = (CreatureEntity) creature;
        speedModifier = speedIn;
        posX = vector3d.getX();
        posY = vector3d.getY();
        posZ = vector3d.getZ();
    }

    public MovePlayerEntity(MobEntity creature, double x, double y, double z, float speedIn) {
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
    }

    public void stop() {

    }

    public boolean canContinueToUse() {
        if ( mob.getX() == posX &&  mob.getY() == posY && mob.getY() == posZ) {
            Set<PrioritizedGoal> goal = ObfuscationReflectionHelper.getPrivateValue(GoalSelector.class, mob.goalSelector, "goals");
            for (PrioritizedGoal availableGoal : goal) {
                if (availableGoal.getGoal().toString().equals("MovePlayerEntity")) {
                    mob.goalSelector.removeGoal(availableGoal.getGoal());
                }
            }
        }
        return !this.mob.getNavigation().isDone();
    }
}
