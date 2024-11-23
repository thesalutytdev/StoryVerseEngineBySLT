package org.thesalutyt.storyverse.api.screen.gui.elements;

import java.io.Serializable;

public class CircleRect extends GuiWidget implements Serializable {
    public float x;
    public float y;
    public float x1;
    public float y1;
    public float radius;
    public int color;
    public CircleRect(float x, float y, float x1, float y1, float radius, int color) {
        super((double) x, (double) y, (double) x1, (double) y1);
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.radius = radius;
        this.color = color;
    }
}
