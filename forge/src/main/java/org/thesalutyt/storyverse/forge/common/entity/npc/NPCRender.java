package org.thesalutyt.storyverse.forge.common.entity.npc;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.ArrayList;

public class NPCRender extends GeoEntityRenderer<NPCEntity> {
    public static ArrayList<NPCRender> renders = new ArrayList<>();

    public NPCRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NPCModel());
        renders.add(this);
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
