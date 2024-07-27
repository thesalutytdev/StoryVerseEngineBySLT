package org.thesalutyt.storyverse.api.camera.cutscene.instance;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneArguments;

public abstract class CutsceneInstance {
    public String player;
    public BlockPos startPos;
    public BlockPos finishPos;
    public double rotX;
    public double rotY;
    public int ticksPassed;
    public CutsceneArguments arguments;

    public CutsceneInstance(String player, CutsceneArguments args) {
        this.player = player;
        this.arguments = args;
    }

    public static void setHeadRotation(ServerPlayerEntity player, double rotX, double rotY) {
        player.xRot = (float) rotX;
        player.xRotO = (float) rotX;
        player.yHeadRot = (float) rotY;
        player.yBodyRot = (float) rotY;
        player.yRot = (float) rotY;
        player.yRotO = (float) rotY;
        player.yHeadRotO = (float) rotY;
        player.yBodyRotO = (float) rotY;
    }

    public static void setPosition(ServerPlayerEntity player, double x, double y, double z) {
        player.setPos(x, y, z);
        player.teleportToWithTicket(x, y, z);
        player.teleportTo(x, y, z);
    }

    public abstract void start();

    public abstract void finish();

    public abstract void tick();
}
