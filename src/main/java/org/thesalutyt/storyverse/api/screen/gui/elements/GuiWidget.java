package org.thesalutyt.storyverse.api.screen.gui.elements;

import java.io.Serializable;

public class GuiWidget implements Serializable {
    public Double x;
    public Double y;
    public Double width;
    public Double height;

    public GuiWidget(Double x, Double y, Double width, Double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
