package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class BlockPosValue extends Value<net.minecraft.util.math.BlockPos> {
    public BlockPosValue(net.minecraft.util.math.BlockPos value) {
        super(value);
    }

    public BlockPosValue(PacketBuffer buffer) {
        super(buffer.readBlockPos());
    }

    public void setValue(Object value) {
        if (!(value instanceof net.minecraft.util.math.BlockPos)) return;
        this.set((BlockPos) value);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(this.getValue());
    }

    @Override
    public BlockPos tryParse(PacketBuffer buffer) {
        if (buffer.readBlockPos() != getValue()) return null;
        return getValue();
    }

    @Override
    public Value<net.minecraft.util.math.BlockPos> get() {
        return this;
    }

    @Override
    public net.minecraft.util.math.BlockPos getValue() {
        return this.value;
    }
}
