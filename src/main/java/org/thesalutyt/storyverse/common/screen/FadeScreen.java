package org.thesalutyt.storyverse.common.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.item.MilkBucketItem;
import org.thesalutyt.storyverse.annotations.Documentate;

public class FadeScreen {
    Minecraft mc = Minecraft.getInstance();
    int screenWidth;

    {
        assert mc.screen != null;
        screenWidth = mc.screen.width;
    }

    int screenHeight;

    {
        assert mc.screen != null;
        screenHeight = mc.screen.height;
    }

    public FadeScreen(Minecraft mc) {
        super();
    }

    public void renderFadeScreen(MatrixStack stack, int ticksPassed, int col) {
        return;
    }
}
