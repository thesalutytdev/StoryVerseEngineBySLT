package org.thesalutyt.storyverse.api.features;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;

public class Gui {
    Minecraft mc = Minecraft.getInstance();
    final int WIDTH = mc.screen.width;
    final int HEIGHT = mc.screen.height;
    Gui s_c_g = new Gui();
    public void test() {
        IngameGui.fill(new MatrixStack(), 1, 1, 1, 1, 1);
    }
}
