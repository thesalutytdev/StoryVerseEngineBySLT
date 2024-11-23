package org.thesalutyt.storyverse.api.camera.cutscene;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.api.camera.cutscene.instance.AbstractCutscene;
import org.thesalutyt.storyverse.api.camera.cutscene.math.InterpolationCalculator;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.Time;

import java.util.ArrayList;

public class Moving extends AbstractCutscene {
    public static ArrayList<Moving> activeMoving = new ArrayList<>();

    public BlockPos finishPos;
    public double finishRotX;
    public double finishRotY;
    public InterpolationCalculator.Vector3d step;
    public InterpolationCalculator.Vector3d prevPos;
    public double rotStep;
    public Time.ITime time;
    public boolean active;
    public double curRotX;
    public double curRotY;
    public ServerPlayerEntity player;

    public Moving(String playerName, CutsceneArguments args, BlockPos finishPos, double finishRotX, double finishRotY, Time.ITime time) {
        super(playerName, args);
        if (args.type != CutsceneType.MOVING) {
            return;
        }
        this.finishPos = finishPos;
        this.finishRotX = finishRotX;
        this.startPos = new BlockPos(args.x, args.y, args.z);
        this.finishRotY = finishRotY;
        this.step = InterpolationCalculator.interpolate(new InterpolationCalculator.Vector3d(args.x, args.y, args.z),
                new InterpolationCalculator.Vector3d(finishPos.getX(), finishPos.getY(), finishPos.getZ()), time);
        this.rotStep = InterpolationCalculator.interpolate(finishRotX, finishRotY, time);
        this.time = time;
        this.curRotX = args.rotX;
        this.curRotY = args.rotY;
        this.player = Server.getPlayerByName(playerName);
        this.prevPos = new InterpolationCalculator.Vector3d(args.x, args.y, args.z);

        CutsceneManager.movingInstances.put(playerName, this);
    }

    @Override
    public void start() {
        this.active = true;
        SVEnvironment.Root.inMoving = true;
        this.player.setGameMode(GameType.SPECTATOR);
        player.teleportToWithTicket(startPos.getX(), startPos.getY(), startPos.getZ());
        setHeadRotation(player, rotX, rotY);
        activeMoving.add(this);
    }

    @Override
    public void finish() {
        this.active = false;
        SVEnvironment.Root.inMoving = false;
        this.player.setGameMode(GameType.SURVIVAL);
        CutsceneManager.movingInstances.remove(this.player.getName().getContents());
        activeMoving.remove(this);
    }

    public void checkPlayer(double x, double y, double z) {
        if (this.player.getX() != x || this.player.getY() != y || this.player.getZ() != z) {
            setPosition(this.player, x, y, z);
        }
    }

    public void tick() {

    }

    private void step() {
        Moving moving = this;

        if (this.player.blockPosition() == moving.finishPos) {
            moving.finish();
            return;
        }

        if (!moving.active) {
            return;
        }
        if (moving.ticksPassed >= moving.time.ticks) {
            moving.finish();
            return;
        }
        double curX = moving.prevPos.getX();
        double curY = moving.prevPos.getY();
        double curZ = moving.prevPos.getZ();
        double nextX = curX + moving.step.x;
        double nextY = curY + moving.step.y;
        double nextZ = curZ + moving.step.z;

        moving.checkPlayer(nextX, nextY, nextZ);

        moving.curRotX += moving.rotStep;
        moving.curRotY += moving.rotStep;

        moving.prevPos = new InterpolationCalculator.Vector3d(nextX, nextY, nextZ);

        if (nextX != curX || nextY != curY || nextZ != curZ) {
            setHeadRotation(moving.player, moving.curRotX, moving.curRotY);
            setPosition(moving.player, nextX, nextY, nextZ);
        }
        moving.ticksPassed++;
        if (moving.ticksPassed >= moving.time.ticks || moving.player.blockPosition() == moving.finishPos) {
            moving.finish();
        }
    }

    public static void tick(Integer tick) {
        try {
            activeMoving.forEach(Moving::step);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
