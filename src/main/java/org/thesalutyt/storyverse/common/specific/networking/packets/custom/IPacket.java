package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.values.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class IPacket {
    public static HashMap<UUID, IPacket> packets = new HashMap<>();
    protected ArrayList<Value<?>> values = new ArrayList<>();
    protected UUID id;

    public IPacket(Value<?>... values) {
        Collections.addAll(this.values, values);
        id = UUID.randomUUID();
        packets.put(id, this);
    }

    public IPacket(ArrayList<Value<?>> values) {
        this.values = values;
        id = UUID.randomUUID();
        packets.put(id, this);
    }

    public abstract void encode(PacketBuffer buffer);
    public abstract void handle(Supplier<NetworkEvent.Context> context);

    protected void tryParseValues(PacketBuffer buffer) {
        IPacket packet = packets.get(buffer.readUUID());
        for (Value<?> v : packet.values) {
            if (v == null) return;
            v.setValue(v.tryParse(buffer));
            this.values.add(v);
        }
    }

    public UUID getId() {
        return id;
    };

    public ArrayList<Value<?>> getValues() {
        return values;
    }
}
