package org.thesalutyt.storyverse.common.entities.adder.essential.renderers;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomMobEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.models.CustomMobEntityModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CustomMobEntityRender extends GeoEntityRenderer<CustomMobEntity> {
    public CustomMobEntityRender(EntityRendererManager renderManager) {
        super(renderManager, new CustomMobEntityModel());
    }
}
