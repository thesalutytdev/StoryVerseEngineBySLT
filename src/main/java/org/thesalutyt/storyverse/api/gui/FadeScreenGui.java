package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.thesalutyt.storyverse.utils.TimeHelper;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class FadeScreenGui {

    private static final TimeHelper timer = new TimeHelper();
    public static int elapsedTicks = 0;
    public static int color = 0xFF000000;
    public static int time = 5000;
    public static String text = "test";
    private static boolean in = true;
    private static int inputTime = 0;
    private static boolean out = true;
    private static int outputTime = 0;
    public static boolean enabled = false;

    public static void fade(String text, int time, int color, int input, int output) {
        FadeScreenGui.time = time;
        FadeScreenGui.color = color;
        FadeScreenGui.text = text;
        FadeScreenGui.elapsedTicks = 0;
        FadeScreenGui.inputTime = input;
        FadeScreenGui.outputTime = output;
        timer.reset();
        in = true;
        out = false;
        enabled = true;
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (enabled) {
                Minecraft mc = Minecraft.getInstance();
                MatrixStack matrixStack = event.getMatrixStack();
                FontRenderer fontRenderer = mc.font;

                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();

                int newColor = getColorWithAlpha(color);

                AbstractGui.fill(matrixStack, 0, 0, screenWidth, screenHeight, newColor);
                if (elapsedTicks > 240 && in) fontRenderer.draw(matrixStack, text, screenWidth / 2f - (float) fontRenderer.width(text) / 2, screenHeight / 2f, new Color(255, 255, 255).getRGB());
                if (elapsedTicks < 250 && in) elapsedTicks = Math.min(255, (int)(255 * (timer.getTime() / (float)inputTime)));
                if (elapsedTicks > 0 && out) elapsedTicks = Math.max(0, (int)(255 * ((inputTime + time + outputTime - timer.getTime()) / (float)outputTime)));

                if (timer.hasReached(inputTime + time)) {
                    in = false;
                    out = true;
                }
                if (timer.hasReached(inputTime + time + outputTime)) {
                    out = false;
                    enabled = false;
                }
            }
        }
    }

    public static int getColorWithAlpha(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        return (elapsedTicks << 24) | (red << 16) | (green << 8) | blue;
    }
}
