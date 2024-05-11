package org.thesalutyt.storyverse.common.screen.gui.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.thesalutyt.storyverse.common.screen.gui.SVContainers;

public class GuiContainer extends Container {
    public final PlayerEntity playerEntity;
    private final PlayerInventory playerInventory;

    public GuiContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super((ContainerType) SVContainers.GUI_CONTAINER.get(), windowId);
        this.playerEntity = player;
        this.playerInventory = playerInventory;
    }

    public static void open() {

    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }
}
