package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

public class Sounds implements EnvResource {
    @Documentate(
            desc = "Plays sound for everyone"
    )
    public static void playSoundGlobal(SoundEvent event, float volume, float pitch){
        for (ServerPlayerEntity entity: Server.getPlayers()){
            entity.connection.send(new SPlaySoundPacket(event.getLocation(), SoundCategory.MASTER, entity.position(), volume, pitch));
        }
    }
    public static void playSoundGlobal(SoundEvent event, SoundCategory category, float volume, float pitch){
        for (ServerPlayerEntity entity: Server.getPlayers()){
            entity.connection.send(new SPlaySoundPacket(event.getLocation(), category, entity.position(), volume, pitch));
        }
    }

    @Override
    public String getResourceId() {
        return "Sounds";
    }
}
