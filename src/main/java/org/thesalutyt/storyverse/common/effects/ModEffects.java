package org.thesalutyt.storyverse.common.effects;

import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.effects.adder.CustomEffect;

public class ModEffects {
    public static DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, StoryVerse.MOD_ID);

    public static void addEffect(CustomEffect effect) {
        EFFECTS.register(effect.name, () -> effect);
    }
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
