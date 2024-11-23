package org.thesalutyt.storyverse.common.effects.adder;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.common.effects.ModEffects;
import org.thesalutyt.storyverse.common.elements.ICustomElement;
import org.thesalutyt.storyverse.common.events.ModEvents;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

public class CustomEffect extends Effect implements ICustomElement {
    protected String name;
    protected EffectType type;
    protected int color;
    protected EffectAdder effectAdderReference;

    public CustomEffect(EffectType type, Integer color, String name) {
        super(type, color);
        this.name = name;
        this.type = type;
        this.color = color;
        ModEffects.addEffect(this);
    }

    public CustomEffect(EffectAdder adderInstance) {
        super(adderInstance.getEffectType(), adderInstance.getColor());
        this.name = adderInstance.getName();
        this.type = adderInstance.getEffectType();
        this.color = adderInstance.getColor();
        this.effectAdderReference = adderInstance;
        ModEffects.addEffect(this);
    }

    public CustomEffect create(String name, Integer color, String type) {
        return new CustomEffect(toEffectType(type), color, name);
    }

    public void onTick(LivingEntity entity, int level) {
        if (effectAdderReference != null) {
            effectAdderReference.onTick(entity, level);
        }
    }

    public static EffectType toEffectType(String type) {
        switch (type.toUpperCase()) {
            case "HARM":
            case "BAD":
            case "HARMFUL":
                return EffectType.HARMFUL;
            case "GOOD":
            case "BUFF":
            case "BENEFICIAL":
                return EffectType.BENEFICIAL;
            case "NEUTRAL":
            default:
                return EffectType.NEUTRAL;
        }
    }

    public EffectType getEffectType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void applyEffectTick(LivingEntity entity, int level) {
        onTick(entity, level);
    }

    public int getEffectColor() {
        return color;
    }

    @Override
    public Object getDefaultElement() {
        return Effect.class;
    }

    @Override
    public String getClassName() {
        return "CustomEffect";
    }

    @Override
    public boolean userReachable() {
        return false;
    }

    @Override
    public String getResourceId() {
        return "CustomEffect";
    }

    @Override
    public String toString() {
        return "CustomEffect{" +
                "name='" + name + '\'' +
                "type=" + type +
                "color=" + color + "}";
    }
}
