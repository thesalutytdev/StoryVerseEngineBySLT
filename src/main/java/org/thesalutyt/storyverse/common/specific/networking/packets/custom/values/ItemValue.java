package org.thesalutyt.storyverse.common.specific.networking.packets.custom.values;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ItemValue extends Value<ItemStack> {
    public ItemValue(ItemStack value) {
        super(value);
    }

    public ItemValue(PacketBuffer buffer) {
        super(buffer.readItem());
    }

    public void setValue(Object value) {
        if (!(value instanceof ItemStack)) return;
        this.set((ItemStack) value);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeItem(this.getValue());
    }

    @Override
    public ItemStack tryParse(PacketBuffer buffer) {
        if (buffer.readItem() != getValue()) return null;
        return getValue();
    }

    @Override
    public Value<ItemStack> get() {
        return this;
    }

    @Override
    public ItemStack getValue() {
        return this.value;
    }
}
