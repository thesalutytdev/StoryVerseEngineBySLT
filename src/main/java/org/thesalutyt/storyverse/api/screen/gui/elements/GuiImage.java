package org.thesalutyt.storyverse.api.screen.gui.elements;

import java.io.Serializable;

public class GuiImage extends GuiWidget implements Serializable {
    public String path;
    public Integer x;
    public Integer y;
    public Integer width;
    public Integer height;
    public Boolean centered;

    public GuiImage(String path, Integer x, Integer y, Integer width, Integer height, Boolean centered) {
        super(x.doubleValue(), y.doubleValue(), width.doubleValue(), height.doubleValue());
        this.path = path;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.centered = centered;
    }
}
