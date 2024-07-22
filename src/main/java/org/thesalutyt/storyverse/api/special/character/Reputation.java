package org.thesalutyt.storyverse.api.special.character;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.MobController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Reputation extends ScriptableObject implements EnvResource, JSResource {
    public static HashMap<MobController, Reputation> repMap = new HashMap<>();
    public MobController character;
    public String mobId;
    public Integer reputation = 0;
    public static ArrayList<MobController> characters = new ArrayList<>();

    public Reputation(String id) {
        MobController mob = MobJS.getMob(id);
        character = mob;
        repMap.put(mob, this);
        characters.add(mob);
        mobId = id;
    }

    public Reputation() {}

    public Reputation link(String id) {
        MobController mob = MobJS.getMob(id);
        character = mob;
        repMap.put(mob, this);
        characters.add(mob);
        return this;
    }

    public Integer getReputation(String id) {
        MobController mob = MobJS.getMob(id);
        if (repMap.containsKey(mob)) {
            return repMap.get(mob).reputation;
        }
        return 0;
    }

    public Integer setReputation(String id, Integer rep) {
        MobController mob = MobJS.getMob(id);
        if (repMap.containsKey(mob)) {
            repMap.get(mob).reputation = rep;
            return repMap.get(mob).reputation;
        }
        return reputation;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        Reputation rp = new Reputation();
        rp.setParentScope(scope);

        try {
            Method get = Reputation.class.getMethod("getReputation", String.class);
            methodsToAdd.add(get);
            Method set = Reputation.class.getMethod("setReputation", String.class, Integer.class);
            methodsToAdd.add(set);
            Method link = Reputation.class.getMethod("link", String.class);
            methodsToAdd.add(link);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, rp);
            rp.put(m.getName(), rp, methodInstance);
        }
        scope.put("reputation", scope, rp);
    }

    @Override
    public String getClassName() {
        return "Reputation";
    }

    @Override
    public String getResourceId() {
        return "Reputation";
    }
}
