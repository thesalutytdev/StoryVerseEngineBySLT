package org.thesalutyt.storyverse.common.entities.npc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Objects;

public class NPCEntity extends MobEntity implements IAnimatable, IAnimationTickable {
    private AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);
    private final NonNullList<ItemStack> armorInventory;
    private final NonNullList<ItemStack> inventory;
    private int ticks;
    public Entity focusedEntity;
    public NPCEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        this.armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        this.inventory = NonNullList.withSize(16, ItemStack.EMPTY);
        this.ticks = 0;
    }
    private <E extends IAnimatable> PlayState predicateDef(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15.0;
        String idleAnim = String.valueOf(this.getPersistentData().get("idleAnim"));
        String walkAnim = String.valueOf(this.getPersistentData().get("walkAnim"));
        AnimationBuilder def;
        if (event.isMoving()) {
            def = (new AnimationBuilder()).loop(Objects.equals(walkAnim, "") ? "animation.npc.walk" : walkAnim);
            event.getController().setAnimation(def);
            return PlayState.CONTINUE;
        } else {
            def = (new AnimationBuilder()).loop(Objects.equals(idleAnim, "") ? "animation.npc.idle" : idleAnim);
            event.getController().setAnimation(def);
            return PlayState.CONTINUE;
        }
    }
    public static AttributeModifierMap.MutableAttribute generateAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.00)
                .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612)
                .add(Attributes.ATTACK_SPEED, 4.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.LUCK, 0.0);
    }

    private <E extends IAnimatable> PlayState playOnceC(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15.0;
        String anim = this.getPersistentData().getString("a_playonce");
        if (anim == "") {
            return PlayState.STOP;
        } else {
            AnimationBuilder def = (new AnimationBuilder()).playOnce(anim);
            event.getController().setAnimation(def);
            return PlayState.CONTINUE;
        }
    }

    private <E extends IAnimatable> PlayState loopC(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15.0;
        String anim = this.getPersistentData().getString("a_loop");
        if (anim == "") {
            return PlayState.STOP;
        } else {
            AnimationBuilder def = (new AnimationBuilder()).loop(anim);
            event.getController().setAnimation(def);
            return PlayState.CONTINUE;
        }
    }

    protected GroundPathNavigator createNavigator(World world) {
        return new GroundPathNavigator(this, world);
    }
    public HandSide getHandSide() {
        return HandSide.RIGHT;
    }
    public Iterable<ItemStack> getArmorInventory() {
        return this.armorInventory;
    }
    public Iterable<ItemStack> getInventory() {
        return this.inventory;
    }
    public void setItemInHand(EquipmentSlotType arg0, ItemStack arg1) {
        if (arg0 == EquipmentSlotType.MAINHAND) {
            this.inventory.set(0, arg1);
        }

        if (arg0 == EquipmentSlotType.OFFHAND) {
            this.inventory.set(1, arg1);
        }

    }

    public ItemStack getItemInHand(EquipmentSlotType arg0) {
        if (arg0 == EquipmentSlotType.MAINHAND) {
            return (ItemStack)this.inventory.get(0);
        } else {
            return arg0 == EquipmentSlotType.OFFHAND ? (ItemStack)this.inventory.get(1) : ItemStack.EMPTY;
        }
    }

    public void setTexturePath(String texture) {
        this.getPersistentData().putString("texturePath", texture);
    }

    public void setModelPath(String model) {
        this.getPersistentData().putString("modelPath", model);
    }

    public void setAnimationPath(String model) {
        this.getPersistentData().putString("animPath", model);
    }

    public void setIdleAnim(String anim) {
        this.getPersistentData().putString("idleAnim", anim);
    }

    public void setWalkAnim(String anim) {
        this.getPersistentData().putString("walkAnim", anim);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 15.0F, this::predicateDef));
        data.addAnimationController(new AnimationController(this, "c_playonce", 15.0F, this::playOnceC));
        data.addAnimationController(new AnimationController(this, "c_loop", 15.0F, this::loopC));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public int tickTimer() {
        return this.ticks;
    }
}
