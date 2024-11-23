package org.thesalutyt.storyverse.api.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.screen.gui.elements.*;
import org.thesalutyt.storyverse.api.screen.gui.render.RenderUtils;
import org.thesalutyt.storyverse.api.screen.gui.script.ScriptableGui;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class CustomizableGui extends Screen implements EnvResource, Serializable {
    public String title;
    public ArrayList<GuiLabel> labels = new ArrayList<>();
    public ArrayList<GuiButton> buttons = new ArrayList<>();
    public ArrayList<GuiImage> images = new ArrayList<>();
    public ArrayList<CircleRect> circleRect = new ArrayList<>();
    public ArrayList<GuiDisplayEntity> entities = new ArrayList<>();
    public ArrayList<GuiItem> items = new ArrayList<>();
    public String background;
    public Boolean renderBG = true;
    public Boolean isPause = true;
    public Boolean closeOnEsc = true;
    public int gWidth;
    public int gHeight;
    public int gMouseX;
    public int gMouseY;
    public int cursorX = this.width;
    public int cursorY = this.height;
    public ScriptableGui guiScriptableReference;
    public Runnable onTick;
    public Runnable onClose;

    public CustomizableGui(String title) {
        super(new StringTextComponent(title));
    }

    public void setScriptableReference(ScriptableGui guiScriptableReference) {
        this.guiScriptableReference = guiScriptableReference;
    }

    protected void renderAllItems(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        assert this.minecraft != null;
        if (background != null && renderBG) {
            this.minecraft.getTextureManager().bind(new ResourceLocation(StoryVerse.MOD_ID, background));
            this.blit(matrixStack, 0, 0, 1024, 512,
                    1024, 512);
        }
        if (!entities.isEmpty()) {
            for (GuiDisplayEntity entity : entities) {
                InventoryScreen.renderEntityInInventory(entity.widget.x.intValue(), entity.widget.y.intValue(),
                        entity.size.intValue(), (float) (this.width / 2) - mouseX,
                        (float) (this.height / 2 - 50) - mouseY, entity.entity);
            }
        }
        if (!buttons.isEmpty()) {
            for (GuiButton button : buttons) {
                this.addButton(button);
            }
        }
        if (!labels.isEmpty()) {
            for (GuiLabel label : labels) {
                if (label.centred) {
                    drawCenteredString(matrixStack, this.font, new StringTextComponent(label.message),
                            label.y, label.size, Color.WHITE.getRGB());
                } else {
                    drawString(matrixStack, this.font, new StringTextComponent(label.message), label.x,
                            label.y, label.size);
                }
            }
        }
        if (!circleRect.isEmpty()) {
            for (CircleRect rect : circleRect) {
                RenderUtils.drawCircleRect(rect.x, rect.y, rect.x1, rect.y1,
                        rect.radius, rect.color);
            }
        }
        if (!images.isEmpty()) {
            for (GuiImage image : images) {
                this.minecraft.textureManager.bind(new ResourceLocation(StoryVerse.MOD_ID, image.path));
                this.blit(matrixStack, image.x, image.y, 0, 0, image.width, image.height);
            }
        }
        if (!items.isEmpty()) {
            for (GuiItem item : items) {
                item.itemRenderer.renderGuiItem(item.stack, item.x.intValue(), item.y.intValue());
            }
        }
    }

    @Override
    public void init() {
        super.init();
        this.width = gWidth;
        this.height = gHeight;
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        GLFW.glfwSetCursorPos(windowHandle, cursorX, cursorY);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (renderBG) {
            renderBackground(matrixStack);
        }
        renderAllItems(matrixStack, mouseX, mouseY, partialTicks);
        this.gMouseX = mouseX;
        this.gMouseY = mouseY;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (onTick != null) {
            onTick.run();
        }
        if (guiScriptableReference != null) {
            guiScriptableReference.tick();
        }
    }

    @Override
    public void onClose() {
        if (onClose != null) {
            onClose.run();
        }
        if (guiScriptableReference != null) {
            guiScriptableReference.runOnClose();
            ScriptableGui.guis.remove(guiScriptableReference.id);
        }
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return isPause;
    }
    @Override
    public boolean shouldCloseOnEsc() {
        return closeOnEsc;
    }

    @Override
    public String getResourceId() {
        return "CustomizableGui";
    }
}
