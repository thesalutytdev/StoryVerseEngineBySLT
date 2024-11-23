package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.thesalutyt.storyverse.api.environment.resource.wrappers.FadeScreenWrapper;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import java.util.UUID;
import java.util.function.Supplier;

public class FadeScreenPacket {
    private final String packetData;

    public FadeScreenPacket(UUID wrapperId) {
        if (!FadeScreenWrapper.fades.containsKey(wrapperId)) throw new RuntimeException("No such fade screen wrapper");
        if (FadeScreenWrapper.fades.get(wrapperId).isExpired()) throw new RuntimeException("Fade screen is expired!");

        this.packetData = FadeScreenWrapper.fades.get(wrapperId).toString();
    }

    public FadeScreenPacket(PacketBuffer buffer) {
        this.packetData = buffer.readUtf();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(packetData);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        System.out.println("Handle fade screen");
        context.get().enqueueWork(() -> {
            try {
                System.out.println(context.get().getDirection().toString());
                FadeScreenWrapper.parse(packetData).fade();
            } catch (Exception e) {
                new ErrorPrinter(e);
                context.get().setPacketHandled(false);
            }
        });
        context.get().setPacketHandled(true);
    }
}
