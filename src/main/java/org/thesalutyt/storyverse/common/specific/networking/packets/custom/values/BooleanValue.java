package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;

public class BooleanValue extends Value<Boolean> {
    public BooleanValue(boolean value) {
        super(value);
    }

    public BooleanValue(PacketBuffer buffer) {
        super(buffer.readBoolean());
    }

    public void setValue(Object value) {
        if (!(value instanceof Boolean)) return;
        this.set((Boolean) value);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(this.getValue());
    }

    @Override
    public Boolean tryParse(PacketBuffer buffer) {
        if (buffer.readBoolean() != getValue()) return null;
        return getValue();
    }

    @Override
    public Value<Boolean> get() {
        return this;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }
}
