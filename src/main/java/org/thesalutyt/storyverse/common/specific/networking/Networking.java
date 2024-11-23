package org.thesalutyt.storyverse.common.specific.networking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.*;

import java.io.*;

public class Networking {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(StoryVerse.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        net.messageBuilder(CustomPacket.class, id())
                .decoder(CustomPacket::new)
                .encoder(CustomPacket::encode)
                .consumer(CustomPacket::handle)
                .add();
        if (FMLEnvironment.dist.isClient()) {
            net.messageBuilder(SVGuiPacket.class, id())
                    .decoder(SVGuiPacket::new)
                    .encoder(SVGuiPacket::encode)
                    .consumer(SVGuiPacket::handle)
                    .add();
        }

        net.messageBuilder(FadeScreenPacket.class, id())
                .decoder(FadeScreenPacket::new)
                .encoder(FadeScreenPacket::encode)
                .consumer(FadeScreenPacket::handle)
                .add();

        net.messageBuilder(TotemShowPacket.class, id())
                .decoder(TotemShowPacket::new)
                .encoder(TotemShowPacket::encode)
                .consumer(TotemShowPacket::handle)
                .add();

        net.messageBuilder(ScriptExecutionPacket.class, id())
                .decoder(ScriptExecutionPacket::new)
                .encoder(ScriptExecutionPacket::encode)
                .consumer(ScriptExecutionPacket::handle)
                .add();

        INSTANCE = net;
    }

    public static byte[] toByte(Object object){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object toObj(byte[] bytes){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            StoryVerse.LOGGER.info(e);
            throw new RuntimeException(e);
        }
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, PlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
