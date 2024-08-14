package org.thesalutyt.storyverse.api.screen.gui.test;

import net.minecraft.util.ResourceLocation;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGui;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGuiProperties;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IWidgetList;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiButton;

public class RLC extends IGui {
    public RLC() {
        super(new IGuiProperties().renderBackground(false).closeOnEsc(true).name("rlc")
                .width(512)
                .height(256));
    }

    @Override
    public void init() {
        super.init();
        this.width = 512;
        this.height = 256;

        setWidgets(new IWidgetList().addButton(new GuiButton("btn",
                new ResourceLocation("file",
                        "/home/maxim/npc.png"), 100, 100, 512.0, 512.0,
                "text.storyverse.test", () -> {System.out.println("Test button pressed!");})));
    }
}
