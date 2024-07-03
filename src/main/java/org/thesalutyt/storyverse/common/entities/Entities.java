package org.thesalutyt.storyverse.common.entities;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

public class Entities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, StoryVerse.MOD_ID);
    public static final RegistryObject<EntityType<NPCEntity>> NPC =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.of(NPCEntity::new, EntityClassification.CREATURE)
                            .build(new ResourceLocation(StoryVerse.MOD_ID, "npc").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}