package org.thesalutyt.storyverse.common.entities.npc;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class NPCModel extends AnimatedTickingGeoModel<NPCEntity> {
    public ResourceLocation parsePath(String path) {
        String modId = "storyverse";
        String name = "animations/npc.animation.json";
        if (path.contains(":")) {
            modId = path.substring(0, path.indexOf(":"));
        }

        name = path.substring(path.indexOf(":") + 1);
        return new ResourceLocation(modId, name + (name.endsWith(".json") ? "" : ".json"));
    }
    @Override
    public ResourceLocation getModelLocation(NPCEntity object) {
        String path = object.getPersistentData().getString("modelPath");
        return this.parsePath(path == "" ? "storyverse:geo/steve.geo" : path);
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity object) {
        return new ResourceLocation("storyverse", "textures/entity/npc.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(NPCEntity animatable) {
        return null;
    }
}
