package org.thesalutyt.storyverse.api.screen.gui.elements;

import java.io.Serializable;
import java.util.HashMap;

public class GuiLabel extends GuiWidget implements Serializable {
    public String message;
    public Integer size;
    public Integer x;
    public Integer y;
    public Boolean centred;
    public static HashMap<String, GuiLabel> labels = new HashMap<>();
    public GuiLabel(Integer x, Integer y, Double width, Double height, String message, Integer size, Boolean centred) {
        super(x.doubleValue(), y.doubleValue(), width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.centred = centred;
        this.size = size;
        labels.put(message, this);
    }
}
