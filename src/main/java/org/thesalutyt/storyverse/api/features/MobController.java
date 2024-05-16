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
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MoveGoal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MobController implements EnvResource {
    private final MobEntity entity;
    public HashMap<String, Runnable> handlers = new HashMap<>();
    public static HashMap<UUID, MobController> mobControllers = new HashMap<>();
    private boolean hasCustomUUID = false;
    private UUID customUUID = null;
    public static ArrayList<String> interactActionsID = new ArrayList<>();
    private Goal moveGoal;

    @Documentate(
            desc = "Setups MobController entity"
    )
    public MobController(Entity entity){
        this.entity = (MobEntity) entity;
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
    public MobController lookAt(Entity entity, float pitch, float yaw) {
        this.entity.lookAt(entity, pitch, yaw);
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

    @Documentate(
            desc = "Returns mob's custom name"
    )
    public String getName() {
        return this.entity.getName().getContents();
    }

    @Documentate(
            desc = "Makes mob glow"
    )
    public MobController setGlow(boolean method) {
        this.entity.setGlowing(method);
        return this;
    }

    @Documentate(
            desc = "Disables or enables mob's AI"
    )
    public MobController setNoAI(boolean method) {
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
    public MobController setInvulnerable(boolean method) {
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
    public MobController setRotation(float x, float y) {
        this.entity.xRot = x;
        this.entity.yRot = y;
        return this;
    }

    @Documentate(
            desc = "Sets mob's X rotation"
    )
    public MobController setXRotation(float x) {
        this.entity.xRot = x;
        return this;
    }

    @Documentate(
            desc = "Sets mob's Y rotation"
    )
    public MobController setYRotation(float y) {
        this.entity.yRot = y;
        return this;
    }

    @Documentate(
            desc = "Gives mob item in hand"
    )
    public MobController setItemHand(Item item) {
        this.entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(item));
        return this;
    }
    public MobController holdItem(Hand hand, ItemStack item) {
        this.entity.setItemInHand(hand, item);
        return this;
    }
    @Documentate(
            desc = "Removes mob"
    )
    public MobController remove() {
        this.entity.remove();
        return this;
    }

    public MobController remove(boolean keepData) {
        this.entity.remove(keepData);
        return this;
    }

    @Documentate(
            desc = "Adds effect to mob"
    )
    public MobController addEffect(Effect effect, int time, int level) {
        this.entity.addEffect(new EffectInstance(effect, level, time));
        return this;
    }

    @Documentate(
            desc = "Hurts mob"
    )
    public MobController hurt(String damageSource, float amount) {
        this.entity.hurt(new DamageSource(damageSource), amount);
        return this;
    }

    public MobController hurt(float amount) {
        this.entity.hurt(new DamageSource("storyverse.damage.script"), amount);
        return this;
    }

    @Documentate(
            desc = "Disable or enable physics for a mob"
    )
    public MobController setPhysics(boolean method) {
        this.entity.setNoGravity(method);
        return this;
    }

    @Documentate(
            desc = "Sets mob's speed"
    )
    public MobController setSpeed(float speed) {
        this.entity.setSpeed(speed);
        return this;
    }

    @Documentate(
            desc = "Sets mob's X position"
    )
    public MobController setX(float x) {
        this.entity.setXxa(x);
        return this;
    }

    @Documentate(
            desc = "Sets mob's Y position"
    )
    public MobController setY(float y) {
        this.entity.setYya(y);
        return this;
    }

    @Documentate(
            desc = "Sets mob's Z position"
    )
    public MobController setZ(float z) {
        this.entity.setZza(z);
        return this;
    }

    @Documentate(
            desc = "Sets mob's name visible"
    )
    public MobController shouldShowName(boolean method) {
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

    @Documentate(
            desc = "Sets mob's head rotation"
    )
    public MobController setHeadRotation(float[] pos) {
        this.setHeadRotation(pos[0], pos[1]);
        return this;
    }

    public MobController setHeadRotation(float x, float y) {
        this.entity.xRot = x;
        this.entity.yRot = y;
        return this;
    }

    @Documentate(
            desc = "Sets mob's body rotation"
    )
    public MobController setBodyRot(float y) {
        this.entity.yBodyRot = y;
        return this;
    }

    @Documentate(
            desc = "Sets mob's target"
    )
    public MobController setTarget(LivingEntity target) {
        this.entity.setTarget(target);
        return this;
    }

    @Documentate(
            desc = "Sets mob focus on entity"
    )
    public MobController setEntityFocused(Entity target, float pitch, float yaw) {
        this.entity.lookAt(target, pitch, yaw);
        return this;
    }
    public MobController setEntityFocused(Entity target, Vector3d vector3d) {
        this.entity.lookAt(target, 1, 1);
        return this;
    }

    @Documentate(
            desc = "Sets mob focus on player"
    )
    public MobController lookPlayer(PlayerEntity player, float pitch, float yaw) {
        this.entity.lookAt(player, pitch, yaw);
        return this;
    }

    @Documentate(
            desc = "Sets mob's health value"
    )
    public MobController setHealth(float health) {
        this.entity.setHealth(health);
        return this;
    }

    @Documentate(
            desc = "Toggles mob's ability to pickup loot"
    )
    public MobController canPickUpLoot(boolean method) {
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
    public MobController setAggressive(boolean method) {
        this.entity.setAggressive(method);
        return this;
    }

    @Documentate(
            desc = "Sets mob invisible"
    )
    public MobController setInvisible(boolean method) {
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
            Chat.sendNamed("NPC", text);
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

    @Override
    public String getResourceId() {
        return "MobController";
    }
}
