package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.gui.widgets.TextAreaWidget;
import org.thesalutyt.storyverse.utils.RenderUtils;

import java.awt.*;
import java.io.IOException;

public class ScriptGui extends Screen {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 192;
    private TextAreaWidget textArea;

    public ScriptGui() {
        super(new TranslationTextComponent("gui.creator.title"));
    }

    @Override
    protected void init() {
        this.textArea = new TextAreaWidget(this.width / 2 - WIDTH / 2, this.height / 2 - HEIGHT / 2, WIDTH, HEIGHT);
        this.children.add(this.textArea);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.textArea.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderUtils.drawCircleRect(590, 130, 610, 150, 4, new Color(22, 148, 0).getRGB());
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        this.textArea.charTyped(codePoint);
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            boolean key = this.textArea.keyPressed(keyCode, scanCode, modifiers);
            if(key) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.textArea.mouseScrolled(mouseX, mouseY, delta)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isInside(mouseX, mouseY, 590, 130, 610, 150) && button == 0) {
            Script.runScript("script.js");
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean isInside(double mouseX, double mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}