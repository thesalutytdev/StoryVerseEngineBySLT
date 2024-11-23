package org.thesalutyt.storyverse.common.entities.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.npc.NPCRender;
import org.thesalutyt.storyverse.common.entities.sit.SeatRenderer;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(Entities.NPC.get(), NPCRender::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.SEAT.get(), SeatRenderer::new);

//        Entities.renderMap.forEach((key, value) -> {
//            if (EntityAdder.entityHashMap.containsKey(key.get().getRegistryName())) {
//                LivingEntity entity = EntityAdder.entityHashMap.get(key.get().getRegistryName());
//                if (entity instanceof CustomMobEntity) {
//                    RenderingRegistry.registerEntityRenderingHandler(
//                            Entities.entityTypeHashMap.get(Objects.requireNonNull(key.get().getRegistryName()).getPath()),
//                            CustomMobEntityRender::new);
//                }
//            }
//        });
    }
}
