package org.thesalutyt.storyverse.api.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

public class Camera implements EnvResource {
    private static final Minecraft mc = Minecraft.getInstance();
    private Entity camera = mc.getCameraEntity();
    private Entity cameraEntity;
    private Goal moveGoal;
    public double posX = 0.0;
    public double posY = 0.0;
    public double posZ = 0.0;
    public double rotX = 0.0;
    public double rotY = 0.0;
    public String type = "undef";

    public Camera() {

    }

    public Camera setCameraEntity(Entity entity) {
        mc.setCameraEntity(entity);
        return this;
    }
    public void setCameraPos(double x, double y, double z) {
        this.camera.setPos(x, y, z);
    }

    public void setCameraPos(double[] pos) {
        this.camera.setPos(pos[0], pos[1], pos[2]);
    }
    public void setCameraPos(BlockPos pos) {
         this.camera.setPos(pos.getX(), pos.getY(), pos.getZ());
    }
    public void resetCamera(PlayerEntity player) {
        this.cameraEntity = null;
    }

    public void moveCamera(BlockPos pos, float speed){
        CreatureEntity cameraEntity = (CreatureEntity) camera;
        cameraEntity.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), speed);
    }

    @Override
    public String getResourceId() {
        return "Camera";
    }
}

