package org.thesalutyt.storyverse.api.environment.js;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.WorldWrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class MobJS extends ScriptableObject implements EnvResource {
    WorldWrapper worldWrapper = new WorldWrapper();
    public static HashMap<String, MobController> controllers = new HashMap<>();
    public MobController create(String id, Double x, Double y, Double z, String type) {
        System.out.println("Started creating");
        MobController mob = new MobController(worldWrapper.pos(x, y, z), worldWrapper.toEntityType(type));
        System.out.println("Mob = " + mob);
        controllers.put(id, mob);
        System.out.println("Putting mob in base");
        return mob;
    }
    public MobController create(String id, Double x, Double y, Double z, NativeArray npcArgs) {
        Object[] args = npcArgs.toArray(new Object[0]);
        MobController mob = new MobController(worldWrapper.pos(x, y, z), worldWrapper.toEntityType("NPC"));
        controllers.put(id, mob);
        return mob;
    }
    public MobController getMob(String id) {
        return controllers.get(id);
    }

    public static void putIntoScope(Scriptable scope) {
        MobJS ef = new MobJS();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method create = MobJS.class.getMethod("create", String.class, Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(create);
            Method getMob = MobJS.class.getMethod("getMob", String.class);
            methodsToAdd.add(getMob);
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
    public String getClassName() {
        return "MobJS";
    }

    @Override
    public String getResourceId() {
        return "MobJS";
    }
}
