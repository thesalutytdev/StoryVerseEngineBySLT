package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.thesalutyt.storyverse.api.features.Script;

import java.util.function.Supplier;

public class ScriptExecutionPacket {
    public String script;

    public ScriptExecutionPacket(String script) {
        this.script = script;
    }

    public ScriptExecutionPacket(PacketBuffer buffer) {
        this.script = buffer.readUtf();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(script);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (script != null) new Script().run(script);
        });
    }

    public String getScript() {
        return script;
    }
}
