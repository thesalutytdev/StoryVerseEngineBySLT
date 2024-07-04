package org.thesalutyt.storyverse.mixins;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.main.Main.class)
@OnlyIn(Dist.CLIENT)
public class MainMixin {
    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] args, CallbackInfo ci) {
        System.out.println("MIXIN WORKED!!");
    }
}
