package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Sounds extends ScriptableObject implements EnvResource {
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
    public static void playSound(String sound, Double volume, Double pitch) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(sound));
        playSoundGlobal(soundEvent, volume.floatValue(), pitch.floatValue());
    }
    public static void putIntoScope (Scriptable scope) {
        Sounds ef = new Sounds();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method playSound = Sounds.class.getMethod("playSound", String.class, Double.class, Double.class);
            methodsToAdd.add(playSound);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("sound", scope, ef);
    }


    @Override
    public String getResourceId() {
        return "Sounds";
    }

    @Override
    public String getClassName() {
        return "Sounds";
    }
}
