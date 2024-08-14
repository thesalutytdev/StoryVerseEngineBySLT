package org.thesalutyt.storyverse.api.camera.entityCamera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.*;

public class Cutscene implements EnvResource {
    private Camera camera;
    private static final Minecraft mc = Minecraft.getInstance();
    private static final WorldWrapper world = new WorldWrapper();
    private Entity cameraEntity = mc.cameraEntity;
    private BlockPos beforeCutscenePosition;
    private MobController cameraEntityController;
    private Script script = new Script();
    private CameraType cameraType;
    private PlayerEntity player;
    private GameType beforeGameMode;
    public Cutscene() {

    }

    @Documentate(
            desc = "Makes player enter cutscene"
    )
    public Cutscene enterCutscene(PlayerEntity player, EntityType cameraEntity, BlockPos pos, CameraType type) {
        this.beforeCutscenePosition = player.blockPosition();
        this.cameraEntityController = new MobController(pos, cameraEntity);
        mc.setCameraEntity(cameraEntityController.getEntity());
        cameraEntityController.addEffect(Effects.INVISIBILITY, 999999999, 99);
        // cameraEntityController.setNoAI(true);
        cameraEntityController.moveTo(pos, 1);
        this.cameraType = type;
        this.player = player;
        this.beforeGameMode = GameType.SURVIVAL;
        player.setGameMode(GameType.SPECTATOR);
        Server.execute("/gamemode spectator @s");

        return this;
    }

    @Documentate(
            desc = "Makes camera entity move"
    )
    public CameraResult move(BlockPos pos, Integer speed) {
        if (this.cameraType == CameraType.FULL || this.cameraType == CameraType.POS_ONLY || this.cameraType == CameraType.ROT_ONLY) {
            return CameraResult.CAMERA_FAILED;
        } else {
            this.cameraEntityController.setNoAI(false);
            WalkTask walkTask = cameraEntityController.moveTo(pos, speed);

            return CameraResult.CAMERA_SUCCESS;
        }
    }

    @Documentate(
            desc = "Stops camera entity from moving"
    )
    public CameraResult stopMove() {
        this.cameraEntityController.stopMove();
        this.cameraEntityController.setNoAI(true);
        return CameraResult.CAMERA_SUCCESS;
    }

    @Documentate(
            desc = "Makes player exit cutscene"
    )
    public CameraResult exitCutscene() {
        this.mc.cameraEntity = (ClientPlayerEntity) Server.getPlayer();
        this.cameraEntityController.remove();
        this.player.setPos(this.beforeCutscenePosition.getX(),
                this.beforeCutscenePosition.getY(),
                this.beforeCutscenePosition.getZ());
        this.player.setGameMode(beforeGameMode);
        Server.execute("/gamemode survival @s");

        return CameraResult.CAMERA_SUCCESS;
    }

    @Documentate(
            desc = "Sets camera entity head rotation"
    )
    public CameraResult setHeadRotation(Double x, Double y) {
        this.cameraEntityController.setHeadRotation(x, y);

        return CameraResult.CAMERA_SUCCESS;
    }

    public CameraResult setHeadRotation(Double[] rotation) {
        this.cameraEntityController.setHeadRotation(rotation);

        return CameraResult.CAMERA_SUCCESS;
    }
    public CameraType getCameraType() {
        return this.cameraType;
    }
    public static void putIntoScope(Scriptable scope) {
        scope.put("cutscene", scope, new Cutscene());
    }
    @Documentate(
            desc = "Returns camera entity controller"
    )
    public MobController getCameraController() {
        return this.cameraEntityController;
    }

    @Override
    public String getResourceId() {
        return "Cutscene";
    }
}
