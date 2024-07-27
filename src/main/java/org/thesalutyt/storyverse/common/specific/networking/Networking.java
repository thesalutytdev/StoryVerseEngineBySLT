package org.thesalutyt.storyverse.common.specific.networking;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.thesalutyt.storyverse.SVEngine;

public class Networking {
    public static SimpleChannel CHANNEL;
    public static int ID;

    public Networking() {
    }

    public static int nextID() {
        return ID++;
    }

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("storyverse", "network"), () -> {
            return SVEngine.MOD_VERSION;
        }, (s) -> {
            return true;
        }, (s) -> {
            return true;
        });

    }
}
