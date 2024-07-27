package org.thesalutyt.storyverse.common.effects.adder;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import org.thesalutyt.storyverse.common.effects.ModEffects;
import org.thesalutyt.storyverse.common.elements.ICustomElement;

public class CustomEffect extends Effect implements ICustomElement {
    public String name;

    public CustomEffect(EffectType type, Integer color, String name) {
        super(type, color);
        this.name = name;
        setRegistryName(name);
        ModEffects.addEffect(this);
    }

    public CustomEffect create(String name, Integer color, String type) {
        return new CustomEffect(toEffectType(type), color, name);
    }

    public static EffectType toEffectType(String type) {
        switch (type.toUpperCase()) {
            case "HARM":
            case "BAD":
            case "HARMFUL":
                return EffectType.HARMFUL;
            default:
            case "NEUTRAL":
                return EffectType.NEUTRAL;
            case "GOOD":
            case "BUFF":
            case "BENEFICIAL":
                return EffectType.BENEFICIAL;
        }
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
}
