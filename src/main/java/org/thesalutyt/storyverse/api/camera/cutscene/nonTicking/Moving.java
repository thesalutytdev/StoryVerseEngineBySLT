package org.thesalutyt.storyverse.api.camera.cutscene.nonTicking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneArguments;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneType;
import org.thesalutyt.storyverse.api.camera.cutscene.instance.CutsceneInstance;
import org.thesalutyt.storyverse.api.camera.cutscene.math.InterpolationCalculator;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.Time;

public class Moving extends CutsceneInstance {
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
    private final int STEP_TIME = 40;

    private double steps_to_run;

    public Moving(String playerName, CutsceneArguments args, BlockPos finishPos, double finishRotX, double finishRotY, Time.ITime time) {
        super(playerName, args);
        if (args.type != CutsceneType.MOVING) {
            return;
        }
        this.finishPos = finishPos;
        this.finishRotX = finishRotX;
        this.startPos = new BlockPos(args.x, args.y, args.z);
        this.finishRotY = finishRotY;
        this.step = InterpolationCalculator.interpolateNonTicking(new InterpolationCalculator.Vector3d(args.x, args.y, args.z),
                new InterpolationCalculator.Vector3d(finishPos.getX(), finishPos.getY(), finishPos.getZ()), time);
        this.rotStep = InterpolationCalculator.interpolateNonTicking(finishRotX, finishRotY, time);
        this.time = time;
        this.curRotX = args.rotX;
        this.curRotY = args.rotY;
        this.player = Server.getPlayerByName(playerName);
        this.prevPos = new InterpolationCalculator.Vector3d(args.x, args.y, args.z);
        this.steps_to_run = time.milliSeconds / STEP_TIME;

        Manager.movingInstances.put(playerName, this);
    }

    @Override
    public void start() {
        this.active = true;
        this.player.setPos(startPos.getX(), startPos.getY(), startPos.getZ());
        setHeadRotation(player, rotX, rotY);
        this.player.setGameMode(GameType.SPECTATOR);
        EventLoop.getLoopInstance().runTimeout(Moving::runStep, this.STEP_TIME);
    }

    @Override
    public void finish() {
        this.active = false;
        this.steps_to_run = 0;
        this.player.setPos(finishPos.getX(), finishPos.getY(), finishPos.getZ());
        setHeadRotation(player, finishRotX, finishRotY);
        this.player.setGameMode(GameType.SURVIVAL);
        Manager.movingInstances.remove(player.getName().getContents());
    }

    public static void runStep() {
        Moving moving = Manager.movingInstances.get(Player.getPlayerName());

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

        moving.steps_to_run--;

        if (moving.player.blockPosition() == moving.finishPos) {
            moving.finish();
        }

        if (moving.steps_to_run > 0) {
            EventLoop.getLoopInstance().runTimeout(Moving::runStep, moving.STEP_TIME);
        }
    }

    public void checkPlayer(double x, double y, double z) {
        if (this.player.getX() != x || this.player.getY() != y || this.player.getZ() != z) {
            setPosition(this.player, x, y, z);
        }
    }

    @Override
    public void tick() {

    }
}
