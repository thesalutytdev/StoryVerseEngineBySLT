package org.thesalutyt.storyverse.api.environment.js;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.action.ActionPacket;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.common.events.ModEvents;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class ScriptProperties extends ScriptableObject implements EnvResource, JSResource {
    public static String worldStarterScript;
    public static Boolean ran = false;
    public static void onWorldStart(String script_name, Boolean is) {
        if (!is) {
            return;
        } else {
            worldStarterScript = script_name;
        }
    }

    public static void run() {
        Script.runScript(worldStarterScript);
        ran = true;
    }

    public static void resetWorldStart() {
        worldStarterScript = null;
    }

    public static void putIntoScope(Scriptable scope) {
        ScriptProperties ef = new ScriptProperties();
        ArrayList<Method> methodsToAdd = new ArrayList<>();
        ef.setParentScope(scope);
        try {
            Method onWorldJoined = ScriptProperties.class.getMethod("onWorldStart", String.class, Boolean.class);
            methodsToAdd.add(onWorldJoined);
            Method resetWorldStart = ScriptProperties.class.getMethod("resetWorldStart");
            methodsToAdd.add(resetWorldStart);
            Method run = ScriptProperties.class.getMethod("run");
            methodsToAdd.add(run);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("properties", scope, ef);
    }
    @Override
    public String getClassName() {
        return "ScriptProperties";
    }

    @Override
    public String getResourceId() {
        return "ScriptProperties";
    }
}
