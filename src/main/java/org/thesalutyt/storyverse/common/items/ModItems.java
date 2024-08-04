package org.thesalutyt.storyverse.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;
import org.thesalutyt.storyverse.common.items.adder.UsableItem;

import java.util.ArrayList;

public class ModItems {
    public static ArrayList<RegistryObject<Item>> items = new ArrayList<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            StoryVerse.MOD_ID);
    public static final RegistryObject<Item> NPC_DELETER = ITEMS.register("npc_deleter", NpcDeleter::new);
    public static final RegistryObject<Item> ENTITY_DELETER = ITEMS.register("entity_deleter", EntityDeleter::new);
    public static final RegistryObject<Item> NPC_SPAWNER = ITEMS.register("npc_spawner", NpcSpawner::new);
    public static final RegistryObject<Item> NPC_SETTINGS = ITEMS.register("npc_settings", NpcSettings::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    public static void addItem(CustomItem item) {
        ModItems.ITEMS.register(item.name,
                () -> item.item);
    }

    public static void addItem(UsableItem item) {
        ModItems.ITEMS.register(item.name,
                () -> item);
    }
}
