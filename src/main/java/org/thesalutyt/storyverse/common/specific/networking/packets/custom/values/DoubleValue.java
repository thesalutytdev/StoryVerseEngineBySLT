package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;

public class DoubleValue extends Value<Double> {
    public DoubleValue(double value) {
        super(value);
    }

    public void setValue(Object value) {
        if (!(value instanceof Double)) return;
        this.set((Double) value);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeDouble(this.getValue());
    }

    @Override
    public Double tryParse(PacketBuffer buffer) {
        if (buffer.readDouble() != getValue()) return null;
        return getValue();
    }

    public DoubleValue(PacketBuffer buffer) {
        super(buffer.readDouble());
    }

    @Override
    public Value<Double> get() {
        return this;
    }

    @Override
    public Double getValue() {
        return this.value;
    }
}
