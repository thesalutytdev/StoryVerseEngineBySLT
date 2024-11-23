package org.thesalutyt.storyverse.common.entities.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.quests.Quest;
import org.thesalutyt.storyverse.api.quests.QuestManager;
import org.thesalutyt.storyverse.api.quests.goal.GoalType;
import org.thesalutyt.storyverse.api.quests.goal.NPCItem;
import org.thesalutyt.storyverse.common.entities.Entities;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NPCEntity extends MobEntity implements IAnimatable, IAnimationTickable, IMerchant {
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

    public NPCEntity(EntityType<? extends MobEntity> p_i48568_1_, World p_i48568_2_) {
        super(p_i48568_1_, p_i48568_2_);
    }

    public static NPCEntity createNew(World world, String json) {
        NPCEntity npc = new NPCEntity(Entities.NPC.get(), world);

        npc.restoreFromJson(json);

        return npc;
    }

    public void setId(String id) {
        this.npcId = id;
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
        if (!this.getEmote().equals("")) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(this.getEmote()));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder()
                .addAnimation("animation.npc.idle"));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.npc.walking"));
            return PlayState.CONTINUE;

        }

        if (!this.getAnimation().equals("")) {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation(this.getAnimation()));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.npc.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        data.addAnimationController(new AnimationController(this, "controllerEmote",
                0, this::emote));
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        setAnimation(tag.getString("animation"));
        setAnimation(tag.getString("emote"));
        setIdleAnim(tag.getString("idleAnim"));
        setWalkAnim(tag.getString("walkAnim"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        tag.putString("animation", this.getAnimation());
        tag.putString("emote", this.getEmote());
        tag.putString("walkAnim", this.getWalkAnim());
        tag.putString("idleAnim", this.getIdleAnim());
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
        this.getPersistentData().putString("texturePath", texture);
    }

    public String getTexturePath() {
        return this.getPersistentData().getString("texturePath");
    }

    public String getModelPath() {
        return this.getPersistentData().getString("modelPath");
    }

    public String getAnimationPath() {
        return this.getPersistentData().getString("animationPath");
    }
    public void setModelPath(String model) {
        this.entityData.set(MODEL, model);
        this.getPersistentData().putString("modelPath", model);
    }

    public void setAnimationPath(String animationPath) {
        this.entityData.set(ANIMATION_FILE, animationPath);
        this.getPersistentData().putString("animationPath", animationPath);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "animation.npc.idle");
        this.entityData.define(EMOTION, "animation.npc.idle");
        this.entityData.define(TEXTURE, "");
        this.entityData.define(MODEL, "");
        this.entityData.define(ANIMATION_FILE, "");
        this.entityData.define(IDLE_ANIM, "animation.npc.idle");
        this.entityData.define(WALK_ANIM, "animation.npc.walk");
        this.setEmote("animation.npc.idle");
        this.setWalkAnim("animation.npc.walk");
        this.setIdleAnim("animation.npc.idle");
    }

    public void setSleep(boolean sitting) {
        this.entityData.set(SLEEP, sitting);
        this.getPersistentData().putBoolean("sleep", sitting);
    }

    public void setMove(boolean sitting) {
        this.entityData.set(MOVE, sitting);
        this.getPersistentData().putBoolean("move", sitting);
    }

    public void setAnimation(String animations) {
        this.entityData.set(ANIMATION, animations);
        this.getPersistentData().putString("animation", animations);
    }

    public void setIdleAnim(String idleAnim) {
        this.entityData.set(IDLE_ANIM, idleAnim);
        this.getPersistentData().putString("idleAnim", idleAnim);
    }

    public void setWalkAnim(String walkAnim) {
        this.entityData.set(WALK_ANIM, walkAnim);
        this.getPersistentData().putString("walkAnim", walkAnim);
    }

    public String getAnimation() {
        return this.getPersistentData().getString("animation");
    }

    public String getWalkAnim() {
        return this.getPersistentData().getString("walkAnim");
    }

    public String getIdleAnim() {
        return this.getPersistentData().getString("idleAnim");
    }

    public void setEmote(String emote) {
        this.entityData.set(EMOTION, emote);
        this.getPersistentData().putString("emote", emote);
    }

    public String getEmote() {
        return this.getPersistentData().getString("emote");
    }
    public boolean isMove() {
        return this.getPersistentData().getBoolean("move");
    }

    public void moveEntity(double x, double y, double z, float speed){
        this.getNavigation().moveTo(x, y, z, speed);
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
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

    public void setPosition(double x, double y, double z) {
        this.setPos(x, y, z);
    }

    public void restoreName(String name) {
        this.setCustomName(new StringTextComponent(name));
    }

    public void restoreNameVisible(boolean nameVisible) {
        this.setCustomNameVisible(nameVisible);
    }

    public void restoreTexture(String texture) {
        this.setTexturePath(texture);
    }

    public void restoreModel(String model) {
        this.setModelPath(model);
    }

    public void restoreAnimations(String animations) {
        this.setAnimationPath(animations);
    }

    public void restoreIdleAnimation(String idleAnimation) {
        this.setIdleAnim(idleAnimation);
    }

    public void restoreWalkAnimation(String walkAnimation) {
        this.setWalkAnim(walkAnimation);
    }

    public void restoreOffers(MerchantOffers offers) {
        this.overrideOffers(offers);
    }

    public void restoreCanPickup(boolean canPickup) {
        this.canPickup = canPickup;
    }

    public void restoreShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    public void restoreIsTrader(boolean isTrader) {
        this.isTrader = isTrader;
    }

    public void restoreIsAttackable(boolean isAttackable) {
        this.isAttackable = isAttackable;
    }

    public void restoreTraderName(String traderName) {
        this.traderName = traderName;
    }

    public void restoreNpcId(String npcId) {
        this.npcId = npcId;
    }

    public void restoreUUID(UUID uuid) {
        this.setUUID(uuid);
    }

    public String getNpcId() {
        return npcId;
    }


    public String dumpToJson() {
        String json = "{\"id\": \"" + getNpcId() + "\"," +
                "\"uuid\": \"" + getUUID() + "\"," +
                "\"texture\": \""
                + getTexturePath() + "\", \"model\": \""
                + getModelPath() + "\", \"animationsPath\": \""
                + getAnimationPath() + "\"," +
                "\"name\": \"" + getName().getContents() + "\"," +
                "\"position\": [" + getX() + ", " + getY() + ", " + getZ() + "]," +
                "\"idleAnimation\": \"" + getIdleAnim() + "\"," +
                "\"walkAnimation\": \"" + getWalkAnim() + "\"," +
                "\"isAttackable\": " + isAttackable + "," +
                "\"showProgressBar\": " + showProgressBar + "," +
                "\"canPickup\": " + canPickup + "," +
                "\"nameVisible\": " + isCustomNameVisible() + "}";
        return json;
    }

    public void restoreFromJson(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        setPosition(jsonObject.get("position").getAsJsonArray().get(0).getAsDouble(),
                jsonObject.get("position").getAsJsonArray().get(1).getAsDouble(),
                jsonObject.get("position").getAsJsonArray().get(2).getAsDouble());
        restoreNpcId(jsonObject.get("id").getAsString());
        restoreTexture(jsonObject.get("texture").getAsString());
        restoreModel(jsonObject.get("model").getAsString());
        restoreAnimations(jsonObject.get("animationsPath").getAsString());
        restoreName(jsonObject.get("name").getAsString());
        restoreIdleAnimation(jsonObject.get("idleAnimation").getAsString());
        restoreWalkAnimation(jsonObject.get("walkAnimation").getAsString());
//        restoreTraderName(jsonObject.get("traderName").getAsString());
//        restoreIsTrader(jsonObject.get("isTrader").getAsBoolean());
        restoreIsAttackable(jsonObject.get("isAttackable").getAsBoolean());
        restoreShowProgressBar(jsonObject.get("showProgressBar").getAsBoolean());
        restoreCanPickup(jsonObject.get("canPickup").getAsBoolean());
        restoreUUID(UUID.fromString(jsonObject.get("uuid").getAsString()));
        restoreNameVisible(jsonObject.get("nameVisible").getAsBoolean());

//        JsonObject trades = jsonObject.get("trades").getAsJsonArray().get(0).getAsJsonObject();
//        MerchantOffers offers = new MerchantOffers();
//
//        for(JsonElement trade : trades.getAsJsonArray()) {
//            MerchantOffer offer;
//            CompoundNBT nbt = new CompoundNBT();
//            nbt.putString("trade", trade.getAsString());
//
//            offer = new MerchantOffer(nbt);
//            offers.add(offer);
//        }

        restoreOffers(offers);
    }
}
