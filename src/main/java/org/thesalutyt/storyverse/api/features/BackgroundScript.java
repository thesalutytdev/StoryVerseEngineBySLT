package org.thesalutyt.storyverse.api.features;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class BackgroundScript extends ScriptableObject implements EnvResource, JSResource {
    public final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    public static void run(String script) {
        new Thread(() -> {
            Script.runScript(script);
        }).start();
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        BackgroundScript bs = new BackgroundScript();
        bs.setParentScope(scope);
        try {
            Method run = BackgroundScript.class.getMethod("run", String.class);
            methodsToAdd.add(run);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, bs);
            bs.put(m.getName(), bs, methodInstance);
        }
        scope.put("background", scope, bs);
    }

    @Override
    public String getResourceId() {
        return "BackgroundScript";
    }

    @Override
    public String getClassName() {
        return "BackgroundScript";
    }
}