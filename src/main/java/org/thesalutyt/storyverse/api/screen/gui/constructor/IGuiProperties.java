package org.thesalutyt.storyverse.api.screen.gui.constructor;

import net.minecraft.util.ResourceLocation;

public class IGuiProperties {
    public Boolean isPause = true;
    public Boolean closeOnEsc = true;
    public Boolean renderBG = true;
    public Integer width;
    public Integer height;
    public String name;
    public ResourceLocation background;

    public IGuiProperties() {

    }

    public IGuiProperties name(String name) {
        this.name = name;
        return this;
    }

    public IGuiProperties isPause(Boolean pause) {
        this.isPause = pause;
        return this;
    }

    public IGuiProperties closeOnEsc(Boolean closeOnEsc) {
        this.closeOnEsc = closeOnEsc;
        return this;
    }

    public IGuiProperties renderBackground(Boolean renderBG) {
        this.renderBG = renderBG;
        return this;
    }

    public IGuiProperties width(Integer width) {
        this.width = width;
        return this;
    }

    public IGuiProperties height(Integer height) {
        this.height = height;
        return this;
    }

    public IGuiProperties background(ResourceLocation background) {
        this.background = background;
        return this;
    }
}
