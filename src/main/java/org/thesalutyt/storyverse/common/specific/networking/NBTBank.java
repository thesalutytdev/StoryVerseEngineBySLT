//package org.thesalutyt.storyverse.common.specific.networking;
//
//import java.util.Set;
//import java.util.UUID;
//import net.minecraft.entity.Entity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraftforge.fml.network.PacketDistributor;
//
//public class NBTBank {
//    private final CompoundNBT clientData = new CompoundNBT();
//
//    public NBTBank() {
//    }
//
//    public void postOnClient(String nbt, Object val, Type type) {
//        switch (type) {
//            case STRING:
//                this.clientData.putString(nbt, (String)val);
//                break;
//            case DOUBLE:
//                this.clientData.putDouble(nbt, (Double)val);
//                break;
//            case INT:
//                this.clientData.putInt(nbt, (Integer)val);
//                break;
//            case UUID:
//                this.clientData.putUUID(nbt, (UUID)val);
//        }
//
//    }
//
//    public void flush(Entity entity) {
//        Set<String> keys = this.clientData.getAllKeys();
//        if (keys.size() != 0) {
//            String[] clientKeys = (String[])keys.toArray(new String[0]);
//            ClientPacket clientPacket = new ClientPacket(this.clientData, entity.func_145782_y());
//            Networking.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> {
//                return entity;
//            }), clientPacket);
//            String[] var5 = clientKeys;
//            int var6 = clientKeys.length;
//
//            for(int var7 = 0; var7 < var6; ++var7) {
//                String cKey = var5[var7];
//                this.clientData.func_82580_o(cKey);
//            }
//
//        }
//    }
//
//    public static enum Type {
//        STRING,
//        DOUBLE,
//        INT,
//        UUID;
//
//        private Type() {
//        }
//    }
//}
