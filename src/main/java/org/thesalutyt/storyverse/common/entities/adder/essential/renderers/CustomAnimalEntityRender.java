package org.thesalutyt.storyverse.common.entities.adder.essential.renderers;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomAnimalEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.models.CustomAnimalEntityModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CustomAnimalEntityRender extends GeoEntityRenderer<CustomAnimalEntity> {
    public CustomAnimalEntityRender(EntityRendererManager renderManager) {
        super(renderManager, new CustomAnimalEntityModel());
    }
}
