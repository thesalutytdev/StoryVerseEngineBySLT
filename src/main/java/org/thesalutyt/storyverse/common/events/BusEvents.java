package org.thesalutyt.storyverse.common.events;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.thesalutyt.storyverse.common.entities.npcNOTWORK.NPCEntity;

@Mod.EventBusSubscriber(
        modid = "storyverse",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class BusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
            event.put(NPC.get(), NPCEntity.createMobAttributes().build());
    }
}
