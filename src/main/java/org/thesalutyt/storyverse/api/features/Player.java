package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.MoveThroughVillageAtNightGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.special.FadeScreenPacket;
import org.thesalutyt.storyverse.common.dimension.mover.Mover;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;
import org.thesalutyt.storyverse.common.specific.networking.Networking;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class Player extends ScriptableObject implements EnvResource{
    private static ServerPlayerEntity player;
    public static HashMap<String, ArrayList<BaseFunction>> events = new HashMap<>();
    public Player(ServerPlayerEntity player) {
        Player.player = player;
    }

    @Documentate(
            desc = "Returns player's name"
    )
    public static String getPlayerName() {
        return player.getName().getContents();
    }
    @Documentate(
            desc = "Returns player's experience levels"
    )
    public static Integer getXPLevels() {
        return player.experienceLevel;
    }
    @Documentate(
            desc = "Gives player's experience levels"
    )
    public static void giveXPLevels(Integer levels) {
        player.giveExperienceLevels(levels);
    }
    @Documentate(
            desc = "Sets player's experience levels"
    )
    public static void setXPLevels(Integer levels) {
        player.setExperienceLevels(levels);
    }

    @Documentate(
            desc = "Returns player's experience points"
    )
    public static Integer getXPPoints() {
        return player.totalExperience;
    }
    @Documentate(
            desc = "Gives player's experience levels"
    )
    public static void giveXPPoints(Integer points) {
        player.giveExperiencePoints(points);
    }
    @Documentate(
            desc = "Sets player's experience levels"
    )
    public static void setXPPoints(Integer points) {
        player.setExperiencePoints(points);
    }

    @Documentate(
            desc = "Sets player non-killable"
    )
    public static void setInvulnerable(Boolean is) {
        player.setInvulnerable(is);
    }

    @Documentate(
            desc = "Sets player invisible"
    )
    public static void setInvisible(Boolean is) {
        player.setInvisible(is);
    }
    @Documentate(
            desc = "Sets player name visible"
    )
    public static void setNameVisible(Boolean is) {player.setCustomNameVisible(is);}
    @Documentate(
            desc = "Applies custom name to a player"
    )
    public static void setCustomName(String name) {player.setCustomName(new StringTextComponent(name));}

    @Documentate(
            desc = "Sends player a message"
    )
    public void sendMessage(String text) {
        Chat.sendMessage(text);
    }

    @Documentate(
            desc = "Sends player some message with sender name and text"
    )
    public static void sendNamed(String name, String text) {
        Chat.sendNamed(name, text);
    }
    @Documentate(
            desc = "Sends message as player"
    )
    public static void sendAsPlayer(String message) {
        String final_message = String.format("<%s> %s", getPlayerName(), message);
        Chat.sendMessage(final_message);
    }

    @Documentate(
            desc = "Sets player's head rotation"
    )
    public static void setHeadRotation(Double pitch, Double yaw) {
        player.yHeadRot = yaw.floatValue();
        player.xRot = pitch.floatValue();
    }
    @Documentate(
            desc = "Returns player's X position"
    )
    public static Double getX() {
        return player.getX();
    }

    @Documentate(
            desc = "Returns player's Y position"
    )
    public static Double getY() {
        return player.getY();
    }

    @Documentate(
            desc = "Returns player's Z position"
    )
    public static Double getZ() {
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
    public static void setPosition(Double[] pos) {
        player.moveTo(pos[0], pos[1], pos[2]);
    }

    public static void setPosition(Double x, Double y, Double z) {
        setPosition(new Double[]{x, y, z});
    }

    @Documentate(
            desc = "Teleports player"
    )
    public static void teleportTo(Double x, Double y, Double z) {
        setPosition(new Double[]{x, y, z});
    }

    @Documentate(
            desc = "Returns player's entity"
    )
    public static Entity getEntity() {
        return player.getEntity();
    }
    public static PlayerEntity getPlayerEntity() {
        return (PlayerEntity) player.getEntity();
    }

    @Documentate(
            desc = "Makes player ride some entity"
    )
    public static void startRiding(Object entity) {
        player.getEntity().startRiding((Entity) entity);
    }

    @Documentate(
            desc = "Stops player from riding"
    )
    public static void stopRiding() {
        player.getEntity().stopRiding();
    }

    @Documentate(
            desc = "Sets player's X position"
    )
    public static void setX(Double x) {
        setPosition(x, getY(), getZ());
    }

    @Documentate(
            desc = "Sets player's Y position"
    )
    public static void setY(Double y) {
        setPosition(getX(), y, getZ());
    }

    @Documentate(
            desc = "Sets player's Z position"
    )
    public static void setZ(Double z) {
        setPosition(getX(), getY(), z);
    }

    @Documentate(
            desc = "Sets player's game mode"
    )
    public static void setGameMode(Integer mode) {
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

    public static void setGameMode(String mode) {
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
                return;
        }
    }

    @Documentate(
            desc = "Shows player fade screen"
    )
    public static void showFadeScreen(int time, String colorString) {
        int color = (int)Long.parseLong(colorString, 16);
        FadeScreenPacket packet = new FadeScreenPacket(player.getUUID(), time, color);
        Networking.CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void showFadeScreen(PlayerEntity player, int time, String colorString) {
        int color = (int)Long.parseLong(colorString, 16);
        FadeScreenPacket packet = new FadeScreenPacket(player.getUUID(), time, color);
        Networking.CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void showFadeScreen(PlayerEntity player, int time) {
        showFadeScreen(player, time, "FF000000");
    }

    public static void showFadeScreen(int time) {
        showFadeScreen(time, "FF000000");
    }

    @Documentate(
            desc = "Toggles player's abilities"
    )
    public static void canBuild(Boolean method) {
        player.abilities.mayBuild = method;
    }

    public static void canFly(Boolean method) {
        player.abilities.flying = method;
    }

    public static void instantBuild(Boolean method) {
        player.abilities.instabuild = method;
    }

    public static void canDie(Boolean method) {
        player.abilities.invulnerable = method;
    }

    @Documentate(
            desc = "Sets player's speed"
    )
    public static void setWalkSpeed(Double walkSpeed) {
        player.abilities.setWalkingSpeed(walkSpeed.floatValue());
    }

    @Documentate(
            desc = "Sets player's fly speed"
    )
    public static void setFlyingSpeed(Double flyingSpeed) {
        player.abilities.setFlyingSpeed(flyingSpeed.floatValue());
    }

    @Documentate(
            desc = "Kills player"
    )
    public static void kill() {
        player.kill();
    }
    public static void setPlayerByName(String playerName) {
        player = Server.getPlayerByName(playerName);
    }
    public static void setPlayer(Object newPlayer) {
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
    public static void hurt(Double damage, String damageSource) {
        player.hurt(new DamageSource(damageSource), damage.floatValue());
    }
    public static void hurt(Double damage) {
        hurt(damage, "storyverse:script");
    }
    public static void remove(Boolean keepData) {player.remove(keepData);}
    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        runEvent(event.getPlayer().getName().getContents());
    }
    public static void addEventListener(String type, String arg, BaseFunction function) {
        ArrayList<BaseFunction> functions = new ArrayList<>();
        functions.add(function);
        switch (type) {
            case "sleep":
            case "sleep_in_bed":
                EventLoop.getLoopInstance().runImmediate(() -> {
                    if (!events.containsKey(arg)) {
                        events.put(arg, functions);
                    }
                });
            default:
                return;
        }
    }
    public static void runEvent(String arg) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            if (events.containsKey(arg)) {
                ArrayList<BaseFunction> arr = events.get(arg);
                Context ctx = Context.getCurrentContext();
                for (int i = 0; i < arr.size(); i++) {
                    arr.get(i).call(ctx, SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(), new Object[]{arg});
                }
            }
        });
    }
    public static void removeEventListener(String name, String id) {
        if (!Objects.equals(id, "sleep") && !Objects.equals(id, "sleep_in_bed")) {
            return;
        } else {
            events.remove(name);
        }
    }
    public static ServerPlayerEntity getPlayer() {
        return player;
    }

    public static Object getNative() {
        return player;
    }

    public static void putIntoScope (Scriptable scope) {
        // Создаем объект, к которому потом будем обращаться
        Player ef = new Player(Server.getDevPlayer());
        // Это не обязательно, но я указываю, что у этого объекта есть объект выше уровнем
        ef.setParentScope(scope);

        // Это список функций, которые потом будут добавлены в объект ExternalFunctions
        // Здесь просто повторяй, я и сам не стал сильно глубоко копаться в деталях
        ArrayList<Method> methodsToAdd = new ArrayList<>();

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
            Method stopRiding = Player.class.getMethod("stopRiding");
            methodsToAdd.add(stopRiding);
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
            Method addListener = Player.class.getMethod("addEventListener", String.class, String.class, BaseFunction.class);
            methodsToAdd.add(addListener);
            Method runEvent = Player.class.getMethod("runEvent", String.class);
            methodsToAdd.add(runEvent);
            Method removeListener = Player.class.getMethod("removeEventListener", String.class, String.class);
            methodsToAdd.add(removeListener);
            Method remove = Player.class.getMethod("remove", Boolean.class);
            methodsToAdd.add(remove);
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