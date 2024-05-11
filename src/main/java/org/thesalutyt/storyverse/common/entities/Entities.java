package org.thesalutyt.storyverse.common.entities;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

public class Entities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static final RegistryObject<EntityType<NPCEntity>> NPC;

    public Entities() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "storyverse");
        NPC = ENTITY_TYPES.register("npc", () -> {
            return Builder.of(NPCEntity::new, EntityClassification.AMBIENT)
                    .sized(0.8F, 1.85F)
                    .build((new ResourceLocation("storyverse", "npc"))
                            .toString());
        });
    }
}