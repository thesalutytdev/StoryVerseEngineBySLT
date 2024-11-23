package org.thesalutyt.storyverse.api.special;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.environment.resource.wrappers.FadeScreenWrapper;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.common.specific.networking.Networking;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.FadeScreenPacket;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FadeScreen extends ScriptableObject implements EnvResource, JSResource {
    public static void text(String player, String text, Integer color, Integer time, Integer input, Integer output) {
        FadeScreenWrapper data = new FadeScreenWrapper(text, time, input, output, color);
        FadeScreenPacket packet = new FadeScreenPacket(data.getId());

        if (player.equals("$every")) {
            for (ServerPlayerEntity pl : Server.getPlayers()) {
                Networking.sendToPlayer(packet, pl);
            }
            return;
        }

        Networking.sendToPlayer(packet, Server.getPlayerByName(player));
    }

    public static void fade(String player, Integer color, Integer time, Integer input, Integer output) {
        FadeScreenWrapper data = new FadeScreenWrapper(time, input, output, color);
        FadeScreenPacket packet = new FadeScreenPacket(data.getId());

        if (player.equals("$every")) {
            for (ServerPlayerEntity pl : Server.getPlayers()) {
                Networking.sendToPlayer(packet, pl);
            }
            return;
        }

        Networking.sendToPlayer(packet, Server.getPlayerByName(player));
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        FadeScreen ef = new FadeScreen();
        ef.setParentScope(scope);
        try {
            Method fade = FadeScreen.class.getMethod("text", String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class);
            methodsToAdd.add(fade);
            Method fade2 = FadeScreen.class.getMethod("fade", String.class, Integer.class, Integer.class, Integer.class, Integer.class);
            methodsToAdd.add(fade2);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("fade", scope, ef);
    }

    @Override
    public String getClassName() {
        return "FadeScreen";
    }

    @Override
    public String getResourceId() {
        return "FadeScreen";
    }
}
