package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;

import java.util.function.Supplier;

public class TotemShowPacket {
    private final ItemStack stack;

    public TotemShowPacket(String itemStack) {
        this.stack = JSItem.getStack(itemStack);
    }

    public TotemShowPacket(PacketBuffer buffer) {
        stack = buffer.readItem();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
        });
        context.get().setPacketHandled(true);
    }

    public ItemStack getItemStack() {
        return stack;
    }
}
