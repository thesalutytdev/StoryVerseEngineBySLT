package org.thesalutyt.storyverse.api.screen.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.screen.color.ColorType;
import org.thesalutyt.storyverse.api.screen.gui.elements.*;
import org.thesalutyt.storyverse.api.screen.gui.render.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class OverlayGui extends AbstractGui {
    private Minecraft minecraft;
    private ArrayList<GuiLabel> labels = new ArrayList<>();
    private ArrayList<CircleRect> circleRects = new ArrayList<>();
    private ArrayList<GuiItem> items = new ArrayList<>();
    private ArrayList<GuiDisplayEntity> entities = new ArrayList<>();
    private ArrayList<GuiImage> images = new ArrayList<>();
    private int width;
    private int height;
    private int renderMode = 0;
    private Runnable onRender;
    private Runnable onTick;
    private int ticks = 0;

    public OverlayGui(Minecraft mc) {
        this.minecraft = mc;
        this.width = mc.getWindow().getGuiScaledWidth();
        this.height = mc.getWindow().getGuiScaledHeight();
    }

    public void addLabel(GuiLabel label) {
        labels.add(label);
    }

    public void addCircleRect(CircleRect rect) {
        circleRects.add(rect);
    }

    public void addItem(GuiItem item) {
        items.add(item);
    }

    public void addEntity(GuiDisplayEntity entity) {
        entities.add(entity);
    }

    public void addImage(GuiImage image) {
        images.add(image);
    }

    public void setRenderMode(int mode) {
        if (mode < 0 || mode > 2) throw new IllegalArgumentException("Invalid render mode: " + mode);
        renderMode = mode;
    }

    public void setOnRender(Runnable onRender) {
        this.onRender = onRender;
    }

    public void setOnTick(Runnable onTick) {
        this.onTick = onTick;
    }

    public void tick() {
        ticks++;
        if (onTick != null) onTick.run();

    }

    public void renderItems(MatrixStack stack) {
        if (!entities.isEmpty()) {
            for (GuiDisplayEntity entity : entities) {
                InventoryScreen.renderEntityInInventory(entity.widget.x.intValue(), entity.widget.y.intValue(),
                        entity.size.intValue(),
                        (float) ((float) (width / 2) - minecraft.mouseHandler.xpos()),
                        (float) ((float)
                        (height / 2 - 50)
                        - minecraft.mouseHandler.ypos()),
                        entity.entity);
            }
        }
        if (!labels.isEmpty()) {
            for (GuiLabel label : labels) {
                if (label.centred) {
                    drawCenteredString(stack, this.minecraft.font, new StringTextComponent(label.message),
                            label.y, label.size, Color.WHITE.getRGB());
                } else {
                    drawString(stack, this.minecraft.font, new StringTextComponent(label.message), label.x,
                            label.y, label.size);
                }
            }
        }
        if (!circleRects.isEmpty()) {
            for (CircleRect rect : circleRects) {
                RenderUtils.drawCircleRect(rect.x, rect.y, rect.x1, rect.y1,
                        rect.radius, rect.color);
            }
        }
        if (!images.isEmpty()) {
            for (GuiImage image : images) {
                this.minecraft.textureManager.bind(new ResourceLocation(StoryVerse.MOD_ID, image.path));
                this.blit(stack, image.x, image.y, 0, 0, image.width, image.height);
            }
        }
        if (!items.isEmpty()) {
            for (GuiItem item : items) {
                item.itemRenderer.renderGuiItem(item.stack, item.x.intValue(), item.y.intValue());
            }
        }
        if (onRender != null) onRender.run();
    }

    public void drawCenteredStr(String text, MatrixStack stack, int x, int y, ColorType color) {
        drawCenteredStr(text, stack, x, y, color.color);
    }

    public void drawCenteredStr(String text, MatrixStack stack, int x, int y, int color) {
        drawString(stack, minecraft.font, text, x, y, color);
    }

    public int getTicks() {
        return ticks;
    }

    public int getRenderMode() {
        return renderMode;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMouseX() {
        return (int) minecraft.mouseHandler.xpos();
    }

    public int getMouseY() {
        return (int) minecraft.mouseHandler.ypos();
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public ArrayList<GuiLabel> getLabels() {
        return labels;
    }

    public ArrayList<CircleRect> getCircles() {
        return circleRects;
    }

    public ArrayList<GuiItem> getItems() {
        return items;
    }

    public ArrayList<GuiDisplayEntity> getEntities() {
        return entities;
    }

    public ArrayList<GuiImage> getImages() {
        return images;
    }
}
