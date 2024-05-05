package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.network.PacketDistributor;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.special.FadeScreenPacket;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;
import org.thesalutyt.storyverse.common.specific.networking.Networking;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class Player extends ScriptableObject implements EnvResource{
    private static ServerPlayerEntity player;
    public LegacyEventManager eventManager;

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
    public static void giveXPLevels(int levels) {
        player.giveExperienceLevels(levels);
    }

    @Documentate(
            desc = "Returns player's experience points"
    )
    public static void giveXPPoints(int points) {
        player.giveExperiencePoints(points);
    }

    @Documentate(
            desc = "Sets player non-killable"
    )
    public static void setInvulnerable(boolean is) {
        player.setInvulnerable(is);
    }

    @Documentate(
            desc = "Sets player invisible"
    )
    public static void setInvisible(boolean is) {
        player.setInvisible(is);
    }

    @Documentate(
            desc = "Sends player a message"
    )
    public void sendMessage(String text) {
        Chat.sendMessage(player, text);
    }

    @Documentate(
            desc = "Sends player some message with sender name and text"
    )
    public static void sendNamed(String name, String text) {
        Chat.sendNamed(player, name, text);
    }

    @Documentate(
            desc = "Returns player's name"
    )
    public static void setHeadRotation(float pitch, float yaw) {
        player.yHeadRot = yaw;
        player.xRot = pitch;
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
    public double[] getPosition() {
        return new double[]{this.getX(), this.getY(), this.getZ()};
    }
    public BlockPos getBlockPosition() {return player.blockPosition();}

    @Documentate(
            desc = "Sets player's position"
    )
    public static void setPosition(double[] pos) {
        player.moveTo(pos[0], pos[1], pos[2]);
    }

    public static void setPosition(double x, double y, double z) {
        setPosition(new double[]{x, y, z});
    }

    @Documentate(
            desc = "Teleports player"
    )
    public static void teleportTo(double x, double y, double z) {
        setPosition(new double[]{x, y, z});
    }

    @Documentate(
            desc = "Returns player's entity"
    )
    public static Object getEntity() {
        return player.getEntity();
    }

    @Documentate(
            desc = "Makes player ride some entity"
    )
    public static void startRiding(Entity entity) {
        player.getEntity().startRiding(entity);
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
    public static void setX(double x) {
        setPosition(x, getY(), getZ());
    }

    @Documentate(
            desc = "Sets player's Y position"
    )
    public static void setY(double y) {
        setPosition(getX(), y, getZ());
    }

    @Documentate(
            desc = "Sets player's Z position"
    )
    public static void setZ(double z) {
        setPosition(getX(), getY(), z);
    }

    @Documentate(
            desc = "Sets player's game mode"
    )
    public static void setGameMode(int mode) {
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
        if (mode == "survival") {
            setGameMode(0);
        } else if (mode == "creative") {
            setGameMode(1);
        } else if (mode == "adventure") {
            setGameMode(2);
        } else if (mode == "spectator") {
            setGameMode(3);
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
    public static void canBuild(boolean method) {
        player.abilities.mayBuild = method;
    }

    public static void canFly(boolean method) {
        player.abilities.flying = method;
    }

    public static void instantBuild(boolean method) {
        player.abilities.instabuild = method;
    }

    public static void canDie(boolean method) {
        player.abilities.invulnerable = method;
    }

    @Documentate(
            desc = "Sets player's speed"
    )
    public static void setWalkSpeed(float walkSpeed) {
        player.abilities.setWalkingSpeed(walkSpeed);
    }

    @Documentate(
            desc = "Sets player's fly speed"
    )
    public static void setFlyingSpeed(float flyingSpeed) {
        player.abilities.setFlyingSpeed(flyingSpeed);
    }

    @Documentate(
            desc = "Kills player"
    )
    public static void kill() {
        player.kill();
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
    public static void hurt(float damage, String damageSource) {
        player.hurt(new DamageSource(damageSource), damage);
    }
    public static void hurt(float damage) {
        hurt(damage, "storyverse:script");
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
            Method giveXPP = Player.class.getMethod("giveXPPoints", int.class);
            methodsToAdd.add(giveXPP);
            Method giveXPL = Player.class.getMethod("giveXPLevels", int.class);
            methodsToAdd.add(giveXPL);
            Method setHeadRot = Player.class.getMethod("setHeadRotation", float.class, float.class);
            methodsToAdd.add(setHeadRot);
            Method setPosition = Player.class.getMethod("setPosition", double[].class);
            methodsToAdd.add(setPosition);
            Method setPosD = Player.class.getMethod("setPosition", double.class, double.class, double.class);
            methodsToAdd.add(setPosD);
            Method setX = Player.class.getMethod("setX", double.class);
            methodsToAdd.add(setX);
            Method setY = Player.class.getMethod("setY", double.class);
            methodsToAdd.add(setY);
            Method setZ = Player.class.getMethod("setZ", double.class);
            methodsToAdd.add(setZ);
            Method teleport = Player.class.getMethod("teleportTo", double.class, double.class, double.class);
            methodsToAdd.add(teleport);
            Method hurt = Player.class.getMethod("hurt", float.class);
            methodsToAdd.add(hurt);
            Method hurt_ds = Player.class.getMethod("hurt", float.class, String.class);
            methodsToAdd.add(hurt_ds);
            Method setGameMode = Player.class.getMethod("setGameMode", String.class);
            methodsToAdd.add(setGameMode);
            Method setGMInt = Player.class.getMethod("setGameMode", int.class);
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
            Method canFly = Player.class.getMethod("canFly", boolean.class);
            methodsToAdd.add(canFly);
            Method canBuild = Player.class.getMethod("canBuild", boolean.class);
            methodsToAdd.add(canBuild);
            Method canDie = Player.class.getMethod("canDie", boolean.class);
            methodsToAdd.add(canDie);
            Method setFlySpeed = Player.class.getMethod("setFlyingSpeed", float.class);
            methodsToAdd.add(setFlySpeed);
            Method setWalkSpeed = Player.class.getMethod("setWalkSpeed", float.class);
            methodsToAdd.add(setWalkSpeed);
            Method startRiding = Player.class.getMethod("startRiding", Entity.class);
            methodsToAdd.add(startRiding);
            Method stopRiding = Player.class.getMethod("stopRiding");
            methodsToAdd.add(stopRiding);
            Method setInvisible = Player.class.getMethod("setInvisible", boolean.class);
            methodsToAdd.add(setInvisible);
            Method setInvulnerable = Player.class.getMethod("setInvulnerable", boolean.class);
            methodsToAdd.add(setInvulnerable);
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