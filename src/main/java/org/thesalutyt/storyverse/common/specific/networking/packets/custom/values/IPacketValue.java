package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;

public interface IPacketValue {
    public Value<?> get();
    public void encode(PacketBuffer buffer);
}
