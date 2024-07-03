package org.thesalutyt.storyverse.api.gui.script;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

import java.util.ArrayList;
import java.util.List;

public class CustomGui extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(StoryVerse.MOD_ID, "textures/entity/npc/npc.png");
    private final List<Button> buttons;
    public static CustomGui createGui() {
        return new CustomGui();
    }

    protected CustomGui() {
        super(new StringTextComponent("Custom GUI"));
        this.buttons = new ArrayList<>();
    }

    public void addButton(String text, int x, int y, Button.IPressable onPress) {
        this.buttons.add(new Button(x, y, 100, 20, new StringTextComponent(text), onPress));
    }

    @Override
    protected void init() {
        super.init();
        int y = 20;
        for (Button button : buttons) {
            button.x = (this.width - 100) / 2;
            button.y = y;
            this.addButton(button);
            y += 25;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, 10, 0xFFFFFF);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
