package org.thesalutyt.storyverse.common.entities.adder.essential;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.common.entities.adder.CustomEntityArgsHandler;
import org.thesalutyt.storyverse.common.entities.adder.essential.arguments.EntityArgumentList;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MoveGoal;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CustomMobEntity extends MobEntity implements IAnimationTickable, IAnimatable {
    public String id;
    public boolean isInvulnerable = false;
    public boolean isInvisible = false;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(100, ItemStack.EMPTY);
    public Entity focusedEntity;
    private int ticks = 0;
    public Boolean canPickup = true;
    public Boolean isAttackable = true;
    private EntityArgumentList arguments;
    private static final DataParameter<Boolean> SLEEP =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.BOOLEAN);

    private static final DataParameter<String> ANIMATION =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> LOOP_ANIM =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> ONCE_ANIM =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> IDLE_ANIM =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> WALK_ANIM =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> EMOTION =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> TEXTURE =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> MODEL =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> ANIMATION_FILE =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> MOVE =
            EntityDataManager.defineId(CustomMobEntity.class, DataSerializers.BOOLEAN);


    public CustomMobEntity(EntityType<? extends MobEntity> entity, World level) {
        super(entity, level);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setArguments(EntityArgumentList arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean isAttackable() {
        return isAttackable;
    }

    private <E extends IAnimatable> PlayState emote(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder()
                .addAnimation(this.getEmote()));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            if (!this.getWalkAnim().equals("")) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(this.getWalkAnim()));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.npc.walk"));
            }
            return PlayState.CONTINUE;

        }

        if (!this.getAnimation().equals("")) {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation(this.getAnimation()));
            return PlayState.CONTINUE;
        }

        if (!this.getIdleAnim().equals("")) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.getIdleAnim()));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.npc.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        data.addAnimationController(new AnimationController(this, "controllerEmote",
                0, this::emote));
        data.addAnimationController(new AnimationController<>(this,"c_playonce",15,this::playOnceC));
        data.addAnimationController(new AnimationController<>(this,"c_loop",15,this::loopC));
    }

    @Override
    public void kill() {
        super.kill();
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction i : arguments.onDeath) {
                i.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(),
                        new Object[]{new MobController(this), ticks});
            }
        });
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        if (hand != Hand.MAIN_HAND || !this.level.isClientSide) return ActionResultType.PASS;

        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction i : arguments.onInteract) {
                i.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(),
                        new Object[]{new MobController(this), ticks});
            }
        });
        return super.interactAt(player,vec,hand);
    }

    @Override
    public void tick() {
        super.tick();
        this.arguments = CustomEntityArgsHandler.getArgs(this.id);

        this.setTexturePath(arguments.skin);
        this.setModelPath(arguments.model);
        this.setAnimationPath(arguments.animation);

        setTexturePath(getTexturePath());
        setModelPath(getModelPath());
        setAnimationPath(getAnimationPath());
        if (focusedEntity != null) {
            lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(focusedEntity.getX(), focusedEntity.getEyeY(), focusedEntity.getZ()));
        }
        ticks++;
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction i : arguments.onTick) {
                i.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(),
                        new Object[]{new MobController(this), ticks});
            }
        });
    }

    public  <E extends IAnimatable> PlayState playOnceC(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15;
        String anim = getPersistentData().getString("a_playonce");
        if(anim == "")
            return PlayState.STOP;
        AnimationBuilder def = new AnimationBuilder().playOnce(anim);
        event.getController().setAnimation(def);
        return PlayState.CONTINUE;
    }

    public <E extends IAnimatable> PlayState loopC(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15;
        String anim = getPersistentData().getString("a_loop");
        if(anim == "")
            return PlayState.STOP;
        AnimationBuilder def = new AnimationBuilder().loop(anim);
        event.getController().setAnimation(def);
        return PlayState.CONTINUE;
    }

    public void setTexturePath(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public String getTexturePath() {
        return this.entityData.get(TEXTURE);
    }

    public String getLoopAnim() {
        return this.entityData.get(LOOP_ANIM);
    }

    public String getOnceAnim() {
        return this.entityData.get(ONCE_ANIM);
    }

    public String getModelPath() {
        return this.entityData.get(MODEL);
    }

    public String getAnimationPath() {
        return this.entityData.get(ANIMATION_FILE);
    }
    public void setModelPath(String model) {
        this.entityData.set(MODEL, model);
    }

    public void setLoopAnim(String anim) {
        this.entityData.set(LOOP_ANIM, anim);
    }

    public void setOnceAnim(String anim) {
        this.entityData.set(ONCE_ANIM, anim);
    }

    public void setAnimationPath(String animationPath) {
        this.entityData.set(ANIMATION_FILE, animationPath);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "");
        this.entityData.define(EMOTION, "animation.npc.idle");
        this.entityData.define(TEXTURE, "");
        this.entityData.define(MODEL, "");
        this.entityData.define(ONCE_ANIM, "");
        this.entityData.define(LOOP_ANIM, "");
        this.entityData.define(ANIMATION_FILE, "");
        this.entityData.define(IDLE_ANIM, "animation.npc.idle");
        this.entityData.define(WALK_ANIM, "animation.npc.walk");
    }

    public void setSleep(boolean sitting) {
        this.entityData.set(SLEEP, sitting);
    }


    public void setMove(boolean sitting) {
        this.entityData.set(MOVE, sitting);
    }

    public void setAnimation(String animations) {
        this.entityData.set(ANIMATION, animations);
    }

    public void setIdleAnim(String idleAnim) {
        this.entityData.set(IDLE_ANIM, idleAnim);
    }

    public void setWalkAnim(String walkAnim) {
        this.entityData.set(WALK_ANIM, walkAnim);
    }

    public String getAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public String getWalkAnim() {
        return this.entityData.get(WALK_ANIM);
    }

    public String getIdleAnim() {
        return this.entityData.get(IDLE_ANIM);
    }

    public void setEmote(String emote) {
        this.entityData.set(EMOTION, emote);
    }

    public String getEmote() {
        return this.entityData.get(EMOTION);
    }
    public boolean isMove() {
        return this.entityData.get(SLEEP);
    }

    public void moveEntity(double x, double y, double z, float speed){
        this.goalSelector.addGoal(1, new MoveGoal(this, x, y, z, speed));
    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }

    @Override
    public int tickTimer() {
        return ticks;
    }
}
