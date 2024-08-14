package org.thesalutyt.storyverse.api.screen.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class TextArea extends Widget {
    public TextFieldWidget textField;
    public String content = "";

    public TextArea(Integer x, Integer y, Integer width, Integer height) {
        this(x, y, width, height, " ");
    }
    public TextArea(Integer x, Integer y, Integer width, Integer height, String message) {
        super(x, y, width, height, new StringTextComponent(" "));
        this.content = message;
        this.textField = new TextFieldWidget(Minecraft.getInstance().font,
                this.x, this.y, this.width, this.height, new StringTextComponent(this.content));
        this.textField.setBordered(false);
        this.textField.setMaxLength(50); // Максимальная длина текста
        this.textField.setVisible(true); // Видимость текстового поля
        this.textField.setTextColor(0xFFFFFF);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.textField.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            this.textField.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.textField.isFocused() && this.textField.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.textField.isFocused() && this.textField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
