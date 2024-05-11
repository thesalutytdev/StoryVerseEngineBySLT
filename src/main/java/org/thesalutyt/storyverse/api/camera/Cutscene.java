package org.thesalutyt.storyverse.api.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
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
    public Cutscene() {

    }

    @Documentate(
            desc = "Makes player enter cutscene"
    )
    public Cutscene enterCutscene(PlayerEntity player, EntityType cameraEntity, BlockPos pos, CameraType type) {
        this.beforeCutscenePosition = player.blockPosition();
        this.cameraEntityController = new MobController(
                world.spawnEntity(pos,
                        cameraEntity)
        );
        mc.setCameraEntity(cameraEntityController.getEntity());
        new Thread(() -> {cameraEntityController.setInvisible(true);
            cameraEntityController.addEffect(Effects.INVISIBILITY, 999999, 99);
            cameraEntityController.setNoAI(true);
        }).start();
        this.cameraType = type;
        this.player = player;
        player.setGameMode(GameType.SPECTATOR);
        Server.execute(player, "/gamemode @s spectator");

        return this;
    }

    @Documentate(
            desc = "Makes camera entity move"
    )
    public CameraResult move(BlockPos pos, Integer speed) {
        if (this.cameraType == CameraType.FULL || this.cameraType == CameraType.POS_ONLY || this.cameraType == CameraType.ROT_ONLY) {
            return CameraResult.CAMERA_FAILED;
        } else {
            new Thread(() -> {
                this.cameraEntityController.setNoAI(false);

                WalkTask walkTask = new WalkTask(pos,
                        this.cameraEntityController.getMobEntity(),
                        this.cameraEntityController.getController());
                script.waitWalkEnd(walkTask);
                this.cameraEntityController.setNoAI(true);
            }).start();

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
        this.mc.cameraEntity = player;
        this.cameraEntityController.kill();
        this.player.setPos(this.beforeCutscenePosition.getX(),
                this.beforeCutscenePosition.getY(),
                this.beforeCutscenePosition.getZ());

        return CameraResult.CAMERA_SUCCESS;
    }

    @Documentate(
            desc = "Sets camera entity head rotation"
    )
    public CameraResult setHeadRotation(float x, float y) {
        this.cameraEntityController.setHeadRotation(x, y);

        return CameraResult.CAMERA_SUCCESS;
    }

    public CameraResult setHeadRotation(float[] rotation) {
        this.cameraEntityController.setHeadRotation(rotation);

        return CameraResult.CAMERA_SUCCESS;
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
