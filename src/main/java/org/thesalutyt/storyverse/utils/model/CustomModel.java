package org.thesalutyt.storyverse.utils.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class CustomModel extends Model {
    public static Minecraft mc = Minecraft.getInstance();

    public CustomModel(Function<ResourceLocation, RenderType> p_i225947_1_) {
        super(p_i225947_1_);
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_,
                               IVertexBuilder p_225598_2_,
                               int p_225598_3_, int p_225598_4_,
                               float p_225598_5_, float p_225598_6_,
                               float p_225598_7_, float p_225598_8_) {

    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {


    }
}