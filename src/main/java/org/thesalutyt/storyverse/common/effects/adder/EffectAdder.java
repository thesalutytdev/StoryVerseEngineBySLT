package org.thesalutyt.storyverse.common.effects.adder;

import net.minecraft.potion.EffectType;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EffectAdder extends ScriptableObject implements EnvResource, JSResource {
    public CustomEffect create(String name, Integer color, String type) {
        return new CustomEffect(CustomEffect.toEffectType(type), color, name);
    }

    public static EffectType toEffectType(String type) {
        return CustomEffect.toEffectType(type);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        EffectAdder ef = new EffectAdder();
        ef.setParentScope(scope);

        try {
            Method create = EffectAdder.class.getMethod("create", String.class, Integer.class, String.class);
            methodsToAdd.add(create);
            Method toEffectType = EffectAdder.class.getMethod("toEffectType", String.class);
            methodsToAdd.add(toEffectType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("effect", scope, ef);
    }

    @Override
    public String getClassName() {
        return "EffectAdder";
    }

    @Override
    public String getResourceId() {
        return "EffectAdder";
    }
}
