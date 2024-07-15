package org.thesalutyt.storyverse.api.screen.gui.constructor;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.screen.gui.elements.CircleRect;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiImage;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiLabel;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.ColoredLabel;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiButton;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiEntity;
import org.thesalutyt.storyverse.api.screen.gui.render.RenderUtils;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

public class IGui extends Screen {
    public IGuiProperties properties;
    public IWidgetList widgets;
    public IGui(IGuiProperties properties) {
        super(new TranslationTextComponent(properties.name));
        this.properties = properties;
    }

    public IGui config(IGuiProperties properties) {
        this.properties = properties;

        return this;
    }

    @Override
    public void init() {
        super.init();
        this.width = properties.width;
        this.height = properties.height;
        guiInit();
    }

    public IGui guiInit() {
        return this;
    }

    public IGui resize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public IGui setWidgets(IWidgetList list) {
        this.widgets = list.get();
        return this;
    }

    public IWidgetList getWidgets() {
        return widgets;
    }

    public IGuiProperties getProperties() {
        return properties;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        assert this.minecraft != null;
        if (properties.renderBG && properties.background != null) {
            this.minecraft.getTextureManager().bind(properties.background);
            blit(stack, 0, 0, 0, 0,
                    this.width, this.height, this.width, this.height);
        }
        if (widgets != null) {
            try {
                for (GuiButton button : widgets.buttons) {
                    button.render(stack, mouseX, mouseY, partialTicks);
                }
                for (GuiLabel label : widgets.labels) {
                    if (label.centred) {
                        AbstractGui.drawCenteredString(stack, this.minecraft.font, new StringTextComponent(label.message),
                                label.y, label.size, Color.WHITE.getRGB());
                    } else {
                        AbstractGui.drawString(stack, this.minecraft.font, new StringTextComponent(label.message),
                                label.x, label.y, label.size);
                    }
                }
                for (ColoredLabel label : widgets.coloredLabels) {
                    if (label.centred) {
                        AbstractGui.drawCenteredString(stack, this.minecraft.font, new StringTextComponent(label.message),
                                label.y, label.size, label.color.getRGB());
                    } else {
                        AbstractGui.drawString(stack, this.minecraft.font, new StringTextComponent(label.message),
                                label.x, label.y, label.size);
                    }
                }
                for (GuiImage image : widgets.images) {
                    this.minecraft.textureManager.bind(new ResourceLocation(StoryVerse.MOD_ID, image.path));
                    blit(stack, image.x, image.y, 0, 0, image.width, image.height);
                }
                for (GuiEntity entity : widgets.entities) {
                    InventoryScreen.renderEntityInInventory(entity.x, entity.y,
                            entity.size, (float) (this.width / 2) - mouseX,
                            (float) (this.height / 2 - 50) - mouseY, entity.entity);
                }
                for (CircleRect rect : widgets.circleRect) {
                    RenderUtils.drawCircleRect(rect.x, rect.y, rect.x1, rect.y1,
                            rect.radius, rect.color);
                }
            } catch (Exception e) {
                new ErrorPrinter(e);
            }
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    public void renderSpec(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return properties.isPause;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return properties.closeOnEsc;
    }
}
