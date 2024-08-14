package org.thesalutyt.storyverse.common.entities.npc;

import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.JumpGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.quests.Quest;
import org.thesalutyt.storyverse.api.quests.QuestManager;
import org.thesalutyt.storyverse.api.quests.goal.GoalType;
import org.thesalutyt.storyverse.api.quests.goal.NPCItem;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class NPCEntity extends AnimalEntity implements IAnimatable, IAnimationTickable, IMerchant {
    private int ticks = 0;
    public String npcId = "none";
    public String traderName = "none";
    public MerchantOffers offers = null;
    public Boolean isTrader = false;
    public Boolean canPickup = true;
    public Boolean isAttackable = true;
    public PlayerEntity tradingPlayer;
    public Double rotX;
    public Double rotY;
    public Boolean showProgressBar = true;
    public ItemStack lastItemPicked = ItemStack.EMPTY;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(100, ItemStack.EMPTY);
    private static final DataParameter<Boolean> SLEEP =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.BOOLEAN);

    private static final DataParameter<String> ANIMATION =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> LOOP_ANIM =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> ONCE_ANIM =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> IDLE_ANIM =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> WALK_ANIM =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> EMOTION =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> TEXTURE =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> MODEL =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);

    private static final DataParameter<String> ANIMATION_FILE =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> MOVE =
            EntityDataManager.defineId(NPCEntity.class, DataSerializers.BOOLEAN);

    public Entity focusedEntity;
    public final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public NPCEntity(EntityType<? extends AnimalEntity> p_i48568_1_, World p_i48568_2_) {
        super(p_i48568_1_, p_i48568_2_);
    }

    @Override
    public void tick() {
        super.tick();
        setTexturePath(getTexturePath());
        setModelPath(getModelPath());
        setAnimationPath(getAnimationPath());
        if (focusedEntity != null) {
            lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(focusedEntity.getX(), focusedEntity.getEyeY(), focusedEntity.getZ()));
        }
        if (rotX != null || rotY != null) {
            assert rotX != null;
            assert rotY != null;
//            xRot = rotX.floatValue();
//            xRotO = rotX.floatValue();
//            yHeadRot = rotY.floatValue();
//            yBodyRot = rotY.floatValue();
//            yRot = rotY.floatValue();
//            yRotO = rotY.floatValue();
//            yHeadRotO = rotY.floatValue();
//            yBodyRotO = rotY.floatValue();
        }
        ticks++;
    }

    @Override
    public boolean isAttackable() {
        return isAttackable;
    }

    private void applyOpenDoorsAbility() {
        if (GroundPathHelper.hasGroundPathNavigation(this)) {
            ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        }

    }

    @Override
    protected void registerGoals() {
        // this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
        // this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1));
        this.goalSelector.addGoal(4, new JumpGoal() {
            @Override
            public boolean canUse() {
                return true;
            }
        });
        this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(6, new SwimGoal(this));
        applyOpenDoorsAbility();
        super.registerGoals();
    }

    public static AttributeModifierMap setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.4f).build();
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

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        setAnimation(tag.getString("animation"));
        setAnimation(tag.getString("emote"));
        setIdleAnim(tag.getString("idleAnim"));
        setWalkAnim(tag.getString("walkAnim"));
        setLoopAnim(tag.getString("a_loop"));
        setOnceAnim(tag.getString("a_playonce"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        tag.putString("animation", this.getAnimation());
        tag.putString("emote", this.getEmote());
        tag.putString("walkAnim", this.getWalkAnim());
        tag.putString("idleAnim", this.getIdleAnim());
        tag.putString("a_playonce", this.getOnceAnim());
        tag.putString("a_loop", this.getLoopAnim());
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.PLAYER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
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
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

    public boolean changeDimension() {
        return true;
    }

    @Nullable
    @Override
    public NPCEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int tickTimer() {
        return 0;
    }

    @Override
    public void setDropChance(EquipmentSlotType slotType, float chance) {
        switch(slotType.getType()) {
            case HAND:
                Arrays.fill(this.armorDropChances, chance);
                break;
            case ARMOR:
                Arrays.fill(this.armorDropChances, 1000);
                break;
            default:
                break;
        }

    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        return super.interactAt(player,vec,hand);
    }

    @Override
    protected void pickUpItem(ItemEntity p_175445_1_) {
        super.pickUpItem(p_175445_1_);
    }

    @Override
    public boolean canPickUpLoot() {
        return canPickup;
    }

    @Override
    public void onItemPickup(ItemEntity item) {
        try {
            this.lastItemPicked = item.getItem();
            MobJS.runEvent(MobJS.getMob(this.getUUID()), "on-pickup");
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onItemPickup(item);
    }

    @Override
    public boolean wantsToPickUp(ItemStack item) {
        try {
            assert Minecraft.getInstance().player != null;
            PlayerEntity player = Minecraft.getInstance().player;
            System.out.println("Player: " + player.getDisplayName().getContents() + " " + player.getGameProfile().getName());
            Quest quest = QuestManager.quests.get(player);
            System.out.println("Quest: " + quest + " " + quest.entityAdder + " " + quest.goal + " " + quest.id);
            assert quest.entityAdder != null;
            if (!(quest.entityAdder instanceof NPCEntity)) {
                return false;
            }
            if (quest.goal == GoalType.NPC_ITEM) {
                if (item.getItem().equals(NPCItem.items.get(this).stack)) {
                    System.out.println("Picked up item: " + item.getItem());
                    return true;
                }
            }
            return super.wantsToPickUp(item);
        } catch (Exception e) {
            e.printStackTrace();
            return super.wantsToPickUp(item);
        }
    }

    public ActResult useItem(Hand hand) {
        try {
            switch (hand) {
                case MAIN_HAND:
                    this.getMainHandItem().getItem().onUseTick(this.getEntity().level, this, this.getMainHandItem(),
                            1);
                    break;
                case OFF_HAND:
                    this.getOffhandItem().getItem().onUseTick(this.getEntity().level, this, this.getOffhandItem(),
                            1);
                    break;
                default:
                    return ActResult.NULL_POINTER_EXCEPTION;
            }
            return ActResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ActResult.FAILED;
        }
    }

    public ItemStack getLastItemPicked() {
        return lastItemPicked;
    }

    public void hold(Hand hand, ItemStack item) {
        setItemInHand(hand, item);
        NPCRender.renders.get(0).hold(hand, item);
    }

    public void armor(Integer slot, ItemStack item) {
        AtomicInteger cur = new AtomicInteger();
        getArmorSlots().forEach(sl_ -> {
            if (cur.get() == slot) {
                sl_ = item;
            }
            cur.getAndIncrement();
        });
        NPCRender.renders.get(0).armor(slot, item);
    }

    public void jump() {
        this.jumpControl.jump();
    }

    @Override
    public void setTradingPlayer(@Nullable PlayerEntity player) {
        this.tradingPlayer = player;
    }

    @Nullable
    @Override
    public PlayerEntity getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        return offers;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers offers) {
        this.offers = offers;
    }

    @Override
    public void notifyTrade(MerchantOffer offers) {

    }

    @Override
    public void notifyTradeUpdated(ItemStack p_110297_1_) {

    }

    @Override
    public World getLevel() {
        return level;
    }

    @Override
    public int getVillagerXp() {
        return this.offers.get(0).getXp();
    }

    @Override
    public void overrideXp(int p_213702_1_) {

    }

    @Override
    public boolean showProgressBar() {
        return showProgressBar;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_TRADE;
    }
}
