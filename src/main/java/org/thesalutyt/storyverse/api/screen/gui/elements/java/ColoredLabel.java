package org.thesalutyt.storyverse.api.screen.gui.elements.java;

import java.awt.*;
import java.util.HashMap;

public class ColoredLabel {
    public String message;
    public Integer size;
    public Integer x;
    public Integer y;
    public Double width;
    public Double height;
    public Boolean centred;
    public Color color;
    public static HashMap<String, ColoredLabel> labels = new HashMap<>();
    public ColoredLabel(Integer x, Integer y, Double width, Double height, String message, Integer size,
                        Boolean centred, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.centred = centred;
        this.size = size;
        this.color = color;
        labels.put(message, this);
    }
}
