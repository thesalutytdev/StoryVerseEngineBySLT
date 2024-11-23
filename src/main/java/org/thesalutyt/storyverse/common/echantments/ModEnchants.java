package org.thesalutyt.storyverse.common.echantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;

import java.util.ArrayList;

public class ModEnchants {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, StoryVerse.MOD_ID);
    public static final ArrayList<RegistryObject<Enchantment>> customEnchants = new ArrayList<>();

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }

    public static void addEnchantment(String name, Enchantment enchantment) {
        customEnchants.add(ENCHANTMENTS.register(name, () -> enchantment));
    }
}
