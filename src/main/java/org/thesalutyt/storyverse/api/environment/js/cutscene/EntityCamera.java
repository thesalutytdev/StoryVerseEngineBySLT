package org.thesalutyt.storyverse.api.environment.js.cutscene;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EntityCamera extends ScriptableObject implements EnvResource, JSResource {
    public static void setCameraEntity(String player, String mobId) {
        Server.getPlayerByName(player).setCamera(MobJS.controllers.get(mobId).getMobEntity());
    }

    public static void resetCamera(String player) {
        Server.getPlayerByName(player).setCamera(null);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        EntityCamera ef = new EntityCamera();
        ef.setParentScope(scope);

        try {
            Method setCamera = EntityCamera.class.getMethod("setCameraEntity", String.class, String.class);
            methodsToAdd.add(setCamera);
            Method resetCamera = EntityCamera.class.getMethod("resetCamera", String.class);
            methodsToAdd.add(resetCamera);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("camera", scope, ef);
    }

    @Override
    public String getClassName() {
        return "EntityCamera";
    }

    @Override
    public String getResourceId() {
        return "EntityCamera";
    }
}
