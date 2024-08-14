package org.thesalutyt.storyverse.api.screen.gui.elements.java;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

public class GuiButton extends Button {
    public String message;
    public Runnable onClick;
    public static HashMap<String, GuiButton> btns = new HashMap<>();
    public Button button;
    public Double x;
    public Double y;
    public Double width;
    public Double height;
    public ResourceLocation texture;
    public String id;

    public void onClick() {
        onClick.run();
    }

    public GuiButton(String id, String texture,
                     Double x, Double y, Double width, Double height, String message, Runnable function) {
        super(x.intValue(), y.intValue(),
                width.intValue(),
                height.intValue(),
                new StringTextComponent(message), (button) -> {
            System.out.println("Button clicked: " + message);
        });
        this.id = id;
        this.texture = new ResourceLocation(StoryVerse.MOD_ID, texture);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.onClick = function;
        this.button = new Button(x.intValue(), y.intValue(), width.intValue(), height.intValue(),
                new StringTextComponent(message), (button) -> {
            System.out.println("Button clicked: " + message);
            onClick();
        });
        btns.put(id, this);
    }

    public GuiButton(String id, String texture,
                     int x, int y, Double width, Double height, String translatable, Runnable function) {
        super(x, y,
                width.intValue(),
                height.intValue(),
                new TranslationTextComponent(translatable), (button) -> {
                    System.out.println("Button clicked: " + translatable);
                });
        this.id = id;
        this.texture = new ResourceLocation(StoryVerse.MOD_ID, texture);
        this.x = (double) x;
        this.y = (double) y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.onClick = function;
        this.button = new Button(x, y, width.intValue(), height.intValue(),
                new TranslationTextComponent(message), (button) -> {
            System.out.println("Button clicked: " + message);
            onClick();
        });
        btns.put(id, this);
    }

    public GuiButton(String id, ResourceLocation texture,
                     int x, int y, Double width, Double height, String translatable, Runnable function) {
        super(x, y,
                width.intValue(),
                height.intValue(),
                new TranslationTextComponent(translatable), (button) -> {
                    System.out.println("Button clicked: " + translatable);
                });
        this.id = id;
        this.texture = texture;
        this.x = (double) x;
        this.y = (double) y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.onClick = function;
        this.button = new Button(x, y, width.intValue(), height.intValue(),
                new TranslationTextComponent(message), (button) -> {
            System.out.println("Button clicked: " + message);
            onClick();
        });
        btns.put(id, this);
    }

    @Override
    public void onPress() {
        onClick();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(texture);
        blit(matrixStack, this.x.intValue(),
                this.y.intValue(), 0, 0,
                this.width.intValue(), this.height.intValue(), this.width.intValue(),
                this.height.intValue());
        drawCenteredString(matrixStack, Minecraft.getInstance().font, this.getMessage(),
                (int) this.x.intValue() + (int) this.width.intValue() / 2,
                this.y.intValue() + (this.height.intValue() - 8) / 2, 0xFFFFFF);
    }
}
