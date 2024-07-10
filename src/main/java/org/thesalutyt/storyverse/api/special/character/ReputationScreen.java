package org.thesalutyt.storyverse.api.special.character;

import org.thesalutyt.storyverse.api.screen.gui.constructor.IGui;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGuiProperties;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IWidgetList;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiLabel;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiEntity;

public class ReputationScreen extends IGui {
    public Reputation reputation;
    public ReputationScreen(Reputation rep) {
        super(new IGuiProperties().name("gui.storyverse.reputation_screen.name")
                .width(512)
                .height(256)
                .renderBackground(false)
                .closeOnEsc(true)
                .isPause(false));
        this.reputation = rep;
    }

    @Override
    public IGui guiInit() {
        super.guiInit();
        setWidgets(new IWidgetList().addLabel(new GuiLabel(100,
                100, 100.0, 100.0,
                "Reputation Screen", 90, true))
                .addEntity(new GuiEntity(reputation.character.getMobEntity(), 200, 200, 90)));
        return this;
    }
}
