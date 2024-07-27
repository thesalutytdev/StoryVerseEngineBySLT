package org.thesalutyt.storyverse.api.camera.cutscene;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.api.camera.cutscene.instance.CutsceneInstance;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

public class Cutscene extends CutsceneInstance {
    public double x;
    public double y;
    public double z;
    public BlockPos pos;
    public CutsceneType type;
    public boolean active;
    public ServerPlayerEntity player;
    public GameType finishMode = GameType.SURVIVAL;
    public BlockPos finishPos;

    public Cutscene(String playerName, CutsceneArguments args) {
        super(playerName, args);
        this.x = args.x;
        this.y = args.y;
        this.z = args.z;
        this.pos = new BlockPos(x, y, z);
        this.type = args.type;
        this.ticksPassed = 0;
        this.arguments = args;
        this.active = false;
        this.player = Server.getPlayerByName(playerName);
        CutsceneManager.cutscenes.put(playerName, this);
        SVEnvironment.Root.inCutscene = false;
    }

    public void start() {
        this.active = true;
        SVEnvironment.Root.inCutscene = true;
        this.player.setGameMode(GameType.SPECTATOR);
    }

    public void finish() {
        this.active = false;
        SVEnvironment.Root.inCutscene = false;
        this.player.setGameMode(finishMode);
        if (finishPos != null) {
            this.player.teleportToWithTicket(finishPos.getX(), finishPos.getY(), finishPos.getZ());
        }
        CutsceneManager.cutscenes.remove(this.player.getName().getContents());
    }

    public void tick() {

    }

    public static void tick(Integer tick) {
        try {
            if (CutsceneManager.cutscenes.containsKey(Player.getPlayerName())) {
                ServerPlayerEntity player = Player.getPlayer();
                Cutscene cutscene = CutsceneManager.cutscenes.get(player.getName().getContents());
                if (!cutscene.active) {
                    return;
                }
                player.setGameMode(GameType.SPECTATOR);
                switch (cutscene.type) {
                    case FULL:
                        player.teleportToWithTicket(cutscene.x, cutscene.y, cutscene.z);
                        setHeadRotation(player, cutscene.arguments.rotX, cutscene.arguments.rotY);
                        break;
                    case POS_ONLY:
                        player.teleportToWithTicket(cutscene.x, cutscene.y, cutscene.z);
                        break;
                    case ROT_ONLY:
                        setHeadRotation(player, cutscene.arguments.rotX, cutscene.arguments.rotY);
                        break;
                    case NULL:
                    default:
                        break;
                }
                cutscene.ticksPassed++;
            }
        } catch (Exception e) {
            new ErrorPrinter(e);
        }
    }
}
