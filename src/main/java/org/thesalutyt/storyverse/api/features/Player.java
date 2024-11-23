package org.thesalutyt.storyverse.api.features;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MoveGoal;
import org.thesalutyt.storyverse.common.specific.networking.Networking;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.ScriptExecutionPacket;

import java.lang.reflect.Method;
import java.util.*;

public class Player extends ScriptableObject implements EnvResource{
    private ServerPlayerEntity player;
    private static ArrayList<ServerPlayerEntity> players = new ArrayList<>();
    public Boolean isWaterWalking = false;

    public Player(ServerPlayerEntity player) {
        this.player = player;
        players.add(player);
    }

    public static ServerPlayerEntity getFirstPlayer() {
        return players.get(0);
    }

    public static ArrayList<ServerPlayerEntity> getAllRegistered() {
        return players;
    }

    @Documentate(
            desc = "Returns player's name"
    )
    public String getPlayerName() {
        return player.getName().getContents();
    }
    @Documentate(
            desc = "Returns player's experience levels"
    )
    public Integer getXPLevels() {
        return player.experienceLevel;
    }
    @Documentate(
            desc = "Gives player's experience levels"
    )
    public void giveXPLevels(Integer levels) {
        player.giveExperienceLevels(levels);
    }
    @Documentate(
            desc = "Sets player's experience levels"
    )
    public void setXPLevels(Integer levels) {
        player.setExperienceLevels(levels);
    }

    @Documentate(
            desc = "Returns player's experience points"
    )
    public Integer getXPPoints() {
        return player.totalExperience;
    }
    @Documentate(
            desc = "Gives player's experience levels"
    )
    public void giveXPPoints(Integer points) {
        player.giveExperiencePoints(points);
    }
    @Documentate(
            desc = "Sets player's experience levels"
    )
    public void setXPPoints(Integer points) {
        player.setExperiencePoints(points);
    }

    @Documentate(
            desc = "Sets player non-killable"
    )
    public void setInvulnerable(Boolean is) {
        player.setInvulnerable(is);
    }

    @Documentate(
            desc = "Sets player invisible"
    )
    public void setInvisible(Boolean is) {
        player.setInvisible(is);
    }
    @Documentate(
            desc = "Sets player name visible"
    )
    public void setNameVisible(Boolean is) {player.setCustomNameVisible(is);}
    @Documentate(
            desc = "Applies custom name to a player"
    )
    public void setCustomName(String name) {player.setCustomName(new StringTextComponent(name));}

    @Documentate(
            desc = "Sends player a message"
    )
    public void sendMessage(String text) {
        player.sendMessage(new StringTextComponent(text), player.getUUID());
    }

    @Documentate(
            desc = "Sends player some message with sender name and text"
    )
    public void sendNamed(String name, String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("%s[%s]", SVEngine.CHARACTER_COLOR_STR, name));
        message.append(String.format("§r %s", text));

        player.sendMessage(message, player.getUUID());
    }
    @Documentate(
            desc = "Sends message as player"
    )
    public void sendAsPlayer(String message) {
        String final_message = String.format("<%s> %s", getPlayerName(), message);
        player.sendMessage(new StringTextComponent(final_message), player.getUUID());
    }

    @Documentate(
            desc = "Sets player's head rotation"
    )
    public void setHeadRotation(Double pitch, Double yaw) {
        player.xRot = yaw.floatValue();
        player.xRotO = yaw.floatValue();
        player.yHeadRot = pitch.floatValue();
        player.yBodyRot = pitch.floatValue();
        player.yRot = pitch.floatValue();
        player.yRotO = pitch.floatValue();
        player.yHeadRotO = pitch.floatValue();
        player.yBodyRotO = pitch.floatValue();
    }
    @Documentate(
            desc = "Returns player's X position"
    )
    public Double getX() {
        return player.getX();
    }

    @Documentate(
            desc = "Returns player's Y position"
    )
    public Double getY() {
        return player.getY();
    }

    @Documentate(
            desc = "Returns player's Z position"
    )
    public Double getZ() {
        return player.getZ();
    }

    @Documentate(
            desc = "Returns player's position"
    )
    public Double[] getPosition() {
        return new Double[]{getX(), getY(), getZ()};
    }
    public BlockPos getBlockPosition() {return player.blockPosition();}

    @Documentate(
            desc = "Sets player's position"
    )
    public void setPosition(Double[] pos) {
        player.moveTo(pos[0], pos[1], pos[2]);
    }

    public void setPosition(Double x, Double y, Double z) {
        setPosition(new Double[]{x, y, z});
    }

    @Documentate(
            desc = "Teleports player"
    )
    public void teleportTo(Double x, Double y, Double z) {
        setPosition(new Double[]{x, y, z});
    }

    @Documentate(
            desc = "Returns player's entity"
    )
    public Entity getEntity() {
        return player.getEntity();
    }
    public PlayerEntity getPlayerEntity() {
        return (PlayerEntity) player.getEntity();
    }

    @Documentate(
            desc = "Makes player ride some entity"
    )
    public void startRiding(Object entity) {
        player.getEntity().startRiding((Entity) entity);
    }

    public void startRiding(String entity) {
        player.getEntity().startRiding(MobJS.controllers.get(entity).getEntity());
    }

    public void testRidding() {
        player.getEntity().startRiding(player.getEntity());
    }

    @Documentate(
            desc = "Stops player from riding"
    )
    public void stopRiding() {
        player.getEntity().stopRiding();
    }

    @Documentate(
            desc = "Sets player's X position"
    )
    public void setX(Double x) {
        setPosition(x, getY(), getZ());
    }

    @Documentate(
            desc = "Sets player's Y position"
    )
    public void setY(Double y) {
        setPosition(getX(), y, getZ());
    }

    @Documentate(
            desc = "Sets player's Z position"
    )
    public void setZ(Double z) {
        setPosition(getX(), getY(), z);
    }

    @Documentate(
            desc = "Sets player's game mode"
    )
    public void setGameMode(Integer mode) {
        if (mode == 0) {
            player.setGameMode(GameType.SURVIVAL);

        } else if (mode == 1) {
            player.setGameMode(GameType.CREATIVE);
        } else if (mode == 2) {
            player.setGameMode(GameType.ADVENTURE);
        } else if (mode == 3) {
            player.setGameMode(GameType.SPECTATOR);
        }
    }

    public void setGameMode(String mode) {
        switch (mode) {
            case "survival":
                setGameMode(0);
            case "creative":
                setGameMode(1);
            case "adventure":
                setGameMode(2);
            case "spectator":
                setGameMode(3);
            default:
        }
    }

    @Documentate(
            desc = "Toggles player's abilities"
    )
    public void canBuild(Boolean method) {
        player.abilities.mayBuild = method;
    }

    public void canFly(Boolean method) {
        player.abilities.flying = method;
    }

    public void instantBuild(Boolean method) {
        player.abilities.instabuild = method;
    }

    public void canDie(Boolean method) {
        player.abilities.invulnerable = method;
    }

    @Documentate(
            desc = "Sets player's speed"
    )
    public void setWalkSpeed(Double walkSpeed) {
        player.abilities.setWalkingSpeed(walkSpeed.floatValue());
    }

    @Documentate(
            desc = "Sets player's fly speed"
    )
    public void setFlyingSpeed(Double flyingSpeed) {
        player.abilities.setFlyingSpeed(flyingSpeed.floatValue());
    }

    @Documentate(
            desc = "Kills player"
    )
    public void kill() {
        player.kill();
    }
    public Player setPlayerByName(String playerName) {
        player = Server.getPlayerByName(playerName);
        return this;
    }
    public void setPlayer(Object newPlayer) {
        if (!(newPlayer instanceof ServerPlayerEntity)) {
            return;
        }
        player = (ServerPlayerEntity) newPlayer;
    }
    @Documentate(
            desc = "Returns player's UUID"
    )
    public UUID getUUID() {
        return player.getUUID();
    }

    @Documentate(
            desc = "Hurts player"
    )
    public void hurt(Double damage, String damageSource) {
        player.hurt(new DamageSource(damageSource), damage.floatValue());
    }
    public void hurt(Double damage) {
        hurt(damage, "storyverse:script");
    }
    public void remove(Boolean keepData) {player.remove(keepData);}
    public Boolean isWaterWalking() {
        return isWaterWalking;
    }
    public void setWaterWalk(Boolean method) {
        isWaterWalking = method;
    }
    public void tick() {
        if (this.isWaterWalking()) {
            List<Block> water = Arrays.asList(Blocks.WATER, Blocks.KELP, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS);
            final BlockPos BP = new BlockPos(player.getX(), player.getY() - 0.25, player.getZ());
            final BlockPos AIR = new BlockPos(player.getX(), player.getY() + 0.3, player.getZ());
            if (water.contains(player.getCommandSenderWorld().getBlockState(BP).getBlock()) && !player.isSwimming() && !player.isShiftKeyDown() && player.getCommandSenderWorld().getBlockState(AIR).getBlock() == Blocks.AIR) {
                player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                if(Minecraft.getInstance().options.keyJump.isDown()) player.setOnGround(true);
                if(player.isSprinting()) player.setSprinting(true);
            }
        }
    }
    public String getPlayerWorld() {
        return player.getLevel().toString();
    }

    public void move(Double x, Double y, Double z, Double speed) {
        MobEntity entity = (MobEntity) player.getEntity();

        entity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> entity.goalSelector.removeGoal(prioritizedGoal.getGoal()));
        BlockPos pos = new BlockPos(x, y, z);
        MoveGoal moveGoal = new MoveGoal(entity, pos, speed.floatValue());
        entity.goalSelector.addGoal(1, moveGoal);
    }

    public Boolean isCrouching() {
        return player.isCrouching();
    }

    public Boolean isSprinting() {
        return player.isSprinting();
    }

    public void setSpeed(Double speed) {
        player.setSpeed(speed.floatValue());
    }

    public String getItemInHand(Integer hand) {
        switch (hand) {
            case 0:
                return new JSItem(player.getMainHandItem()).id;
            case 1:
                return new JSItem(player.getOffhandItem()).id;
            default:
                return null;
        }
    }

    public Double getHealth() {
        return (double) player.getHealth();
    }

    public Boolean nearTo(NativeArray pos, Double radius) {
        System.out.println("nearTo: " + pos.get(0) + " " + pos.get(1) + " " + pos.get(2));

        Double[] pos_ = new Double[]{(Double) pos.get(0), Double.parseDouble(pos.get(1).toString()), (Double) pos.get(2)};
        return MathScript.squareDistance(new Double[]{player.getX(), player.getY(), player.getZ()}, pos_) < radius;
    }

    public Boolean getAdvancement(String playerName, String advancementName) {
        ResourceLocation achievementID = new ResourceLocation(advancementName);
        ServerPlayerEntity player = Server.getPlayerByName(playerName);
        Advancement advancement = Objects.requireNonNull(player.getServer()).getAdvancements().getAdvancement(achievementID);
        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            return progress.isDone();
        } else {
            return false;
        }
    }

    public void give(String itemStack) {
        player.addItem(JSItem.getStack(itemStack));
    }

    public void addTag(String tag) {
        player.addTag(tag);
    }

    public void removeTag(String tag) {
        player.removeTag(tag);
    }

    public void setHealth(Double health) {
        player.setHealth(health.floatValue());
    }

    public void sleep(NativeArray pos) {
        player.startSleeping(new BlockPos((int) pos.get(0), (int) pos.get(1), (int) pos.get(2)));
    }

    public void wake() {
        player.stopSleeping();
    }

    public Boolean isSleeping() {
        return player.isSleeping();
    }

    public void stopSleeping() {
        player.stopSleeping();
    }

    public void showActionBar(String message) {
        player.connection.send(new STitlePacket(STitlePacket.Type.ACTIONBAR, new StringTextComponent(message)));
    }

    public void showTitle(String message) {
        player.connection.send(new STitlePacket(STitlePacket.Type.TITLE, new StringTextComponent(message)));
    }

    public void showSubtitle(String message) {
        player.connection.send(new STitlePacket(STitlePacket.Type.SUBTITLE, new StringTextComponent(message)));
    }

    public void runScriptAs(String script, String player) {
        ScriptExecutionPacket packet;

        if (player.equals("$every")) {
            for (ServerPlayerEntity p : Server.getPlayers()) {
                packet = new ScriptExecutionPacket(script);
                Networking.sendToPlayer(packet, p);
            }
            return;
        }

        packet = new ScriptExecutionPacket(script);
        Networking.sendToPlayer(packet, this.player);
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public Object getNative() {
        return player;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        // Создаем объект, к которому потом будем обращаться
        Player ef = new Player(Server.getFirstPlayer());
        // Это не обязательно, но я указываю, что у этого объекта есть объект выше уровнем
        ef.setParentScope(scope);

        // Это список функций, которые потом будут добавлены в объект ExternalFunctions
        // Здесь просто повторяй, я и сам не стал сильно глубоко копаться в деталях

        try {
            Method sendMessage = Player.class.getMethod("sendMessage", String.class);
            methodsToAdd.add(sendMessage);
            Method sendNamed = Player.class.getMethod("sendNamed", String.class, String.class);
            methodsToAdd.add(sendNamed);
            Method sendAsPlayer = Player.class.getMethod("sendAsPlayer", String.class);
            methodsToAdd.add(sendAsPlayer);
            Method setCustomNameVisible = Player.class.getMethod("setNameVisible", Boolean.class);
            methodsToAdd.add(setCustomNameVisible);
            Method setCustomName = Player.class.getMethod("setCustomName", String.class);
            methodsToAdd.add(setCustomName);
            Method giveXPP = Player.class.getMethod("giveXPPoints", Integer.class);
            methodsToAdd.add(giveXPP);
            Method getXPP = Player.class.getMethod("getXPPoints");
            methodsToAdd.add(getXPP);
            Method setXPP = Player.class.getMethod("setXPPoints", Integer.class);
            methodsToAdd.add(setXPP);
            Method giveXPL = Player.class.getMethod("giveXPLevels", Integer.class);
            methodsToAdd.add(giveXPL);
            Method getXPL = Player.class.getMethod("getXPLevels");
            methodsToAdd.add(getXPL);
            Method setXPL = Player.class.getMethod("setXPLevels", Integer.class);
            methodsToAdd.add(setXPL);
            Method setHeadRot = Player.class.getMethod("setHeadRotation", Double.class, Double.class);
            methodsToAdd.add(setHeadRot);
            Method setPosD = Player.class.getMethod("setPosition", Double.class, Double.class, Double.class);
            methodsToAdd.add(setPosD);
            Method setX = Player.class.getMethod("setX", Double.class);
            methodsToAdd.add(setX);
            Method setY = Player.class.getMethod("setY", Double.class);
            methodsToAdd.add(setY);
            Method setZ = Player.class.getMethod("setZ", Double.class);
            methodsToAdd.add(setZ);
            Method teleport = Player.class.getMethod("teleportTo", Double.class, Double.class, Double.class);
            methodsToAdd.add(teleport);
            Method hurt = Player.class.getMethod("hurt", Double.class);
            methodsToAdd.add(hurt);
            Method hurt_ds = Player.class.getMethod("hurt", Double.class, String.class);
            methodsToAdd.add(hurt_ds);
            Method setGameMode = Player.class.getMethod("setGameMode", String.class);
            methodsToAdd.add(setGameMode);
            Method setGMInt = Player.class.getMethod("setGameMode", Integer.class);
            methodsToAdd.add(setGMInt);
            Method getX = Player.class.getMethod("getX");
            methodsToAdd.add(getX);
            Method getY = Player.class.getMethod("getY");
            methodsToAdd.add(getY);
            Method getZ = Player.class.getMethod("getZ");
            methodsToAdd.add(getZ);
            Method getPosition = Player.class.getMethod("getPosition");
            methodsToAdd.add(getPosition);
            Method getBlockPos = Player.class.getMethod("getBlockPosition");
            methodsToAdd.add(getBlockPos);
            Method kill = Player.class.getMethod("kill");
            methodsToAdd.add(kill);
            Method getUUID = Player.class.getMethod("getUUID");
            methodsToAdd.add(getUUID);
            Method getName = Player.class.getMethod("getPlayerName");
            methodsToAdd.add(getName);
            Method getNative = Player.class.getMethod("getNative");
            methodsToAdd.add(getNative);
            Method canFly = Player.class.getMethod("canFly", Boolean.class);
            methodsToAdd.add(canFly);
            Method canBuild = Player.class.getMethod("canBuild", Boolean.class);
            methodsToAdd.add(canBuild);
            Method canDie = Player.class.getMethod("canDie", Boolean.class);
            methodsToAdd.add(canDie);
            Method setFlySpeed = Player.class.getMethod("setFlyingSpeed", Double.class);
            methodsToAdd.add(setFlySpeed);
            Method setWalkSpeed = Player.class.getMethod("setWalkSpeed", Double.class);
            methodsToAdd.add(setWalkSpeed);
            Method startRiding = Player.class.getMethod("startRiding", Object.class);
            methodsToAdd.add(startRiding);
            Method stopRiding = Player.class.getMethod("stopRiding");
            methodsToAdd.add(stopRiding);
            Method testRidding = Player.class.getMethod("testRidding");
            methodsToAdd.add(testRidding);
            Method setInvisible = Player.class.getMethod("setInvisible", Boolean.class);
            methodsToAdd.add(setInvisible);
            Method setInvulnerable = Player.class.getMethod("setInvulnerable", Boolean.class);
            methodsToAdd.add(setInvulnerable);
            Method getPlayerEntity = Player.class.getMethod("getPlayerEntity");
            methodsToAdd.add(getPlayerEntity);
            Method getEntity = Player.class.getMethod("getEntity");
            methodsToAdd.add(getEntity);
            Method instantBuild = Player.class.getMethod("instantBuild", Boolean.class);
            methodsToAdd.add(instantBuild);
            Method setByName = Player.class.getMethod("setPlayerByName", String.class);
            methodsToAdd.add(setByName);
            Method setPl = Player.class.getMethod("setPlayer", Object.class);
            methodsToAdd.add(setPl);
            Method getPlayer = Player.class.getMethod("getPlayer");
            methodsToAdd.add(getPlayer);
            Method remove = Player.class.getMethod("remove", Boolean.class);
            methodsToAdd.add(remove);
            Method startRide = Player.class.getMethod("startRiding", String.class);
            methodsToAdd.add(startRide);
            Method tick = Player.class.getMethod("tick");
            methodsToAdd.add(tick);
            Method isWaterWalking = Player.class.getMethod("isWaterWalking");
            methodsToAdd.add(isWaterWalking);
            Method setWaterWalking = Player.class.getMethod("setWaterWalk", Boolean.class);
            methodsToAdd.add(setWaterWalking);
            Method getWorld = Player.class.getMethod("getPlayerWorld");
            methodsToAdd.add(getWorld);
            Method move = Player.class.getMethod("move", Double.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(move);
            Method isCrouching = Player.class.getMethod("isCrouching");
            methodsToAdd.add(isCrouching);
            Method isSprinting = Player.class.getMethod("isSprinting");
            methodsToAdd.add(isSprinting);
            Method getItemInHand = Player.class.getMethod("getItemInHand", Integer.class);
            methodsToAdd.add(getItemInHand);
            Method setSpeed = Player.class.getMethod("setSpeed", Double.class);
            methodsToAdd.add(setSpeed);
            Method nearTo = Player.class.getMethod("nearTo", NativeArray.class, Double.class);
            methodsToAdd.add(nearTo);
            Method getAdvancement = Player.class.getMethod("getAdvancement", String.class, String.class);
            methodsToAdd.add(getAdvancement);
            Method getHealth = Player.class.getMethod("getHealth");
            methodsToAdd.add(getHealth);
            Method addTag = Player.class.getMethod("addTag", String.class);
            methodsToAdd.add(addTag);
            Method removeTag = Player.class.getMethod("removeTag", String.class);
            methodsToAdd.add(removeTag);
            Method give = Player.class.getMethod("give", String.class);
            methodsToAdd.add(give);
            Method setHealth = Player.class.getMethod("setHealth", Double.class);
            methodsToAdd.add(setHealth);
            Method sleep = Player.class.getMethod("sleep", NativeArray.class);
            methodsToAdd.add(sleep);
            Method wakeUp = Player.class.getMethod("wake");
            methodsToAdd.add(wakeUp);
            Method isSleeping = Player.class.getMethod("isSleeping");
            methodsToAdd.add(isSleeping);
            Method stopSleeping = Player.class.getMethod("stopSleeping");
            methodsToAdd.add(stopSleeping);
            Method shouldShowCape = Player.class.getMethod("showActionBar", String.class);
            methodsToAdd.add(shouldShowCape);
            Method runAs = Player.class.getMethod("runScriptAs", String.class, String.class);
            methodsToAdd.add(runAs);
            Method showTitle = Player.class.getMethod("showTitle", String.class);
            methodsToAdd.add(showTitle);
            Method showSubTitle = Player.class.getMethod("showSubtitle", String.class);
            methodsToAdd.add(showSubTitle);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // Здесь функции укладываются в ExternalFunctions
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        // Здесь ExternalFunctions укладывается в пространство имен верхнего уровня
        scope.put("player", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "player";
    }

    @Override
    public String getClassName() {
        return "player";
    }
}