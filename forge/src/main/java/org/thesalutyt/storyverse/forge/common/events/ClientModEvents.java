package org.thesalutyt.storyverse.forge.common.events;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.thesalutyt.storyverse.forge.common.entity.Entities;
import org.thesalutyt.storyverse.forge.common.entity.npc.NPCRender;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(Entities.NPC.get(), NPCRender::new);
    }
}
