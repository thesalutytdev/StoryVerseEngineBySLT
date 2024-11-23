package org.thesalutyt.storyverse.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

import static org.lwjgl.opengl.GL11.*;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {

    private static final Minecraft mc = Minecraft.getInstance();
    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color) {
        glColor(color);
        glEnable(GL_BLEND);
        RenderSystem.defaultBlendFunc();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1F);
        glBegin(GL_POLYGON);

        float xRadius = (float) Math.min((x1 - x) * 0.5, radius);
        float yRadius = (float) Math.min((y1 - y) * 0.5, radius);
        quickPolygonCircle(x+xRadius,y+yRadius, xRadius, yRadius,180,270,4);
        quickPolygonCircle(x1-xRadius,y+yRadius, xRadius, yRadius,90,180,4);
        quickPolygonCircle(x1-xRadius,y1-yRadius, xRadius, yRadius,0,90,4);
        quickPolygonCircle(x+xRadius,y1-yRadius, xRadius, yRadius,270,360,4);

        glEnd();
        glPopMatrix();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_LINE_SMOOTH);
    }

    public static Font getFontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getInstance().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    private static void quickPolygonCircle(float x, float y, float xRadius, float yRadius, int start, int end, int split) {
        for(int i = end; i >= start; i-=split) {
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * xRadius, y + Math.cos(i * Math.PI / 180.0D) * yRadius);
        }
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0F);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; i++)
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static class State implements Cloneable {
        public boolean enabled;
        public int transX;
        public int transY;
        public int x;
        public int y;
        public int width;
        public int height;

        @Override
        public State clone() {
            try {
                return (State)super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    private static final Deque<Frame> stack = new ArrayDeque<>();

    public static void push(int x, int y, int width, int height) {
        push(new Frame(x, y, x + width, y + height));
    }

    public static void push(Frame frame) {
        stack.addLast(frame);
        apply();
    }

    public static Frame pop() {
        Frame frame = stack.removeLast();
        apply();
        return frame;
    }

    public static void suspendScissors(Runnable fn) {
        Frame frame = pop();
        fn.run();
        push(frame);
    }

    private static void apply() {
        MainWindow window = Minecraft.getInstance().getWindow();

        if (stack.isEmpty()) {
            RenderSystem.disableScissor();
            return;
        }

        int x1 = 0;
        int y1 = 0;
        int x2 = window.getGuiScaledWidth();
        int y2 = window.getGuiScaledHeight();

        for (Frame frame : stack) {
            x1 = Math.max(x1, frame.x1);
            y1 = Math.max(y1, frame.y1);
            x2 = Math.min(x2, frame.x2);
            y2 = Math.min(y2, frame.y2);
        }

        double scale = window.getGuiScale();
        RenderSystem.enableScissor(
                (int) (x1 * scale), (int) (window.getHeight() - scale * y2),
                (int) ((x2 - x1) * scale), (int) ((y2 - y1) * scale)
        );
    }

    public static class Frame {
        public final int x1, y1, x2, y2;

        public Frame(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private static State state = new State();

    public static void makeScissorBox(final int xStart, final int yStart, final int xEnd, final int yEnd) {
        Rectangle screen = new Rectangle(0, 0, Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
        Rectangle current;
        if (state.enabled) {
            current = new Rectangle(state.x, state.y, state.width, state.height);
        } else {
            current = screen;
        }
        Rectangle target = new Rectangle(xStart+state.transX, yStart+state.transY, xEnd, yEnd);
        Rectangle result = current.intersection(target);
        result = result.intersection(screen);
        if (result.width < 0) result.width = 0;
        if (result.height < 0) result.height = 0;
        state.enabled = true;
        state.x = result.x;
        state.y = result.y;
        state.width = result.width;
        state.height = result.height;
        GL11.glScissor(result.x, result.y, result.width, result.height);
    }

    public static void drawImage(MatrixStack matrixStack, ResourceLocation image, int x, int y, int width, int height) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getInstance().getTextureManager().bind(image);
        AbstractGui.blit(matrixStack, x, y, 0, 0, width, height, width, height);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }
}
