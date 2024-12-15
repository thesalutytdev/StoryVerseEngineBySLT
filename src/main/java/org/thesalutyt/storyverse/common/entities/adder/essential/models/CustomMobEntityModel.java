package org.thesalutyt.storyverse.common.entities.adder.essential.models;

import net.minecraft.util.ResourceLocation;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomMobEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CustomMobEntityModel extends AnimatedGeoModel<CustomMobEntity> {
    public ResourceLocation parsePath(String path) {
        String id = path;
        String modId = "storyverse";
        String name = "animations/npc.animation.json";
        if(id.indexOf(":") != -1) {
            modId = id.substring(0,id.indexOf(":"));
        }
        name = id.substring(id.indexOf(":")+1);
        return new ResourceLocation(modId,name+(name.endsWith(".json") ? "" : ".json"));
    }

    public ResourceLocation parsePathTexture(String path) {
        String id = path;
        String modId = "storyverse";
        String name = "textures/entity/npc";
        if(id.indexOf(":") != -1) {
            modId = id.substring(0,id.indexOf(":"));
        }
        name = id.substring(id.indexOf(":")+1);
        return new ResourceLocation(modId,name+(name.endsWith(".png") ? "" : ".png"));
    }

    @Override
    public ResourceLocation getModelLocation(CustomMobEntity object) {
        String path = object.getModelPath();
        ResourceLocation modelPath = parsePath(path == "" ? "geo/alex.geo" : path);
        return modelPath;
    }

    @Override
    public ResourceLocation getTextureLocation(CustomMobEntity object) {
        String path = object.getTexturePath();
        return parsePathTexture(path == "" ? "storyverse:textures/entity/npc" : path.toLowerCase());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CustomMobEntity animatable) {
        String path = animatable.getAnimationPath();
        ResourceLocation animPath = parsePath(path == "" ? "animations/npc.animation" : path);
        return animPath;
    }

    @Override
    public void setCustomAnimations(CustomMobEntity animatable, int instanceId, AnimationEvent animationEvent) {
        try {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
            IBone head = this.getAnimationProcessor().getBone("Head");
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
            if (head != null) {
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}