package org.thesalutyt.storyverse.api.screen.gui.overlay.alert;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.features.Timer;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiImage;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiLabel;

import java.awt.*;

public class AlertGui extends AbstractGui {
    protected AlertType type;
    protected GuiLabel text;
    protected GuiImage image;
    protected double time;

    public AlertGui(AlertType type) {
        this.type = type;
    }

    public AlertGui(AlertType type, GuiLabel text) {
        this(type);
        this.text = text;
    }

    public AlertGui(AlertType type, GuiLabel text, GuiImage image) {
        this(type, text);
        this.image = image;
    }

    public void render(MatrixStack stack) {
        while (time >= 0) {
            switch (type) {
                case TEXT:
                    if (text.centred) {
                        drawCenteredString(stack, Minecraft.getInstance().font, new StringTextComponent(text.message),
                                text.y, text.size, Color.WHITE.getAlpha());
                    } else {
                        drawString(stack, Minecraft.getInstance().font, new StringTextComponent(text.message), text.x,
                                text.y, text.size);
                    }
                    break;
                case BOTH:
                    if (text.centred) {
                        drawCenteredString(stack, Minecraft.getInstance().font, new StringTextComponent(text.message),
                                text.y, text.size, Color.WHITE.getAlpha());
                    } else {
                        drawString(stack, Minecraft.getInstance().font, new StringTextComponent(text.message), text.x,
                                text.y, text.size);
                    }
                    Minecraft.getInstance().textureManager.bind(new ResourceLocation(StoryVerse.MOD_ID, image.path));
                    this.blit(stack, image.x, image.y, 0, 0, image.width, image.height);
                    break;
            }

            time--;
        }
        Minecraft.getInstance().setScreen(null);
    }

    public AlertType getType() {
        return type;
    }

    public String getText() {
        return text.message;
    }

    public GuiImage getImage() {
        return image;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public void setText(GuiLabel text) {
        this.text = text;
    }

    public void setImage(GuiImage image) {
        this.image = image;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
