package org.thesalutyt.storyverse.api;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.thesalutyt.storyverse.api.camera.Camera;
import org.thesalutyt.storyverse.api.camera.Cutscene;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;
import org.thesalutyt.storyverse.fs_environment.Environment;
import org.thesalutyt.storyverse.fs_environment.instances.ScriptInstance;

public class SVEnvironment extends Environment {
    public static String envId = "storyverse";
    public static String version = "1";
    public Environment env = this;
    public Player player;
    public MobController mobController;
    public Script script;
    public WalkTask walkTask;
    public Camera camera;
    public Cutscene cutscene;
    public WorldWrapper world;
    public ScriptInstance scriptInstance;
    public Sounds sounds;
    public LegacyEventManager eventManager;
    public SVEnvironment(ServerPlayerEntity player, ScriptInstance instance) {
        this.player = new Player(player);
        this.script = new Script();
        this.eventManager = new LegacyEventManager();
        this.cutscene = new Cutscene();
        this.camera = new Camera();
        this.world = new WorldWrapper();
    }
    public static class Root {
        public static Integer ticks = 0;
        public static void tick() {
            ticks++;
        }
        public static void resetTick() {
            ticks = 0;
        }
        public static void setTicks(Integer new_ticks_amount) {
            ticks = new_ticks_amount;
        }
        public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {

        }
        public static void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
            SVEnvironment.Root.resetTick();
        }
        public static Entity getCameraEntity() {
            return Minecraft.getInstance().cameraEntity;
        }
    }
}
