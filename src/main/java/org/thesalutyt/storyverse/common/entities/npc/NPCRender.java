package org.thesalutyt.storyverse.common.entities.npc;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.ArrayList;
import java.util.HashMap;

public class NPCRender extends GeoEntityRenderer<NPCEntity> {
    public static ArrayList<NPCRender> renders = new ArrayList<>();

    public NPCRender(EntityRendererManager renderManager) {
        super(renderManager, new NPCModel());
        renders.add(this);
    }

    public void hold(Hand hand, ItemStack stack) {
        switch (hand) {
            case MAIN_HAND:
                this.mainHand = stack;
                break;
            case OFF_HAND:
                this.offHand = stack;
                break;
            default:
                return;
        }
    }

    public void armor(Integer slot, ItemStack stack) {
        switch (slot) {
            case 0:
                this.helmet = stack;
                break;
            case 1:
                this.chestplate = stack;
                break;
            case 2:
                this.leggings = stack;
                break;
            case 3:
                this.boots = stack;
                break;
            default:
                return;
        }
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
