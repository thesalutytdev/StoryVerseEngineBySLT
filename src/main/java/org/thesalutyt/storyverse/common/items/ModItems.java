package org.thesalutyt.storyverse.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;

import java.util.ArrayList;

public class ModItems {
    public static ArrayList<RegistryObject<Item>> items = new ArrayList<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            StoryVerse.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    public static void addItem(CustomItem item) {
        ModItems.ITEMS.register(item.name,
                () -> item.item);
    }
}
