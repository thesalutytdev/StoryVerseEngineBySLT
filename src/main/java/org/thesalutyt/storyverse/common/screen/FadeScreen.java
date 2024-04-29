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
    int screenWidth = mc.screen.width;
    int screenHeight = mc.screen.height;
    public FadeScreen(Minecraft mc) {
        super();
    }

    public void renderFadeScreen(MatrixStack stack, int ticksPassed, int col) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();
        float radVal = (float)Math.toRadians((double)(Math.min(ticksPassed, 10) * 9));
        IngameGui.fill(stack, 0,
                0, screenWidth,
                (int)((double)screenHeight * (Math.sin((double)radVal) + 1.0) / 2.0),
                col);
    }
}
