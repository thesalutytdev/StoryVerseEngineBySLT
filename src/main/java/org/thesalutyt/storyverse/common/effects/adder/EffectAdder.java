package org.thesalutyt.storyverse.common.effects.adder;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.MobController;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EffectAdder extends ScriptableObject implements EnvResource, JSResource {
    private CustomEffect effect;
    private int color;
    private EffectType type;
    private String name;
    private ArrayList<BaseFunction> onEffectTick = new ArrayList<>();

    public EffectAdder create(String name, Integer color, String type) {
        this.color = color;
        this.type = toEffectType(type);
        this.name = name;
        this.effect = new CustomEffect(this);

        return this;
    }

    public EffectAdder setOnTick(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            ArrayList<BaseFunction> onTick = new ArrayList<>();
            onTick.add(function);
            onEffectTick = onTick;
        });
        return this;
    }

    public void onTick(LivingEntity entity, int level) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction f : onEffectTick) {
                f.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{new MobController(entity), level});
            }
        });
    }

    public CustomEffect getEffect() {
        return effect;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public EffectType getEffectType() {
        return type;
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
            Method setOnTick = EffectAdder.class.getMethod("setOnTick", BaseFunction.class);
            methodsToAdd.add(setOnTick);
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
