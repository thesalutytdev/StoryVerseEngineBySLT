package org.thesalutyt.storyverse.mixins;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.ScreenManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.event.WindowEvent;

@Mixin(Window.class)
public class GLFWInitMixin {
    @Final
    private long window;

    @Inject(at = @At("TAIL"),method = "<init>*",remap = false)
    private void onGLFWInit(WindowEvent windowEventHandler,
                            ScreenManager screenManager,
                            DisplayInfo displayData,
                            String string,
                            String string2,
                            CallbackInfo ci) {

    }
}
