package org.thesalutyt.storyverse.api.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.wrappers.EntityData;
import org.thesalutyt.storyverse.api.environment.resource.wrappers.NPCData;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MoveGoal;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MovePlayerEntity;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MobController extends ScriptableObject implements EnvResource {
    private MobEntity entity;
    private WorldWrapper worldWrapper;
    public HashMap<String, Runnable> handlers = new HashMap<>();
    public static HashMap<UUID, MobController> mobControllers = new HashMap<>();
    private Float mobSpeed;
    public static ArrayList<String> interactActionsID = new ArrayList<>();
    private Goal moveGoal;
    public NPCData npcData;
    public EntityData data;

    @Documentate(
            desc = "Setups MobController entity"
    )
    public MobController(Double x, Double y, Double z, String type){
        worldWrapper = new WorldWrapper();
        registerFunctions();
        BlockPos pos = WorldWrapper.pos(x, y, z);
        this.entity = (MobEntity) worldWrapper.spawnEntity(pos, WorldWrapper.toEntityType(type));
        mobSpeed = this.entity.getSpeed();
    }
    public MobController(Double x, Double y, Double z, EntityType type){
        worldWrapper = new WorldWrapper();
        registerFunctions();
        BlockPos pos = WorldWrapper.pos(x, y, z);
        this.entity = (MobEntity) worldWrapper.spawnEntity(pos, type);
        mobSpeed = this.entity.getSpeed();
    }
    public MobController(BlockPos pos, EntityType type){
        worldWrapper = new WorldWrapper();
        System.out.println("Registering functions");
        registerFunctions();
        System.out.println("Creating entity");
        this.entity = (MobEntity) worldWrapper.spawnEntity(pos, type);
        System.out.println("Setting mob's speed");
        mobSpeed = this.entity.getSpeed();
    }
    public MobController(MobEntity entity) {
        worldWrapper = new WorldWrapper();
        registerFunctions();
        this.entity = entity;
        mobSpeed = this.entity.getSpeed();
    }

    public MobController(Entity entity) {
        worldWrapper = new WorldWrapper();
        registerFunctions();
        this.entity = (MobEntity) entity;
        mobSpeed = this.entity.getSpeed();
    }

    public MobController(LivingEntity entity) {
        worldWrapper = new WorldWrapper();
        registerFunctions();
        this.entity = (MobEntity) entity;
        mobSpeed = this.entity.getSpeed();
    }

    protected MobController() {
        registerFunctions();
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
    public Object[] moveTo(Double x, Double y, Double z, Double speed) {
        if (this.entity.getType() == Entities.NPC.get()) {
            NPCEntity npc = (NPCEntity) this.entity;
            npc.moveEntity(x, y, z, speed.floatValue());
            return new Object[] {"none", "none"};
        }
        BlockPos pos = new BlockPos(x, y, z);
        this.moveGoal = new MoveGoal(entity, pos, speed.floatValue());
        this.entity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> {
            this.entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
        });
        this.entity.goalSelector.addGoal(1, this.moveGoal);
        WalkTask task = new WalkTask(pos, this.entity, this);
        return new Object[] {task, task.getTaskStringID()};
    }


    @Documentate(
            desc = "DO NOT ADD FUNCTIONS LOWER TO JS"
    )

    public MobController defineData(EntityData data) {
        this.data = data;
        return this;
    }

    public MobController defineNPCData(NPCData data) {
        if (this.entity.getType() != Entities.NPC.get()) {
            throw new RuntimeException("Entity is not NPC!");
        }
        this.npcData = data;
        return this;
    }

    public MobController moveToPlayer(String playerName) {
        this.moveTo(Server.getPlayerByName(playerName).blockPosition(), this.entity.getSpeed());
        return this;
    }

    public MobController moveToPlayer() {
        this.moveTo(Server.getFirstPlayer().blockPosition(), this.entity.getSpeed());
        return this;
    }

    public MobController followPlayer() {
        ServerPlayerEntity player = Server.getFirstPlayer();
        MovePlayerEntity goal = new MovePlayerEntity(this.entity,
                new BlockPos(player.getX(), player.getY(), player.getZ()), this.entity.getSpeed());
        this.entity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> {
            this.entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
        });
        this.entity.goalSelector.addGoal(1, goal);
        return this;
    }
    public MobController followPlayer(String playerName) {
        ServerPlayerEntity player = Server.getPlayerByName(playerName);
        MovePlayerEntity goal = new MovePlayerEntity(this.entity,
                new BlockPos(player.getX(), player.getY(), player.getZ()), this.entity.getSpeed());
        this.entity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> {
            this.entity.goalSelector.removeGoal(prioritizedGoal.getGoal());
        });
        this.entity.goalSelector.addGoal(1, goal);
        return this;
    }

    @Documentate(
            desc = "Stops mob from moving"
    )
    public MobController stopMove(){
        this.entity.goalSelector.removeGoal(this.moveGoal);
        try {
            this.entity.goalSelector.removeGoal(MovePlayerEntity.movePlayerEntities.get(this.entity));
        } catch (Exception ignored){}
        return this;
    }

    @Documentate(
            desc = "Makes mob looking at entity"
    )
    public MobController lookAt(Object entity, Double pitch, Double yaw) {
        this.entity.lookAt((Entity) entity, yaw.floatValue(), pitch.floatValue());
        return this;
    }
    public MobController lookAt(String mobId) {
        this.entity.lookAt(MobJS.controllers.get(mobId).getEntity(), 0, 0);
        return this;
    }

    public MobController jump() {
        if (this.entity instanceof NPCEntity) {
            ((NPCEntity) this.entity).jump();
        } else {
            this.entity.getJumpControl().jump();
        }
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
        return Objects.requireNonNull(this.entity.getCustomName()).getContents();
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
    public MobController setItemMainHand(Integer item) {
        this.entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(WorldWrapper.item(item)));
        return this;
    }
    public MobController setItemOffHand(Integer item) {
        this.entity.setItemInHand(Hand.OFF_HAND, new ItemStack(WorldWrapper.item(item)));
        return this;
    }
    public MobController holdItem(Integer hand, Integer item) {
        this.entity.setItemInHand(WorldWrapper.selectHand(hand), new ItemStack(WorldWrapper.item(item)));
        return this;
    }


    public MobController setItemMainHand(Object item) {
        holdItem(0, item);
        return this;
    }
    public MobController setItemOffHand(Object item) {
        holdItem(1, item);
        return this;
    }
    public MobController holdItem(Integer hand, Object item) {
        if (item instanceof ItemStack) {
            this.entity.setItemInHand(WorldWrapper.selectHand(hand), (ItemStack) item);
        } else {
            try {
                this.entity.setItemInHand(WorldWrapper.selectHand(hand), JSItem.items.get(item).item);
            } catch (Exception e) {
                Chat.sendError("Item is not an ItemStack");
                return this;
            }
        }
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
        this.entity.addEffect(new EffectInstance(WorldWrapper.toEffect(effect), level, time));
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
        this.entity.setPos(x, this.entity.getY(), this.entity.getZ());
        return this;
    }

    @Documentate(
            desc = "Sets mob's Y position"
    )
    public MobController setY(Double y) {
        this.entity.setYya(y.floatValue());
        this.entity.setPos(this.entity.getX(), y, this.entity.getZ());
        return this;
    }

    @Documentate(
            desc = "Sets mob's Z position"
    )
    public MobController setZ(Double z) {
        this.entity.setZza(z.floatValue());
        this.entity.setPos(this.entity.getX(), this.entity.getY(), z);
        return this;
    }

    public MobController setPos(Double x, Double y, Double z) {
        this.entity.setPos(x, y, z);
        return this;
    }

    public MobController setPos(Double[] pos) {
        this.entity.setPos(pos[0], pos[1], pos[2]);
        return this;
    }

    public Double getX() {
        return this.entity.getX();
    }

    public Double getY() {
        return this.entity.getY();
    }

    public Double getZ() {
        return this.entity.getZ();
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
    public MobController swing(Integer hand) {
        this.entity.swing(WorldWrapper.selectHand(hand));
        return this;
    }

    @Documentate(
            desc = "Sets mob's head rotation"
    )
    public void setHeadRotation(Double[] pos) {
        this.setHeadRotation(pos[0], pos[1]);
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
    public MobController setTarget(String mobId) {
        this.entity.setTarget(MobJS.controllers.get(mobId).getMobEntity());
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
    public MobController setEntityFocused(String mobId, Double pitch, Double yaw) {
        this.entity.lookAt(MobJS.controllers.get(mobId).getEntity(), pitch.floatValue(), yaw.floatValue());
        return this;
    }

    @Documentate(
            desc = "Sets mob focus on player"
    )
    public MobController lookPlayer(PlayerEntity player, float pitch, float yaw) {
        this.entity.lookAt(player, pitch, yaw);
        return this;
    }
    public MobController lookPlayer(String player, Double pitch, Double yaw) {
        this.entity.lookAt(Server.getPlayerByName(player), pitch.floatValue(), yaw.floatValue());
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
    public Boolean isAlive() {
        Minecraft.getInstance().setScreen(null);
        return this.entity.isAlive();
    }
    public MobController attackPlayer(String player) {
        this.entity.setTarget((LivingEntity) Server.getPlayerByName(player).getEntity());
        return this;
    }
    public MobController attackPlayer(PlayerEntity player) {
        this.entity.setTarget((LivingEntity) player.getEntity());
        return this;
    }

    public MobController attackMob(String id) {
        this.entity.setTarget(MobJS.controllers.get(id).getMobEntity());
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
            Chat.sendNamed(Objects.requireNonNull(this.entity.getName()).getContents(), text);
        } catch (NullPointerException ex) {
            Chat.sendNamed(String.format("%s", SVEngine.DEFAULT_CHARACTER_NAME), text);
        }
        return this;
    }

    public MobController setHitBox(String pose, Double x, Double y, Double z) {
        this.entity.setBoundingBox(this.entity.getDimensions(getPose(pose)).makeBoundingBox(x, y, z));
        return this;
    }

    public MobController noCulling(Boolean method) {
        this.entity.noCulling = method;
        return this;
    }

    public Pose getPose(String pose) {
        switch (pose.toUpperCase()) {
            case "STANDING":
                return Pose.STANDING;
            case "FALL_FLYING":
                return Pose.FALL_FLYING;
            case "SPIN_ATTACK":
                return Pose.SPIN_ATTACK;
            case "SLEEPING":
                return Pose.SLEEPING;
            case "SWIMMING":
                return Pose.SWIMMING;
            case "CROUCHING":
                return Pose.CROUCHING;
            case "GET":
            default:
                return this.entity.getPose();
        }
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

    public MobController changeDimension(String world) {
        this.entity.changeDimension(Objects.requireNonNull(Objects.requireNonNull(Server.getWorld(world).getServer()).getLevel(Server.getWorld(world).dimension())));
        return this;
    }

    public MobController noCollide(Boolean method) {
        this.entity.noCulling = method;
        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    private void registerFunctions() {

        try {
            Method noCollide = MobController.class.getMethod("noCollide", Boolean.class);
            methodsToAdd.add(noCollide);
            Method changeDimension = MobController.class.getMethod("changeDimension", String.class);
            methodsToAdd.add(changeDimension);
            Method canPickUpLoot = MobController.class.getMethod("canPickUpLoot", Boolean.class);
            methodsToAdd.add(canPickUpLoot);
            Method setTarget = MobController.class.getMethod("setTarget", String.class);
            methodsToAdd.add(setTarget);
            Method isAlive = MobController.class.getMethod("isAlive");
            methodsToAdd.add(isAlive);
            Method attackPlayer = MobController.class.getMethod("attackPlayer", String.class);
            methodsToAdd.add(attackPlayer);
            Method setAggressive = MobController.class.getMethod("setAggressive", Boolean.class);
            methodsToAdd.add(setAggressive);
            Method moveTo = MobController.class.getMethod("moveTo", Double.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(moveTo);
            Method lookAtPlayer = MobController.class.getMethod("lookPlayer", String.class, Double.class, Double.class);
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
            Method giveItem = MobController.class.getMethod("holdItem", Integer.class, Integer.class);
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
            Method swingHand = MobController.class.getMethod("swing", Integer.class);
            methodsToAdd.add(swingHand);
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
            Method setItemMainH = MobController.class.getMethod("setItemMainHand", Integer.class);
            methodsToAdd.add(setItemMainH);
            Method setItemOffH = MobController.class.getMethod("setItemOffHand", Integer.class);
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
            Method holdII = MobController.class.getMethod("holdItem", Integer.class, Object.class);
            methodsToAdd.add(holdII);
            Method iMh = MobController.class.getMethod("setItemMainHand", Object.class);
            methodsToAdd.add(iMh);
            Method iOh = MobController.class.getMethod("setItemOffHand", Object.class);
            methodsToAdd.add(iOh);
            Method attackMob = MobController.class.getMethod("attackMob", String.class);
            methodsToAdd.add(attackMob);
            Method moveToPl = MobController.class.getMethod("moveToPlayer", String.class);
            methodsToAdd.add(moveToPl);
            Method mTpl = MobController.class.getMethod("moveToPlayer");
            methodsToAdd.add(mTpl);
            Method shShN = MobController.class.getMethod("shouldShowName", Boolean.class);
            methodsToAdd.add(shShN);
            Method followP = MobController.class.getMethod("followPlayer", String.class);
            methodsToAdd.add(followP);
            Method followPl = MobController.class.getMethod("followPlayer");
            methodsToAdd.add(followPl);
            Method getX = MobController.class.getMethod("getX");
            methodsToAdd.add(getX);
            Method getY = MobController.class.getMethod("getY");
            methodsToAdd.add(getY);
            Method getZ = MobController.class.getMethod("getZ");
            methodsToAdd.add(getZ);
            Method setPos = MobController.class.getMethod("setPos", Double.class, Double.class, Double.class);
            methodsToAdd.add(setPos);
            Method jump = MobController.class.getMethod("jump");
            methodsToAdd.add(jump);
            Method getPose = MobController.class.getMethod("getPose", String.class);
            methodsToAdd.add(getPose);
            Method setHitBox = MobController.class.getMethod("setHitBox", String.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(setHitBox);
            Method noCulling = MobController.class.getMethod("noCulling", Boolean.class);
            methodsToAdd.add(noCulling);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, this);
            this.put(m.getName(), this, methodInstance);
        }
    }

    public static void putIntoScope (Scriptable scope) {
        MobController ef = new MobController();
        ef.setParentScope(scope);

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
