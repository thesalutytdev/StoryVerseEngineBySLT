package org.thesalutyt.storyverse.common.entities.adder.essential.renderers;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomFlyingEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.models.CustomFlyingEntityModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CustomFlyingEntityRender extends GeoEntityRenderer<CustomFlyingEntity> {
    public CustomFlyingEntityRender(EntityRendererManager renderManager) {
        super(renderManager, new CustomFlyingEntityModel());
    }
}
