package org.thesalutyt.storyverse.api;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.thesalutyt.storyverse.api.camera.cutscene.Cutscene;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneManager;
import org.thesalutyt.storyverse.api.camera.cutscene.Moving;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.action.Action;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

public class SVEnvironment{
    public static String envId = "storyverse";
    public static String version = "1";
    public Script script = new Script();
    public WorldWrapper world;
    public SVEnvironment(ServerPlayerEntity player) {
    }
    public static class Root {
        public static Integer ticks = 0;
        public static Boolean isDay;
        public static Boolean started = false;
        public static Boolean inCutscene = false;
        public static Boolean inMoving = false;
        public static void tick() {
            ticks++;
            if (inCutscene) {
                Cutscene.tick(ticks);
            }
            if (inMoving) {
                Moving.tick(ticks);
            }
            if (Action.onEveryTick.isEmpty()) {
                return;
            }
            Action.runOnTick(ticks);
        }
        public static void resetTick() {
            ticks = 0;
        }
        public static void setTicks(Integer new_ticks_amount) {
            ticks = new_ticks_amount;
        }
        public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
            if (ScriptProperties.worldStarterScript == null || started || ScriptProperties.ran) {
                return;
            }
            started = true;
            ScriptProperties.run();
        }
        public static void playerLeft(WorldEvent.Unload event) {
            SVEnvironment.Root.resetTick();
        }
        public static Entity getCameraEntity() {
            return Minecraft.getInstance().cameraEntity;
        }
    }
}
