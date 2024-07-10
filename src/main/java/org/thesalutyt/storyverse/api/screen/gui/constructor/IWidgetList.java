package org.thesalutyt.storyverse.api.screen.gui.constructor;

import net.minecraft.client.Minecraft;
import org.thesalutyt.storyverse.api.screen.gui.elements.CircleRect;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiImage;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiLabel;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiButton;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiEntity;

import java.util.ArrayList;

public class IWidgetList {
    public ArrayList<GuiButton> buttons = new ArrayList<>();
    public ArrayList<GuiLabel> labels = new ArrayList<>();
    public ArrayList<GuiImage> images = new ArrayList<>();
    public ArrayList<GuiEntity> entities = new ArrayList<>();
    public ArrayList<CircleRect> circleRect = new ArrayList<>();
    public Minecraft mc = Minecraft.getInstance();

    public IWidgetList() {

    }

    public IWidgetList addButton(GuiButton button) {
        buttons.add(button);
        return this;
    }

    public IWidgetList addLabel(GuiLabel label) {
        labels.add(label);
        return this;
    }

    public IWidgetList addImage(GuiImage image) {
        images.add(image);
        return this;
    }

    public IWidgetList addEntity(GuiEntity entity) {
        entities.add(entity);
        return this;
    }

    public IWidgetList addCircleRect(CircleRect circleRect) {
        this.circleRect.add(circleRect);
        return this;
    }

    public IWidgetList get() {
        return this;
    }
}
