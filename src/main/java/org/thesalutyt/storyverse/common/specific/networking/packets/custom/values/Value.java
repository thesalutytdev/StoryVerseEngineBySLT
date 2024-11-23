package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;

public abstract class Value<T> implements IPacketValue {
    protected T value;

    public abstract Value<T> get();

    public abstract T getValue();

    public abstract void encode(PacketBuffer buffer);

    public abstract T tryParse(PacketBuffer buffer);

    public abstract void setValue(Object value);

    public Value(T value) {
        this.value = value;
    }

    protected void set(T value) {
        this.value = value;
    }
}
