package org.thesalutyt.storyverse.common.entities.npc;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NPCRender extends GeoEntityRenderer<NPCEntity> {
    public NPCRender(EntityRendererManager renderManager) {
        super(renderManager, new NPCModel());
    }

    public ResourceLocation getEntityTexture(NPCEntity entity) {
        String path = entity.getTexturePath();
        return parsePath(path == "" ? "storyverse:textures/entity/npc" : path.toLowerCase());
    }

    public ResourceLocation parsePath(String path) {
        String id = path;
        String modId = "storyverse";
        String name = "textures/entity/npc";
        if(id.indexOf(":") != -1) {
            modId = id.substring(0,id.indexOf(":"));
        }
        name = id.substring(id.indexOf(":")+1);
        return new ResourceLocation(modId,name+(name.endsWith(".png") ? "" : ".png"));
    }
}
