package org.thesalutyt.storyverse.api.camera.cutscene.nonTicking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneArguments;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneManager;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneType;
import org.thesalutyt.storyverse.api.camera.cutscene.instance.CutsceneInstance;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.Time;

public class Cutscene extends CutsceneInstance {
    public double x;
    public double y;
    public double z;
    public BlockPos pos;
    public CutsceneType type;
    public boolean active;
    public ServerPlayerEntity player;
    public GameType finishMode = GameType.SURVIVAL;
    public Time.ITime time;
    public Integer STEP_TIME = 40;
    public double stepsToRun;
    public BlockPos finishPos;

    public Cutscene(String playerName, CutsceneArguments args, Time.ITime time) {
        super(playerName, args);
        this.x = args.x;
        this.y = args.y;
        this.z = args.z;
        this.pos = new BlockPos(x, y, z);
        this.finishPos = new BlockPos(x, y, z);
        this.type = args.type;
        this.ticksPassed = 0;
        this.arguments = args;
        this.active = false;
        this.player = Server.getPlayerByName(playerName);
        this.time = time;
        Manager.cutscenes.put(playerName, this);
        Manager.default_cutscenes.put(playerName, this);

        this.stepsToRun = time.milliSeconds / STEP_TIME;
    }

    public static void runStep() {
        ServerPlayerEntity player = Player.getPlayer();
        Cutscene cutscene = Manager.default_cutscenes.get(player.getName().getContents());
        if (!cutscene.active) {
            return;
        }
        player.setGameMode(GameType.SPECTATOR);
        switch (cutscene.type) {
            case FULL:
                player.teleportToWithTicket(cutscene.x, cutscene.y, cutscene.z);
                setHeadRotation(player, cutscene.arguments.rotX, cutscene.arguments.rotY);
                cutscene.checkRot();
                break;
            case POS_ONLY:
                player.teleportToWithTicket(cutscene.x, cutscene.y, cutscene.z);
                break;
            case ROT_ONLY:
                setHeadRotation(player, cutscene.arguments.rotX, cutscene.arguments.rotY);
                cutscene.checkRot();
                break;
            case NULL:
            default:
                break;
        }

        cutscene.stepsToRun--;

        if (cutscene.stepsToRun > 0) {
            EventLoop.getLoopInstance().runTimeout(Cutscene::runStep, cutscene.STEP_TIME);
        }
    }

    private void checkRot() {
        if (this.arguments.rotX != player.xRot && this.arguments.rotY != player.yRot) {
            setHeadRotation(this.player, this.arguments.rotX, this.arguments.rotY);
        }
    }

    @Override
    public void start() {
        this.active = true;
        EventLoop.getLoopInstance().runTimeout(Cutscene::runStep, this.STEP_TIME);
    }

    @Override
    public void finish() {
        this.active = false;
        this.stepsToRun = 0;
    }

    @Override
    public void tick() {

    }

    public void setStepTime(Integer stepTime) {
        this.STEP_TIME = stepTime;
    }
}
