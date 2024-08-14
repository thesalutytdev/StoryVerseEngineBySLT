package org.thesalutyt.storyverse.common.dimension.mover;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Mover {
    private ServerPlayerEntity player;
    public static RegistryKey<World> toServerWorld(String dimension) {
        switch (dimension) {
            case "overworld": {
                return ServerWorld.OVERWORLD;
            }
            case "nether": {
                return ServerWorld.NETHER;
            }
            case "end": {
                return ServerWorld.END;
            }
        }
        return null;
    }
}
