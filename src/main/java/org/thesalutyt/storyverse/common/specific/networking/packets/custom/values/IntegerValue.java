package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;

public class IntegerValue extends Value<Integer> {
    public IntegerValue(int value) {
        super(value);
    }

    public IntegerValue(PacketBuffer buffer) {
        super(buffer.readInt());
    }

    public void setValue(Object value) {
        if (!(value instanceof Integer)) return;
        this.set((Integer) value);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.getValue());
    }

    @Override
    public Integer tryParse(PacketBuffer buffer) {
        if (buffer.readInt() != getValue()) return null;
        return getValue();
    }

    @Override
    public Value<Integer> get() {
        return this;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
