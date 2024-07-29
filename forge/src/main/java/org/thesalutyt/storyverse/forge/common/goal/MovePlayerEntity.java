package org.thesalutyt.storyverse.forge.common.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ambient.AmbientCreature;

import java.util.HashMap;
import java.util.Set;

public class MovePlayerEntity extends Goal {
    public static HashMap<Mob, MovePlayerEntity> movePlayerEntities = new HashMap<>();
    public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;
    protected final AmbientCreature mob;
    protected double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;

    public MovePlayerEntity(Mob creature, BlockPos vector3d, double speedIn) {
        mob = (AmbientCreature) creature;
        speedModifier = speedIn;
        posX = vector3d.getX();
        posY = vector3d.getY();
        posZ = vector3d.getZ();
    }

    public MovePlayerEntity(Mob creature, double x, double y, double z, float speedIn) {
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
    }

    public void stop() {

    }
}
