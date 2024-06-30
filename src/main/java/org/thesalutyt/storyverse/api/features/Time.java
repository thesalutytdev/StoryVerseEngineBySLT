package org.thesalutyt.storyverse.api.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.main.Main;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

public class Time extends ScriptableObject implements EnvResource, JSResource {
    public static void freeze() {
        ;
    }
    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public String getResourceId() {
        return null;
    }
}
