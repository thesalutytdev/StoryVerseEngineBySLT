package org.thesalutyt.storyverse.api.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.utils.TimeHelper;

import java.awt.*;

public class FadeScreenGui {

    private static final TimeHelper timer = new TimeHelper();
    public static int elapsedTicks = 0;
    public static int color = 0;
    public static int time = 200;
    public static String text = " ";

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MatrixStack matrixStack = event.getMatrixStack();
            FontRenderer fontRenderer = mc.font;

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            int newColor = getColorWithAlpha(color);

            AbstractGui.fill(matrixStack, 0, 0, screenWidth, screenHeight, newColor);
            fontRenderer.draw(matrixStack, text, screenWidth / 2f - (float) fontRenderer.width(text) / 2, screenHeight / 2f, new Color(255, 255, 255).getRGB());
            if(elapsedTicks < 250) elapsedTicks += 4;

            if (timer.hasTimeReached(time)) {
                MinecraftForge.EVENT_BUS.unregister(FadeScreenGui.class);
                timer.reset();
            }
        }
    }

    private static int getColorWithAlpha(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        return (elapsedTicks << 24) | (red << 16) | (green << 8) | blue;
    }
}
