package org.thesalutyt.storyverse.common.entities.camera;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.JumpGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

public class CameraEntity extends MobEntity {
    public static int tick = 0;
    protected CameraEntity(EntityType<? extends MobEntity> mob, World world) {
        super(mob, world);
    }
    @Override
    public void tick() {
        super.tick();
        tick = this.tickCount;
    }
    @Override
    public void remove() {
        super.remove();
        tick = 0;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public boolean canPickUpLoot() {
        return false;
    }
    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new JumpGoal() {
            @Override
            public boolean canUse() {
                return true;
            }
        });
    }
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FLYING_SPEED, 1)
                .add(Attributes.MOVEMENT_SPEED, 1);
    }
}
