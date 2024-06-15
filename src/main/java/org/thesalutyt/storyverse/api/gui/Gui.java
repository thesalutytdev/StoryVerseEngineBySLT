package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.gui.RenderUtils;

import java.awt.*;

public class Gui extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/choice_gui.png");
    String heroSay = "Test Label";

    public Gui() {
        super(new StringTextComponent("dialog"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        assert this.minecraft != null;
        drawCenteredString(matrixStack, this.font, heroSay, this.width / 2, 85, 16777215);

        PlayerEntity playerEntity = this.minecraft.player;

        InventoryScreen.renderEntityInInventory(this.width / 2, this.height / 2, 90, (float)(this.width / 2) - mouseX, (float)(this.height / 2 - 50) - mouseY, playerEntity);
        this.minecraft.getTextureManager().bind(BACKGROUND);
        this.blit(matrixStack, this.width / 2 -100, this.height / 2 - 100, 256, 256, 227, 168);
        this.addButton(new Button(this.width / 2 - 100, this.height / 2 - 100, 200, 20,
                new StringTextComponent("Test Button"), (button) -> {
            Chat.sendAsEngine(button.getMessage().getString() + " pressed!");
        }));
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
