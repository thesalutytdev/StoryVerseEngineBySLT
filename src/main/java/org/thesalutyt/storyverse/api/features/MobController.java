package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MoveGoal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MobController extends ScriptableObject implements EnvResource {
    private MobEntity entity;
    public HashMap<String, Runnable> handlers = new HashMap<>();
    public static HashMap<UUID, MobController> mobControllers = new HashMap<>();
    private boolean hasCustomUUID = false;
    private UUID customUUID = null;
    public static ArrayList<String> interactActionsID = new ArrayList<>();
    private Goal moveGoal;

    @Documentate(
            desc = "Setups MobController entity"
    )
    public MobController(){

    }
    public MobController newController(Object entity) {this.entity = (MobEntity) entity; return this;}
    public MobController newController(Double x, Double y, Double z, String type) {
        WorldWrapper worldWrapper = new WorldWrapper();
        this.entity = (MobEntity) worldWrapper.spawnEntity(new BlockPos(x, y, z), worldWrapper.toEntityType(type));
        return this.getController();
    }
    public MobController newController(BlockPos pos, String type) {
        WorldWrapper worldWrapper = new WorldWrapper();
        this.entity = (MobEntity) worldWrapper.spawnEntity(pos, worldWrapper.toEntityType(type));
        return this;
    }
    @Documentate(
            desc = "Makes mob walk to position with set speed"
    )
    public WalkTask moveTo(BlockPos pos, float speed){
        this.moveGoal = new MoveGoal(entity, pos, speed);
        this.entity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> {
            this.entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
        });
        this.entity.goalSelector.addGoal(0, this.moveGoal);
        return new WalkTask(pos, this.entity, this);
    }
    public WalkTask moveTo(Double x, Double y, Double z, Double speed){
        BlockPos pos = new BlockPos(x, y, z);
        this.moveGoal = new MoveGoal(entity, pos, speed.floatValue());
        this.entity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> {
            this.entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
        });
        this.entity.goalSelector.addGoal(0, this.moveGoal);
        return new WalkTask(pos, this.entity, this);
    }

    @Documentate(
            desc = "Stops mob from moving"
    )
    public MobController stopMove(){
        this.entity.goalSelector.removeGoal(this.moveGoal);
        return this;
    }

    @Documentate(
            desc = "Makes mob looking at entity"
    )
    public MobController lookAt(Object entity, Double pitch, Double yaw) {
        this.entity.lookAt((Entity) entity, pitch.floatValue(), yaw.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets custom name for a mob"
    )
    public MobController setName(String newName) {
        ITextComponent name = new StringTextComponent(newName);
        this.entity.setCustomName(name);
        return this;
    }
    public MobController setNameVisible(Boolean method) {
        this.entity.setCustomNameVisible(method);
        return this;
    }

    @Documentate(
            desc = "Returns mob's custom name"
    )
    public String getName() {
        return this.entity.getName().getContents();
    }

    @Documentate(
            desc = "Makes mob glow"
    )
    public MobController setGlow(Boolean method) {
        this.entity.setGlowing(method);
        return this;
    }

    @Documentate(
            desc = "Disables or enables mob's AI"
    )
    public MobController setNoAI(Boolean method) {
        this.entity.setNoAi(method);
        return this;
    }

    @Documentate(
            desc = "Kills a mob"
    )
    public MobController kill() {
        this.entity.kill();
        return this;
    }

    @Documentate(
            desc = "!! NOT WORKING !!"
    )
    public MobController pickItem(Double count) {
        if (this.entity.canPickUpLoot()) {
            this.entity.pick(count, 1f, true);
            return this;
        }
        else {
            this.entity.setCanPickUpLoot(true);
            this.entity.pick(count, 1f, true);
            return this;
        }
    }

    @Documentate(
            desc = "Returns mob's UUID"
    )
    public UUID getUUID() {
        return this.entity.getUUID();
    }

    @Documentate(
            desc = "Sets mob non-killable"
    )
    public MobController setInvulnerable(Boolean method) {
        this.entity.setInvulnerable(method);
        return this;
    }

    @Documentate(
            desc = "Returns mob's head rotation"
    )
    public double[] getHeadRotation() {
        return new double[] {this.entity.xRot, this.entity.yHeadRot};
    }

    @Documentate(
            desc = "Returns mob's Y head rotation"
    )
    public double getYHeadRotation() {
        return this.entity.yHeadRot;
    }

    @Documentate(
            desc = "Returns mob's head rotation"
    )
    public double[] getRotation() {
        return new double[] {this.entity.xRot, this.entity.yRot};
    }

    @Documentate(
            desc = "Returns mob's X rotation"
    )
    public double getXRotation() {
        return this.entity.xRot;
    }

    @Documentate(
            desc = "Returns mob's Y rotation"
    )
    public double getYRotation() {
        return this.entity.yRot;
    }

    @Documentate(
            desc = "Sets mob's rotation"
    )
    public MobController setRotation(Double x, Double y) {
        this.entity.xRot = x.floatValue();
        this.entity.yRot = y.floatValue();
        return this;
    }

    @Documentate(
            desc = "Sets mob's X rotation"
    )
    public MobController setXRotation(Double x) {
        this.entity.xRot = x.floatValue();
        return this;
    }

    @Documentate(
            desc = "Sets mob's Y rotation"
    )
    public MobController setYRotation(Double y) {
        this.entity.yRot = y.floatValue();
        return this;
    }

    @Documentate(
            desc = "Gives mob item in hand"
    )
    public MobController setItemMainHand(Object item) {
        this.entity.setItemInHand(Hand.MAIN_HAND, new ItemStack((Item) item));
        return this;
    }
    public MobController setItemOffHand(Object item) {
        this.entity.setItemInHand(Hand.OFF_HAND, new ItemStack((Item) item));
        return this;
    }
    public MobController holdItem(Object hand, Object item) {
        this.entity.setItemInHand((Hand) hand, new ItemStack((Item) item));
        return this;
    }
    @Documentate(
            desc = "Removes mob"
    )
    public MobController remove() {
        this.entity.remove();
        return this;
    }

    public MobController remove(Boolean keepData) {
        this.entity.remove(keepData);
        return this;
    }

    @Documentate(
            desc = "Adds effect to mob"
    )
    public MobController addEffect(Effect effect, Integer time, Integer level) {
        WorldWrapper worldWrapper = new WorldWrapper();
        this.entity.addEffect(new EffectInstance(effect, level, time));
        return this;
    }
    public MobController addEffect(Integer effect, Integer time, Integer level) {
        WorldWrapper worldWrapper = new WorldWrapper();
        this.entity.addEffect(new EffectInstance(worldWrapper.toEffect(effect), level, time));
        return this;
    }

    @Documentate(
            desc = "Hurts mob"
    )
    public MobController hurt(String damageSource, Double amount) {
        this.entity.hurt(new DamageSource(damageSource), amount.floatValue());
        return this;
    }

    public MobController hurt(Double amount) {
        this.entity.hurt(new DamageSource("storyverse.damage.script"), amount.floatValue());
        return this;
    }

    @Documentate(
            desc = "Disable or enable physics for a mob"
    )
    public MobController setPhysics(Boolean method) {
        this.entity.setNoGravity(method);
        return this;
    }

    @Documentate(
            desc = "Sets mob's speed"
    )
    public MobController setSpeed(Double speed) {
        this.entity.setSpeed(speed.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob's X position"
    )
    public MobController setX(Double x) {
        this.entity.setXxa(x.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob's Y position"
    )
    public MobController setY(Double y) {
        this.entity.setYya(y.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob's Z position"
    )
    public MobController setZ(Double z) {
        this.entity.setZza(z.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob's name visible"
    )
    public MobController shouldShowName(Boolean method) {
        this.entity.setCustomNameVisible(method);
        return this;
    }

    @Documentate(
            desc = "Swings mob's hand"
    )
    public MobController swing(Hand hand) {
        this.entity.swing(hand);
        return this;
    }
    public MobController swing(Object hand) {
        this.entity.swing((Hand) hand);
        return this;
    }

    @Documentate(
            desc = "Sets mob's head rotation"
    )
    public MobController setHeadRotation(Double[] pos) {
        this.setHeadRotation(pos[0], pos[1]);
        return this;
    }

    public MobController setHeadRotation(Double x, Double y) {
        this.entity.xRot = x.floatValue();
        this.entity.yRot = y.floatValue();
        return this;
    }

    @Documentate(
            desc = "Sets mob's body rotation"
    )
    public MobController setBodyRot(Double y) {
        this.entity.yBodyRot = y.floatValue();
        return this;
    }

    @Documentate(
            desc = "Sets mob's target"
    )
    public MobController setTarget(LivingEntity target) {
        this.entity.setTarget(target);
        return this;
    }
    public MobController setTarget(Object target) {
        this.entity.setTarget((LivingEntity) target);
        return this;
    }
    public void setPlayerAggressive(Boolean method) {
        if (method) {
            this.entity.setTarget((LivingEntity) Server.getPlayer().getEntity());
        } else {
            this.entity.setTarget((LivingEntity) LivingEntity.NULL);
        }
    }
    @Documentate(
            desc = "Sets mob focus on entity"
    )
    public MobController setEntityFocused(Entity target, Double pitch, Double yaw) {
        this.entity.lookAt(target, pitch.floatValue(), yaw.floatValue());
        return this;
    }
    public MobController setEntityFocused(Entity target, Vector3d vector3d) {
        this.entity.lookAt(target, 1, 1);
        return this;
    }
    public MobController setEntityFocused(Object target, Double pitch, Double yaw) {
        this.entity.lookAt((Entity) target, pitch.floatValue(), yaw.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob focus on player"
    )
    public MobController lookPlayer(PlayerEntity player, float pitch, float yaw) {
        this.entity.lookAt(player, pitch, yaw);
        return this;
    }
    public MobController lookPlayer(Object player, Double pitch, Double yaw) {
        this.entity.lookAt((PlayerEntity) player, pitch.floatValue(), yaw.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob's health value"
    )
    public MobController setHealth(Double health) {
        this.entity.setHealth(health.floatValue());
        return this;
    }

    @Documentate(
            desc = "Toggles mob's ability to pickup loot"
    )
    public MobController canPickUpLoot(Boolean method) {
        this.entity.setCanPickUpLoot(method);
        return this;
    }

    @Documentate(
            desc = "Returns mob's position"
    )
    public BlockPos getPosition() {
        return this.entity.getEntity().blockPosition();
    }

    @Documentate(
            desc = "Toggles mob's aggressive setting"
    )
    public MobController setAggressive(Boolean method) {
        this.entity.setAggressive(method);
        return this;
    }

    @Documentate(
            desc = "Sets mob invisible"
    )
    public MobController setInvisible(Boolean method) {
        this.entity.setInvisible(method);
        return this;
    }

    @Documentate(
            desc = "Sends message to a player from a mob"
    )
    public MobController send(String text) {
        try {
            Chat.sendNamed(Objects.requireNonNull(this.entity.getCustomName()).getContents(), text);
        } catch (NullPointerException ex) {
            Chat.sendNamed(String.format("%s", SVEngine.DEFAULT_CHARACTER_NAME), text);
        }
        return this;
    }

    @Documentate(
            desc = "Returns mob's entity"
    )
    public Entity getEntity() {
        return this.entity.getEntity();
    }

    @Documentate(
            desc = "Sets mob's UUID"
    )
    public MobController setUUID(UUID uuid) {
        this.entity.setUUID(uuid);
        this.hasCustomUUID = true;
        this.customUUID = uuid;
        return this;
    }

    @Documentate(
            desc = "Returns controller"
    )
    public MobController getController() {
        return this;
    }

    public MobEntity getMobEntity() {
        return (MobEntity) this.entity.getEntity();
    }

    @Documentate(
            desc = "Register mob's controller for use in interaction event construction"
    )
    public static void registerController(MobController controller) {
        mobControllers.put(controller.getUUID(), controller);
    }

    public static void putIntoScope (Scriptable scope) {
        MobController ef = new MobController();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method create = MobController.class.getMethod("newController", Object.class);
            methodsToAdd.add(create);
            Method newC = MobController.class.getMethod("newController", Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(newC);
            Method moveTo = MobController.class.getMethod("moveTo", Double.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(moveTo);
            Method lookAtPlayer = MobController.class.getMethod("lookPlayer", Object.class, Double.class, Double.class);
            methodsToAdd.add(lookAtPlayer);
            Method setPlAggressive = MobController.class.getMethod("setPlayerAggressive", Boolean.class);
            methodsToAdd.add(setPlAggressive);
            Method send = MobController.class.getMethod("send", String.class);
            methodsToAdd.add(send);
            Method stopMove = MobController.class.getMethod("stopMove");
            methodsToAdd.add(stopMove);
            Method setHeadRot = MobController.class.getMethod("setHeadRotation", Double.class, Double.class);
            methodsToAdd.add(setHeadRot);
            Method setEntityFocused = MobController.class.getMethod("setEntityFocused", Object.class, Double.class, Double.class);
            methodsToAdd.add(setEntityFocused);
            Method giveItem = MobController.class.getMethod("holdItem", Object.class, Object.class);
            methodsToAdd.add(giveItem);
            Method setName = MobController.class.getMethod("setName", String.class);
            methodsToAdd.add(setName);
            Method setNameVisible = MobController.class.getMethod("setNameVisible", Boolean.class);
            methodsToAdd.add(setNameVisible);
            Method getMobEntity = MobController.class.getMethod("getMobEntity");
            methodsToAdd.add(getMobEntity);
            Method setHealth = MobController.class.getMethod("setHealth", Double.class);
            methodsToAdd.add(setHealth);
            Method getPos = MobController.class.getMethod("getPosition");
            methodsToAdd.add(getPos);
            Method remove = MobController.class.getMethod("remove");
            methodsToAdd.add(remove);
            Method kill = MobController.class.getMethod("kill");
            methodsToAdd.add(kill);
            Method pickItem = MobController.class.getMethod("pickItem", Double.class);
            methodsToAdd.add(pickItem);
            Method swing = MobController.class.getMethod("swing", Object.class);
            methodsToAdd.add(swing);
            Method bodyRot = MobController.class.getMethod("setBodyRot", Double.class);
            methodsToAdd.add(bodyRot);
            Method setX = MobController.class.getMethod("setX", Double.class);
            methodsToAdd.add(setX);
            Method setY = MobController.class.getMethod("setY", Double.class);
            methodsToAdd.add(setY);
            Method setZ = MobController.class.getMethod("setZ", Double.class);
            methodsToAdd.add(setZ);
            Method setSpeed = MobController.class.getMethod("setSpeed", Double.class);
            methodsToAdd.add(setSpeed);
            Method setPhysics = MobController.class.getMethod("setPhysics", Boolean.class);
            methodsToAdd.add(setPhysics);
            Method setNoAI = MobController.class.getMethod("setNoAI", Boolean.class);
            methodsToAdd.add(setNoAI);
            Method setItemMainH = MobController.class.getMethod("setItemMainHand", Object.class);
            methodsToAdd.add(setItemMainH);
            Method setItemOffH = MobController.class.getMethod("setItemOffHand", Object.class);
            methodsToAdd.add(setItemOffH);
            Method setXRot = MobController.class.getMethod("setXRotation", Double.class);
            methodsToAdd.add(setXRot);
            Method setInvulnerable = MobController.class.getMethod("setInvulnerable", Boolean.class);
            methodsToAdd.add(setInvulnerable);
            Method setYRot = MobController.class.getMethod("setYRotation", Double.class);
            methodsToAdd.add(setYRot);
            Method getRot = MobController.class.getMethod("getRotation");
            methodsToAdd.add(getRot);
            Method getYHeadRot = MobController.class.getMethod("getYHeadRotation");
            methodsToAdd.add(getYHeadRot);
            Method getHeadRot = MobController.class.getMethod("getHeadRotation");
            methodsToAdd.add(getHeadRot);
            Method getYRot = MobController.class.getMethod("getYRotation");
            methodsToAdd.add(getYRot);
            Method setRot = MobController.class.getMethod("setRotation", Double.class, Double.class);
            methodsToAdd.add(setRot);
            Method setGlow = MobController.class.getMethod("setGlow", Boolean.class);
            methodsToAdd.add(setGlow);
            Method hurt = MobController.class.getMethod("hurt", Double.class);
            methodsToAdd.add(hurt);
            Method hurtWithDamageType = MobController.class.getMethod("hurt", String.class, Double.class);
            methodsToAdd.add(hurtWithDamageType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("entity", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "MobController";
    }

    @Override
    public String getClassName() {
        return "MobController";
    }
}
