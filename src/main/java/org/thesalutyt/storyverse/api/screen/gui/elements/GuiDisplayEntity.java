package org.thesalutyt.storyverse.api.screen.gui.elements;

import net.minecraft.entity.LivingEntity;
import org.thesalutyt.storyverse.api.environment.js.MobJS;

import java.util.UUID;

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
    public GuiDisplayEntity(UUID mobId, Double x, Double y, Double size) {
        super(x, y, size * x, size * y);
        this.entity = MobJS.getMob(mobId).getMobEntity();
        this.size = size;
        this.widget = new GuiWidget(x, y, width, height);
    }
}
