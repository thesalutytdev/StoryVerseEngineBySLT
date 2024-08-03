package org.thesalutyt.storyverse.api.screen.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class GuiItem {
    public ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    public ItemStack stack;
    public Double x;
    public Double y;

    public GuiItem(ItemStack item, Double x, Double y) {
        this.stack = item;
        this.x = x;
        this.y = y;
    }
}
