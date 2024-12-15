package org.thesalutyt.storyverse.common.entities;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.entities.adder.CustomEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomAnimalEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomFlyingEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomMobEntity;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;
import org.thesalutyt.storyverse.common.entities.sit.SeatEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.ArrayList;
import java.util.HashMap;

public class Entities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, StoryVerse.MOD_ID);
    public static final RegistryObject<EntityType<NPCEntity>> NPC =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.of(NPCEntity::new, EntityClassification.CREATURE)
                            .build(new ResourceLocation(StoryVerse.MOD_ID, "npc").toString()));
    public static final RegistryObject<EntityType<SeatEntity>> SEAT =
            ENTITY_TYPES.register("seat", () -> EntityType.Builder.of(SeatEntity::new, EntityClassification.MISC)
                    .sized(0.5f, 0.5f)
                    .build(new ResourceLocation(StoryVerse.MOD_ID, "seat").toString()));
    public static final RegistryObject<EntityType<CustomAnimalEntity>> CUSTOM_ANIMAL = ENTITY_TYPES.register("custom_animal",
            () -> EntityType.Builder.of(CustomAnimalEntity::new, EntityClassification.CREATURE)
                    .build(new ResourceLocation(StoryVerse.MOD_ID, "custom_animal").toString()));
    public static final RegistryObject<EntityType<CustomFlyingEntity>> CUSTOM_FLYING = ENTITY_TYPES.register("custom_flying",
            () -> EntityType.Builder.of(CustomFlyingEntity::new, EntityClassification.CREATURE)
                    .build(new ResourceLocation(StoryVerse.MOD_ID, "custom_flying").toString()));
    public static final RegistryObject<EntityType<CustomMobEntity>> CUSTOM_MOB = ENTITY_TYPES.register("custom_mob",
            () -> EntityType.Builder.of(CustomMobEntity::new, EntityClassification.CREATURE)
                    .build(new ResourceLocation(StoryVerse.MOD_ID, "custom_mob").toString()));

    public static ArrayList<RegistryObject<EntityType<?>>> entityList = new ArrayList<>();
    public static HashMap<String, RegistryObject<EntityType<?>>> entityMap = new HashMap<>();
    public static HashMap<String, EntityType<?>> entityTypeHashMap = new HashMap<>();
    public static HashMap<RegistryObject<EntityType<?>>, GeoEntityRenderer<?>> renderMap = new HashMap<>();

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        for (RegistryObject<EntityType<?>> entity : entityList) {
            eventBus.register(entity);
        }
    }
}