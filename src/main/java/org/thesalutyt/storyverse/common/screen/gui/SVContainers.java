package org.thesalutyt.storyverse.common.screen.gui;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.screen.gui.containers.GuiContainer;

public class SVContainers {
    private static final String MOD_ID = StoryVerse.MOD_ID;
    public static DeferredRegister<ContainerType<?>> CONTAINERS;
    public static RegistryObject<ContainerType<GuiContainer>> GUI_CONTAINER;

    public SVContainers() {}

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
        GUI_CONTAINER = CONTAINERS.register(SVEngine.GUI_CONTAINER_NAME, () -> {
            return IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new GuiContainer(windowId, world, pos, inv, inv.player);
            }));
        });
    }
}
