//package org.thesalutyt.storyverse.common.entities;
//
//import net.minecraft.entity.EntityClassification;
//import net.minecraft.entity.EntityType;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.RegistryObject;
////import net.minecraft.world.entity.MobCategory;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import org.thesalutyt.storyverse.StoryVerse;
//import org.thesalutyt.storyverse.common.entities.client.NPCEntity;
//
//public class Register {
//    //private EntityInit() {}
//    public static final DeferredRegister<EntityType<?>> ENTITES = DeferredRegister.create(ForgeRegistries.ENTITIES,
//            StoryVerse.MOD_ID);
//    public static final RegistryObject<EntityType<NPCEntity>> EXAMPLE_ENTITY = ENTITES.register("example",
//            () -> EntityType.Builder.of(NPCEntity::new, /*MobCategory.CREATURE)*/ EntityClassification.CREATURE)
//                    .canSpawnFarFromPlayer()
//                    .sized(0.8f, 0.6f)
//                    .build(new ResourceLocation(StoryVerse.MOD_ID, "npc").toString()));
//}
