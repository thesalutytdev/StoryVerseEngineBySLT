package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;

public class StringValue extends Value<String> {
    public StringValue(String value) {
        super(value);
    }

    public StringValue(PacketBuffer buffer) {
        super(buffer.readUtf());
    }

    public void setValue(Object value) {
        if (!(value instanceof String)) return;
        this.set((String) value);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(this.getValue());
    }

    @Override
    public String tryParse(PacketBuffer buffer) {
        if (!buffer.readUtf().equals(getValue())) return null;
        return getValue();
    }

    @Override
    public Value<String> get() {
        return this;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
