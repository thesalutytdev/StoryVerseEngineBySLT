package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.gui.widgets.TextAreaWidget;
import org.thesalutyt.storyverse.utils.RenderUtils;
import sun.font.FontUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ScriptGui extends Screen {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 192;
    private TextAreaWidget textArea;
    private final FontRenderer fontRenderer = Minecraft.getInstance().font;

    public ScriptGui() {
        super(new TranslationTextComponent("gui.custom_text_area.title"));
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
        File directory = new File(SVEngine.SCRIPTS_PATH);
        int[] offset = {0};
        renderDirectory(matrixStack, directory, 0, offset);
    }

    private void renderDirectory(MatrixStack matrixStack, File directory, int level, int[] offset) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("    ");
            }
            if (file.isDirectory()) {
                this.fontRenderer.draw(matrixStack, indent + file.getName(), 23, 12 + offset[0], new Color(255, 255, 255).getRGB());
                RenderUtils.drawImage(matrixStack, new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/script/folder_dark.png"), 10 + this.fontRenderer.width(String.valueOf(indent)), 10 + offset[0], 10, 10);
                offset[0] += 10;
                renderDirectory(matrixStack, file, level + 1, offset);
            } else {
                if(file.getName().endsWith(".txt")) {
                    RenderUtils.drawImage(matrixStack, new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/script/text.png"), 10 + this.fontRenderer.width(String.valueOf(indent)), 11 + offset[0], 10, 10);
                } else if(file.getName().endsWith(".json")) {
                    RenderUtils.drawImage(matrixStack, new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/script/json.png"), 10 + this.fontRenderer.width(String.valueOf(indent)), 11 + offset[0], 10, 10);
                } else if(file.getName().endsWith(".js")) {
                    RenderUtils.drawImage(matrixStack, new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/script/js.png"), 10 + this.fontRenderer.width(String.valueOf(indent)), 11 + offset[0], 10, 10);
                } else if(file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                    RenderUtils.drawImage(matrixStack, new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/script/json.png"), 10 + this.fontRenderer.width(String.valueOf(indent)), 11 + offset[0], 10, 10);
                } else {
                    RenderUtils.drawImage(matrixStack, new ResourceLocation(StoryVerse.MOD_ID, "textures/gui/script/text.png"), 10 + this.fontRenderer.width(String.valueOf(indent)), 11 + offset[0], 10, 10);
                }
                this.fontRenderer.draw(matrixStack, indent + file.getName(), 23, 12 + offset[0], new Color(255, 255, 255).getRGB());
                offset[0] += 10;
            }
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        this.textArea.charTyped(codePoint);
        this.textArea.save();
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.textArea.save();
        if(super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            boolean key = this.textArea.keyPressed(keyCode, scanCode, modifiers);
            if(key) {
                Chat.sendMessage("test");
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
