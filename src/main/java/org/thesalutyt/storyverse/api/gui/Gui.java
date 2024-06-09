package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gui extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/dialog_gui.png");
    String heroSay;
    List<Button> buttons = new ArrayList<>();
    int y = 50;
    //byte[] instance;

    public Gui(String heroSay) {
        super(new StringTextComponent("dialog"));
        this.heroSay = heroSay;
        //this.instance = instance;
    }

    @Override
    protected void init() {
            Button button = this.addButton(
                    new Button(this.width / 2 - 75, this.height /
                            4 + y, 150, 20,
                    new StringTextComponent("test"), (p_213021_1_) -> {
                    this.minecraft.setScreen(new Gui("label"));
            }));
            button.setFGColor(0x4a04b3);
            button.setAlpha(0.0f);
            buttons.add(button);
            y+=35;
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        this.minecraft.getTextureManager().bind(BACKGROUND);
        this.blit(matrixStack, this.width / 2 -100, this.height / 2 - 100, 256, 256, 227, 168);
        drawCenteredString(matrixStack, this.font, heroSay, this.width / 2, 85, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
