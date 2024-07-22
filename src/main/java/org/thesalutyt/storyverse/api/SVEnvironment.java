package org.thesalutyt.storyverse.api;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mozilla.javascript.NativeArray;
import org.thesalutyt.storyverse.api.camera.Camera;
import org.thesalutyt.storyverse.api.camera.Cutscene;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.action.Action;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;

import java.util.Arrays;
import java.util.List;

public class SVEnvironment{
    public static String envId = "storyverse";
    public static String version = "1";
    public Player player;
    public MobController mobController;
    public Script script;
    public WalkTask walkTask;
    public Camera camera;
    public Cutscene cutscene;
    public WorldWrapper world;
    public Sounds sounds;
    public LegacyEventManager eventManager;
    public SVEnvironment(ServerPlayerEntity player) {
        this.player = new Player(player);
        this.script = new Script();
        this.eventManager = new LegacyEventManager();
        this.cutscene = new Cutscene();
        this.camera = new Camera();
        this.world = new WorldWrapper();
    }
    public static class Root {
        public static Integer ticks = 0;
        public static Boolean isDay;
        public static Boolean started = false;
        public static void tick() {
            ticks++;
            if (Action.onEveryTick.isEmpty()) {
                return;
            }
//            if (WorldWrapper.isDay() != isDay) {
//                isDay = WorldWrapper.isDay();
//                if (WorldWrapper.isDay()) {
//                    WorldWrapper.runOnTimeChange("day");
//                } else {
//                    WorldWrapper.runOnTimeChange("night");
//                }
//            }
            Action.runOnTick(ticks);
        }
        public static void resetTick() {
            ticks = 0;
        }
        public static void setTicks(Integer new_ticks_amount) {
            ticks = new_ticks_amount;
        }
        public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
            if (ScriptProperties.worldStarterScript == null || started) {
                return;
            }
            if (started) {
                return;
            }
            // isDay = WorldWrapper.isDay();
            Script.runScript(ScriptProperties.worldStarterScript);
            started = true;
        }
        public static void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
            SVEnvironment.Root.resetTick();
        }
        public static Entity getCameraEntity() {
            return Minecraft.getInstance().cameraEntity;
        }
    }
}
