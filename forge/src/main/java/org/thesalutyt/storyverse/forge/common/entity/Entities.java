package org.thesalutyt.storyverse.forge.common.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.forge.common.entity.npc.NPCEntity;

public class Entities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, StoryVerse.MOD_ID);
    public static final RegistryObject<EntityType<NPCEntity>> NPC =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.of(NPCEntity::new, MobCategory.CREATURE)
                            .build(new ResourceLocation(StoryVerse.MOD_ID, "npc").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
