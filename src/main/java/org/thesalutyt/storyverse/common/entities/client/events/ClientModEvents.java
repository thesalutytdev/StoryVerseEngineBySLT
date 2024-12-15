package org.thesalutyt.storyverse.common.entities.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomMobEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.renderers.CustomAnimalEntityRender;
import org.thesalutyt.storyverse.common.entities.adder.essential.renderers.CustomFlyingEntityRender;
import org.thesalutyt.storyverse.common.entities.adder.essential.renderers.CustomMobEntityRender;
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

        RenderingRegistry.registerEntityRenderingHandler(Entities.CUSTOM_ANIMAL.get(), CustomAnimalEntityRender::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.CUSTOM_FLYING.get(), CustomFlyingEntityRender::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.CUSTOM_MOB.get(), CustomMobEntityRender::new);
    }
}
