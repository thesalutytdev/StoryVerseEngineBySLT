//package org.thesalutyt.storyverse.common.specific.networking;
//
//import java.util.function.Supplier;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.Entity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.network.PacketBuffer;
//import net.minecraftforge.fml.network.NetworkEvent;
//
//public class ClientPacket {
//    public CompoundNBT nbt;
//    public int uuid;
//
//    public ClientPacket(CompoundNBT nbt, int uuid) {
//        this.nbt = nbt;
//        this.uuid = uuid;
//    }
//
//    public ClientPacket(PacketBuffer buffer) {
//        this.nbt = buffer.func_150793_b();
//        this.uuid = buffer.readInt();
//    }
//
//    public void encode(PacketBuffer buffer) {
//        buffer.(this.nbt);
//        buffer.writeInt(this.uuid);
//    }
//
//    public void handle(Supplier<NetworkEvent.Context> context) {
//        ((NetworkEvent.Context)context.get()).enqueueWork(() -> {
//            Minecraft mc = Minecraft.getInstance();
//            Entity entity = mc.field_71441_e.func_73045_a(this.uuid);
//            if (entity != null) {
//                String[] nbtKeys = (String[])this.nbt.func_150296_c().toArray(new String[0]);
//                String[] var4 = nbtKeys;
//                int var5 = nbtKeys.length;
//
//                for(int var6 = 0; var6 < var5; ++var6) {
//                    String key = var4[var6];
//                    entity.getPersistentData().func_218657_a(key, this.nbt.func_74781_a(key));
//                }
//
//            }
//        });
//        ((NetworkEvent.Context)context.get()).setPacketHandled(true);
//    }
//}
