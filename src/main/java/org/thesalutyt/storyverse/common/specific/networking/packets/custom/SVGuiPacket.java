package org.thesalutyt.storyverse.common.specific.networking.packets.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent;
import org.thesalutyt.storyverse.api.screen.CustomizableGui;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class SVGuiPacket {
    public static HashMap<UUID, CustomizableGui> guis = new HashMap<>();
    private UUID id;
    private CustomizableGui gui;

    public SVGuiPacket(CustomizableGui gui) {
        if (!FMLEnvironment.dist.isClient()) return;
        this.gui = gui;
        this.id = UUID.randomUUID();
        guis.put(id, gui);
    }

    public SVGuiPacket(PacketBuffer buffer) {
        if (!FMLEnvironment.dist.isClient()) return;
        this.id = buffer.readUUID();
        this.gui = guis.get(id);
    }

    public void encode(PacketBuffer buffer) {
        if (!FMLEnvironment.dist.isClient()) return;
        buffer.writeUUID(id);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        if (!FMLEnvironment.dist.isClient()) return;
        System.out.println("Handled gui");
        context.get().enqueueWork(() -> {
            try {
                System.out.println(context.get().getDirection().toString());
                System.out.println(id);
                System.out.println(guis.get(id));
                System.out.println(gui);
                System.out.println(context.get().getSender());
                Minecraft.getInstance().setScreen(gui);
            } catch (Exception e) {
                new ErrorPrinter(e);
                context.get().setPacketHandled(false);
            }
        });
        context.get().setPacketHandled(true);
    }

//    public static String guiToJson(CustomizableGui gui) {
//        HashMap<String, ArrayList<?>> widgets = new HashMap<>();
//
//        widgets.put("buttons", gui.buttons);
//        widgets.put("labels", gui.labels);
//        widgets.put("images", gui.images);
//        widgets.put("circleRect", gui.circleRect);
//        widgets.put("entities", gui.entities);
//
//        HashMap<String, String> data = new HashMap<>();
//
//        data.put("widgets", Serializer.mapToJson(widgets));
//
//        data.put("width", String.valueOf(gui.width));
//        data.put("height", String.valueOf(gui.height));
//        data.put("background", gui.background);
//        data.put("title", gui.title);
//        data.put("renderBackground", String.valueOf(gui.renderBG));
//        data.put("mouseX", String.valueOf(gui.gMouseX));
//        data.put("mouseY", String.valueOf(gui.gMouseY));
//
//        return Serializer.mapToJson(data);
//    }
//
//    public static CustomizableGui parse(String json) {
//        CustomizableGui gui = new CustomizableGui();
//        return gui;
//    }
}
