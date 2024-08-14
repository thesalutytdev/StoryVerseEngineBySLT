package org.thesalutyt.storyverse.api.special;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.thesalutyt.storyverse.common.events.ModEvents;

import java.util.UUID;
import java.util.function.Supplier;

public class FadeScreenPacket {
    public UUID uuid;
    public int ticks;
    public int color;

    public FadeScreenPacket(UUID uuid, int ticks, int color) {
        this.ticks = ticks;
        this.uuid = uuid;
        this.color = color;
    }

    public FadeScreenPacket(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.ticks = buffer.readInt();
        this.color = buffer.readInt();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeInt(this.ticks);
        buffer.writeInt(this.color);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context)context.get()).enqueueWork(() -> {
            ModEvents.fadeScreenTimers.put(this.uuid, this.ticks);
            ModEvents.fadeScreenColors.put(this.uuid, this.color);
        });
        ((NetworkEvent.Context)context.get()).setPacketHandled(true);
    }
}
