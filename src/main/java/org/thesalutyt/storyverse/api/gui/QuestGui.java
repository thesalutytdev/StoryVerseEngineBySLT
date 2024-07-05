package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.thesalutyt.storyverse.utils.RenderUtils;
import org.thesalutyt.storyverse.utils.TimeHelper;

import java.awt.*;
import java.util.Random;

public class QuestGui extends Screen {
    Minecraft mc = Minecraft.getInstance();
    private final FontRenderer fontRenderer = Minecraft.getInstance().font;
    private final TimeHelper timer = new TimeHelper();
    int x = this.width / 2 + 110;
    int y = this.height / 2;
    int x2 = this.width / 2 - 110;
    int y2 = this.height / 2;
    boolean movingDown1 = true;
    boolean movingDown2 = false;
    int stage = 1;
    boolean collisionDetected = false;
    boolean collisionDetected2 = false;
    int barWidth = 150;
    int circleRadius = 40;

    public QuestGui() {
        super(new TranslationTextComponent("gui.creator.title"));
    }

    @Override
    protected void init() {
        long windowHandle = mc.getWindow().getWindow();
        GLFW.glfwSetCursorPos(windowHandle, (double) this.width / 2 - 100, this.height);
        x = this.width / 2 + 90;
        y = this.height / 2;
        x2 = this.width / 2 - 90;
        y2 = this.height / 2;
        stage = 1;
        barWidth = 150;
        circleRadius = 40;
        movingDown1 = true;
        movingDown2 = false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        AbstractGui.fill(matrixStack, this.width / 2 - 360, this.height / 2 - 220, this.width / 2 + 360, this.height / 2 + 220, new Color(107, 45, 0).getRGB());

        double xOffset = Math.sin(timer.getCurrentMS() * 0.003) * 40;
        x = (int) (this.width / 2 + 90 + xOffset);

        if (movingDown1) {
            y += 4;
            if (y >= this.height - 80) movingDown1 = false;
        } else {
            y -= 4;
            if (y <= 80) movingDown1 = true;
        }

        double x2Offset = Math.sin(timer.getCurrentMS() * 0.003 + Math.PI) * 40;
        x2 = (int) (this.width / 2 - 90 + x2Offset);

        if (movingDown2) {
            y2 += 4;
            if (y2 >= this.height - 80) movingDown2 = false;
        } else {
            y2 -= 4;
            if (y2 <= 80) movingDown2 = true;
        }

        if(timer.hasTimeReached(1600)) {
            if(stage >= 3) {
                stage = 1;
            } else {
                stage += 1;
            }
            timer.reset();
        }

        collisionDetected = checkCollision(x, y, 50, mouseX, mouseY, 30);
        collisionDetected2 = checkCollision(x2, y2, 50, mouseX, mouseY, 30);

        if ((collisionDetected || collisionDetected2) && barWidth > -150) {
            barWidth -= 4;
        }
        if(barWidth <= -150) {
            mc.setScreen(null);
        }

        RenderUtils.drawCircleRect(x + circleRadius, y + circleRadius, x - circleRadius, y - circleRadius, 50, new Color(255, 0, 0).getRGB());
        RenderUtils.drawCircleRect(x2 + circleRadius, y2 + circleRadius, x2 - circleRadius, y2 - circleRadius, 50, new Color(255, 0, 0).getRGB());
        RenderUtils.drawCircleRect((float) this.width / 2 + 340, (float) this.height / 2 - 80, (float) this.width / 2 + 380, (float) this.height / 2 - 40, 60, stage == 1 ? new Color(155, 0, 0).getRGB() : new Color(45, 45, 45).getRGB());
        RenderUtils.drawCircleRect((float) this.width / 2 + 340, (float) this.height / 2 - 20, (float) this.width / 2 + 380, (float) this.height / 2 + 20, 60, stage == 2 ? new Color(255, 174, 0).getRGB() : new Color(45, 45, 45).getRGB());
        RenderUtils.drawCircleRect((float) this.width / 2 + 340, (float) this.height / 2 + 80, (float) this.width / 2 + 380, (float) this.height / 2 + 40, 60, stage == 3 ? new Color(55, 178, 1).getRGB() : new Color(45, 45, 45).getRGB());
        RenderUtils.drawCircleRect(mouseX + 35, mouseY + 35, mouseX - 35, mouseY - 35, 50, new Color(255, 167, 114).getRGB());

        RenderUtils.drawCircleRect((float) this.width / 2 - 150, (float) this.height / 2 + 210, (float) this.width / 2 + 150, (float) this.height / 2 + 230, 5, new Color(45, 45, 45).getRGB());
        RenderUtils.drawCircleRect((float) this.width / 2 - 150, (float) this.height / 2 + 210, (float) this.width / 2 + barWidth, (float) this.height / 2 + 230, 5, new Color(138, 0, 0).getRGB());

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean checkCollision(int x1, int y1, int radius1, int x2, int y2, int radius2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < radius1 + radius2;
    }

    private boolean isPixelCollision(NativeImage img1, int x1, int y1, NativeImage img2, int x2, int y2) {
        int img1Width = img1.getWidth();
        int img1Height = img1.getHeight();
        int img2Width = img2.getWidth();
        int img2Height = img2.getHeight();

        for (int y = 0; y < img1Height; y++) {
            for (int x = 0; x < img1Width; x++) {
                int img1Alpha = img1.getPixelRGBA(x, y) >> 24 & 0xFF;
                if (img1Alpha > 0) {
                    int img2X = x + x1 - x2;
                    int img2Y = y + y1 - y2;
                    if (img2X >= 0 && img2X < img2Width && img2Y >= 0 && img2Y < img2Height) {
                        int img2Alpha = img2.getPixelRGBA(img2X, img2Y) >> 24 & 0xFF;
                        if (img2Alpha > 0) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}