package org.thesalutyt.storyverse.api.screen.gui.elements;

import net.minecraft.entity.LivingEntity;
import org.thesalutyt.storyverse.api.environment.js.MobJS;

public class GuiDisplayEntity extends GuiWidget {
    public Double size;
    public LivingEntity entity;
    public GuiWidget widget;

    public GuiDisplayEntity(String mobId, Double x, Double y, Double size) {
        super(x, y, size * x, size * y);
        this.entity = MobJS.controllers.get(mobId).getMobEntity();
        this.size = size;
        this.widget = new GuiWidget(x, y, width, height);
    }
}
