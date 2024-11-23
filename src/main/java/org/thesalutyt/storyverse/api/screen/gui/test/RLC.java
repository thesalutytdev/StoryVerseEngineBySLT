package org.thesalutyt.storyverse.api.screen.gui.test;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGui;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGuiProperties;
import org.thesalutyt.storyverse.api.screen.gui.elements.TextInputWidget;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class RLC extends IGui {
    private final TextInputWidget textInput = new TextInputWidget(50, 50, 200, 20);

    public RLC() {
        super(new IGuiProperties().renderBackground(false).closeOnEsc(true).name("rlc")
                .width(512)
                .height(256));
    }

    @Override
    public void init() {
        super.init();
        this.width = 512;
        this.height = 256;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        textInput.render(matrices, mouseX, mouseY, delta);


    }

    @Override
    public void tick() {
        super.tick();
        textInput.tick();
    }

    @Override
    public void removed() {
        super.removed();
        textInput.removed();
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        super.charTyped(chr, keyCode);
        return textInput.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        super.keyPressed(key, scanCode, modifiers);
        return textInput.keyPressed(key, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        return textInput.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        return textInput.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        super.mouseScrolled(mouseX, mouseY, amount);
        return textInput.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        super.isMouseOver(mouseX, mouseY);
        return textInput.isMouseOver(mouseX, mouseY);
    }
}
