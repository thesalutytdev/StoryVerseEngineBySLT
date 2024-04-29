package org.thesalutyt.storyverse.common.specific.networking;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.thesalutyt.storyverse.api.special.FadeScreenPacket;

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
            return "1.0";
        }, (s) -> {
            return true;
        }, (s) -> {
            return true;
        });
        CHANNEL.registerMessage(nextID(), FadeScreenPacket.class, FadeScreenPacket::encode, FadeScreenPacket::new, FadeScreenPacket::handle);
    }
}
