package org.thesalutyt.storyverse.api.screen.color;

public class Color {
    public int r;
    public int g;
    public int b;
    public int a;
    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }
    public static ColorType get(String name) {
        return ColorType.valueOf(name);
    }
}
