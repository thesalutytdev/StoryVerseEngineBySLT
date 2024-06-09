package org.thesalutyt.storyverse.common.entities.npcNOTWORK;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NPCRenderer extends GeoEntityRenderer<NPCEntity> {
    public NPCRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NPCModel());
    }

    public ResourceLocation getTextureLocation(NPCEntity instance) {
        String path = instance.getPersistentData().getString("texturePath");
        return this.parsePath(path.equals("") ? "storyverse:textures/entity/npc" : path.toLowerCase());
    }

    public ResourceLocation parsePath(String path) {
        String modId = "storyverse";
        String name = "textures/entity/npc";
        if (path.contains(":")) {
            modId = path.substring(0, path.indexOf(":"));
        }

        name = path.substring(path.indexOf(":") + 1);
        return new ResourceLocation(modId, name + (name.endsWith(".png") ? "" : ".png"));
    }

//    public void render(NPCEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
//        try {
//            stack.clear();
//            super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
//            stack.pushPose();
//        } catch (Exception var8) {
//            var8.printStackTrace();
//        }
//
//    }
}
