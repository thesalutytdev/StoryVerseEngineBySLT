package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.values.Value;

import java.util.*;
import java.util.function.Supplier;

public class CustomPacket extends IPacket {
    public static HashMap<UUID, CustomPacket> packets = new HashMap<>();
    public ArrayList<Value<?>> values = new ArrayList<>();
    public ArrayList<BaseFunction> onHandle = new ArrayList<>();
    public Runnable onHandleRun;
    private UUID id;

    public CustomPacket(Value<?>... values) {
        Collections.addAll(this.values, values);
        id = UUID.randomUUID();
        packets.put(id, this);
    }

    public CustomPacket(ArrayList<Value<?>> values) {
        this.values = values;
        id = UUID.randomUUID();
        packets.put(id, this);
    }

    public CustomPacket(PacketBuffer buffer) {
        CustomPacket packet = packets.get(buffer.readUUID());
        for (Value<?> v : packet.values) {
            if (v == null) return;
            v.setValue(v.tryParse(buffer));
            this.values.add(v);
        }
    }

    public void addOnHandle(ArrayList<BaseFunction> function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onHandle.addAll(function);
        });
    }

    public void addOnHandle(Runnable runnable) {
        onHandleRun = runnable;
    }

    public void encode(PacketBuffer buffer) {
        for (Value<?> value : values) {
            value.encode(buffer);
        }
        buffer.writeUUID(id);
    }

    private void runBaseFunctions(Supplier<NetworkEvent.Context> context) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction f : onHandle) {
                Context ctx = Context.getCurrentContext();
                Object[] args = new Object[values.size() + 1];
                if (context.get().getSender() == null) return;
                args[0] = Objects.requireNonNull(context.get().getSender()).getName().getContents();
                for (int i = 0; i < values.size(); i++) {
                    args[i + 1] = values.get(i).get();
                }
                f.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(), args);
            }
        });
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (onHandleRun != null) onHandleRun.run();
            if (onHandle != null) runBaseFunctions(context);
        });
        context.get().setPacketHandled(true);
    }

    public UUID getId() {
        return id;
    }
}
