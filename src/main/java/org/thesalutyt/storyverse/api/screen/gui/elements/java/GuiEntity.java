package org.thesalutyt.storyverse.api.screen.gui.elements.java;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.LivingEntity;

public class GuiEntity {
    public LivingEntity entity;
    public int x;
    public int y;
    public int size;

    public GuiEntity(LivingEntity entity, int x, int y, int size) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.size = size;
    }
}
