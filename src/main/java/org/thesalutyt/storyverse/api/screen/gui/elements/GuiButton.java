package org.thesalutyt.storyverse.api.screen.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;

public class GuiButton extends Button {
    public String message;
    public ArrayList<BaseFunction> onClick = new ArrayList<>();
    public static HashMap<String, GuiButton> btns = new HashMap<>();
    public Button button;
    public Double x;
    public Double y;
    public Double width;
    public Double height;
    public ResourceLocation texture;
    public String id;

    public void onClick() {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.getCurrentContext();
            for (int i=0;i<onClick.size(); i++) {
                onClick.get(i).call(ctx, SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{});
            }
        });
    }

    public GuiButton(String id, String texture,
                     Double x, Double y, Double width, Double height, String message, BaseFunction function) {
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
        EventLoop.getLoopInstance().runImmediate(() -> {
            onClick.add(function);
        });
        this.button = new Button(x.intValue(), y.intValue(), width.intValue(), height.intValue(),
                new StringTextComponent(message), (button) -> {
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
