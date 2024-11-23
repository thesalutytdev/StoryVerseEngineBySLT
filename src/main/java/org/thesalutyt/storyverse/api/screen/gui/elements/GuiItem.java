package org.thesalutyt.storyverse.api.screen.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
