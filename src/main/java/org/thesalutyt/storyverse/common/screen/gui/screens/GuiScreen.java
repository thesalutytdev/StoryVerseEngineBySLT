package org.thesalutyt.storyverse.common.screen.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.screen.gui.containers.GuiContainer;

public class GuiScreen extends ContainerScreen<GuiContainer> {
    private final ResourceLocation GUI_LEFT;
    private final ResourceLocation GUI_RIGHT;
    public GuiScreen(GuiContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        super(container, inventory, textComponent);
        this.GUI_LEFT = new ResourceLocation(StoryVerse.MOD_ID, ":textures/gui/choicegui_left.png");
        this.GUI_RIGHT = new ResourceLocation(StoryVerse.MOD_ID, ":textures/gui/choicegui_right.png");
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        int width = this.width + this.getXSize() / 2;
        int height = this.height + this.getYSize() / 2;
        this.renderBg(p_230450_1_, p_230450_2_, p_230450_3_, p_230450_4_);
    }
}
