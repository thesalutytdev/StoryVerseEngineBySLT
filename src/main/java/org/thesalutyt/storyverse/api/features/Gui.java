package org.thesalutyt.storyverse.api.features;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.screen.gui.elements.CircleRect;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiButton;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiLabel;
import org.thesalutyt.storyverse.api.screen.gui.render.RenderUtils;
import org.thesalutyt.storyverse.api.screen.gui.resource.GuiType;

import java.awt.*;
import java.util.ArrayList;

public class Gui extends Screen implements EnvResource {
    protected static LivingEntity displayEntity;
    protected static String label = "Dialogue";
    protected static ArrayList<Button> buttons = new ArrayList<>();
    protected static ArrayList<GuiLabel> labels = new ArrayList<>();
    protected static ArrayList<CircleRect> circleRect = new ArrayList<>();
    protected static Boolean displayCR = false;
    protected static ResourceLocation BACKGROUND;
    protected static Integer entityX;
    protected static Integer entityY;
    protected static Integer entitySize = 100;
    protected static Integer gWidth;
    protected static Integer gHeight;
    protected static Boolean isPause = true;
    protected static Boolean renderBG = true;
    public static GuiType type = GuiType.DEFAULT;
    public Gui() {
        super(new StringTextComponent(label));
    }

    public static void create_gui(String label, Integer width, Integer height) {
        Gui.label = label;
        gWidth = width;
        gHeight = height;
    }
    public static void setPause(Boolean pause) {
        Gui.isPause = pause;
    }
    public static void setBackGround(String background) {
        BACKGROUND = new ResourceLocation(StoryVerse.MOD_ID, background);
    }
    public static void addMob(String mobId) {
        displayEntity = MobJS.getMob(mobId).getMobEntity();
    }
    public static void addButton(String buttonId) {
        buttons.add(GuiButton.btns.get(buttonId).button);
    }
    public static void addLabel(String labelId) {
        labels.add(GuiLabel.labels.get(labelId));
    }
    public static void setEntityPos(Integer x, Integer y) {
        entityX = x;
        entityY = y;
    }
    public static int getWidth() {
        return Minecraft.getInstance().getWindow().getWidth();
    }
    public static int getHeight() {
        return Minecraft.getInstance().getWindow().getHeight();
    }
    public static void addCircle(Double x, Double y, Double x1, Double y1, Double radius, Integer color) {
        circleRect.add(new CircleRect(x.floatValue(),
                y.floatValue(),
                x1.floatValue(), y1.floatValue(), radius.floatValue(), color));
        displayCR = true;
    }

    @Override
    protected void init() {
        super.init();
        this.width = gWidth;
        this.height = gHeight;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (renderBG) {
            renderBackground(matrixStack);
        }
        assert this.minecraft != null;
        if (BACKGROUND != null) {
            this.minecraft.getTextureManager().bind(BACKGROUND);
            this.blit(matrixStack, 0, 0, 1024, 512,
                    1024, 512);
        }
        drawCenteredString(matrixStack, this.font, label, this.width / 2, 85, 16777215);
        if (displayEntity != null) {
            InventoryScreen.renderEntityInInventory(100, 100,
                    90, (float)(this.width / 2) - mouseX,
                    (float)(this.height / 2 - 50) - mouseY, displayEntity);
        }
        for (int i = 0; i < buttons.size(); i++) {
            this.addButton(buttons.get(i));
        }
        for (int j = 0; j < labels.size(); j++) {
            GuiLabel label = labels.get(j);
            if (label.centred) {
                drawCenteredString(matrixStack, this.font, new StringTextComponent(labels.get(j).message),
                        labels.get(j).y, labels.get(j).size, Color.WHITE.getAlpha());
            } else {
                drawString(matrixStack, this.font, new StringTextComponent(labels.get(j).message), label.x,
                        labels.get(j).y, labels.get(j).size);
            }
        }
        if (displayCR) {
            for (int k = 0; k < circleRect.size(); k++) {
                RenderUtils.drawCircleRect(circleRect.get(k).x, circleRect.get(k).y, circleRect.get(k).x1, circleRect.get(k).y1,
                        circleRect.get(k).radius, circleRect.get(k).color);
            }
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public boolean isPauseScreen() {
        return isPause;
    }

    public static void close_gui() {
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public String getResourceId() {
        return "GuiUtils";
    }
}
