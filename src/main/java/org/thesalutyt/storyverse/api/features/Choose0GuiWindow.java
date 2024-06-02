package org.thesalutyt.storyverse.api.features;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class Choose0GuiWindow extends ContainerScreen<Choose0Gui.GuiContainerMod> {
    private World world;
    private int x, y, z;
    private PlayerEntity entity;
    private final static HashMap guistate = Choose0Gui.guistate;

    public Choose0GuiWindow(Choose0Gui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    private static final ResourceLocation texture = new ResourceLocation("storyverse:textures/choose_0.png");

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

    }

//    @Override
//    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int gx, int gy) {
//        RenderSystem.color4f(1, 1, 1, 1);
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        Minecraft.getInstance().getTextureManager().bind(texture);
//        int k = (this.width - this.imageWidth) / 2;
//        int l = (this.height - this.imageHeight) / 2;
//
//        this.blit(ms, k, l, 0, 0, this.imageWidth, this.imageHeight, this.imageHeight, this.imageWidth);
//        Minecraft.getInstance().getTextureManager().getTexture(new ResourceLocation("storyverse:textures/choise0small.png"));
//        this.blit(ms, this.getGuiLeft() + 0, this.getGuiTop() + -1, 0, 0, 176, 166, 176, 166);
//
//        RenderSystem.disableBlend();
//    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    public void tick() {
        super.tick();
    }

//    @Override
//    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
//        this.font.draw(ms, "\u00A7r\u0422\u044B \u043F\u043E\u0439\u0434\u0451\u0448\u044C \u0441\u043E \u043C\u043D\u043E\u0439?", 16, 25,
//                -12829636);
//    }

    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.addButton(new Button(this.getGuiLeft() + 41, this.getGuiTop() + 67, 35, 20, new StringTextComponent("Да"), e -> {
            if (true) {
                Choose0Gui.handleButtonAction(entity, 0, x, y, z);
            }
        }));
        this.addButton(new Button(this.getGuiLeft() + 40, this.getGuiTop() + 104, 40, 20, new StringTextComponent("Нет"), e -> {
            if (true) {
                Choose0Gui.handleButtonAction(entity, 1, x, y, z);
            }
        }));
        this.addButton(new Button(this.getGuiLeft() + 112, this.getGuiTop() + 132, 51, 20, new StringTextComponent("§4§lx"), e -> {
            if (true) {
                Choose0Gui.handleButtonAction(entity, 2, x, y, z);
            }
        }));
    }
}
