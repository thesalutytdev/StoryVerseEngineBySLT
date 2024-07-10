package org.thesalutyt.storyverse.common.entities.npc;

import net.minecraft.util.ResourceLocation;
import org.thesalutyt.storyverse.SVEngine;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.io.File;

public class NPCModel extends AnimatedGeoModel<NPCEntity> {
    @Override
    public ResourceLocation getModelLocation(NPCEntity object) {
        try {
            String path = object.getModelPath();
            File modelFile = getFile(path, AssetType.MODEL);
            return new ResourceLocation("file", modelFile.getAbsolutePath());
        } catch (Exception e) {
            return new ResourceLocation(SVEngine.MOD_ID, "geo/npc.geo.json");
        }
    }

    public File getFile(String path, AssetType type) {
        switch (type) {
            case MODEL:
                String basePath = SVEngine.MODELS_PATH;  // Путь к папке storyverse
                File modelFile = new File(basePath + path);
                // Добавляем расширение .json если его нет
                if (!modelFile.getName().endsWith(".geo.json")) {
                    modelFile = new File(modelFile.getAbsolutePath() + ".geo.json");
                }
                return modelFile;
            case TEXTURE:
                String basePath_ = SVEngine.TEXTURES_PATH;  // Путь к папке storyverse
                File textureFile = new File(basePath_ + path);
                // Добавляем расширение .json если его нет
                if (!textureFile.getName().endsWith(".png")) {
                    textureFile = new File(textureFile.getAbsolutePath() + ".png");
                }
                return textureFile;
            case ANIMATION:
                String base_Path_ = SVEngine.ANIMATIONS_PATH;  // Путь к папке storyverse
                File animationsFile = new File(base_Path_ + path);
                // Добавляем расширение .json если его нет
                if (!animationsFile.getName().endsWith(".json")) {
                    animationsFile = new File(animationsFile.getAbsolutePath() + ".json");
                }
                return animationsFile;
            default:
                return null;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity object) {
        try {
            String path = object.getTexturePath();
            File modelFile = getFile(path, AssetType.TEXTURE);
            return new ResourceLocation("file", modelFile.getAbsolutePath());
        } catch (Exception e) {
            return new ResourceLocation(SVEngine.MOD_ID, "textures/entity/npc/npc.png");
        }
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
    public ResourceLocation getAnimationFileLocation(NPCEntity animatable) {
        try {
            String path = animatable.getAnimationPath();
            File modelFile = getFile(path, AssetType.ANIMATION);
            return new ResourceLocation("file", modelFile.getAbsolutePath());
        } catch (Exception e) {
            return new ResourceLocation(SVEngine.MOD_ID, "animations/npc.animation.json");
        }
    }

    @Override
    public void setCustomAnimations(NPCEntity animatable, int instanceId, AnimationEvent animationEvent) {
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
