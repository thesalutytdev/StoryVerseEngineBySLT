package org.thesalutyt.storyverse.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "run", at = @At("HEAD"))
    public void run(CallbackInfo ci) {
        System.out.println("MinecraftMixin run method called");
    }
}
