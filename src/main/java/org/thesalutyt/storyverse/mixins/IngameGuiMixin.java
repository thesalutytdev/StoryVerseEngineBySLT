package org.thesalutyt.storyverse.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IngameGui.class)
@OnlyIn(Dist.CLIENT)
public class IngameGuiMixin extends AbstractGui {
    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrixStack, float partialTicks, CallbackInfo ci) {
        System.out.println("IngameGuiMixin render method called");
    }
}