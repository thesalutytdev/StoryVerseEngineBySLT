package org.thesalutyt.storyverse.api.screen.color;

public enum ColorType {
    GREEN("green", 0x00FF00),
    RED("red", 0xFF0000),
    BLUE("blue", 0x0000FF),
    YELLOW("yellow", 0xFFFF00),
    WHITE("white", 0xFFFFFF),
    BLACK("black", 0x000000),
    PURPLE("purple", 0xFF00FF),
    ORANGE("orange", 0xFF8000),
    PINK("pink", 0xFFC0CB);

    public final String name;
    public final int color;
    ColorType(String name, int color) {
        this.name = name;
        this.color = color;
    }
}
