package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.util.math.BlockPos;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.common.specific.networking.Networking;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.values.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class PacketJS extends ScriptableObject implements EnvResource, JSResource {
    public CustomPacket packet;
    public String name;
    public int stage = 0;
    public boolean built = false;
    public ArrayList<Value<?>> values = new ArrayList<>();
    public ArrayList<BaseFunction> onHandle = new ArrayList<>();

    public PacketJS create(String name) {
        this.name = name;
        this.stage++;
        return this;
    }

    public PacketJS onHandle(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onHandle.add(function);
            stage++;
        });
        return this;
    }

    public PacketJS addValue(String type, Object value) {
        switch (type.toLowerCase()) {
            case "string":
                values.add(new StringValue(String.valueOf(value)));
                break;
            case "integer":
                values.add(new IntegerValue((int) value));
                break;
            case "boolean":
                values.add(new BooleanValue((boolean) value));
                break;
            case "blockpos":
                values.add(new BlockPosValue((BlockPos) value));
                break;
            case "itemstack":
                values.add(new ItemValue(JSItem.getStack((String) value)));
                break;
            case "double":
                values.add(new DoubleValue((double) value));
                break;
            default:
                throw new RuntimeException("Unknown type: " + type);
        }
        stage++;
        return this;
    }

    public PacketJS build() {
        if (stage < 2) throw new RuntimeException("Not enough values set");
        packet = new CustomPacket(values);
        packet.onHandle = onHandle;
        built = true;
        return this;
    }

    public PacketJS send(String player) {
        if (!built) throw new RuntimeException("Not built");
        Networking.sendToPlayer(packet, Server.getPlayerByName(player));
        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        PacketJS ef = new PacketJS();
        ef.setParentScope(scope);

        try {
            Method create = PacketJS.class.getMethod("create", String.class);
            methodsToAdd.add(create);

            Method onHandle = PacketJS.class.getMethod("onHandle", BaseFunction.class);
            methodsToAdd.add(onHandle);

            Method addValue = PacketJS.class.getMethod("addValue", String.class, Object.class);
            methodsToAdd.add(addValue);

            Method build = PacketJS.class.getMethod("build");
            methodsToAdd.add(build);

            Method send = PacketJS.class.getMethod("send", String.class);
            methodsToAdd.add(send);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("packet", scope, ef);
    }

    @Override
    public String getClassName() {
        return "PacketJS";
    }

    @Override
    public String getResourceId() {
        return "PacketJS";
    }
}
