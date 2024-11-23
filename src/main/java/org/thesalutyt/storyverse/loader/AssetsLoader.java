package org.thesalutyt.storyverse.loader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.FileUtils;
import org.thesalutyt.storyverse.SVEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class AssetsLoader implements ILoader {
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        System.out.println("SUU: " + destinationDirectory.getAbsolutePath());
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }

    public static ResourceLocation loadEntityTexture(String texture) throws Exception {
        File textureImage = new File(SVEngine.TEXTURES_PATH + texture);
        if (!textureImage.exists()) {
            throw new Exception("Texture file not found: " + textureImage.getAbsolutePath());
        }
        NativeImage image = NativeImage.read(new FileInputStream(textureImage));
        DynamicTexture dynamicTexture = new DynamicTexture(image);
        dynamicTexture.upload();
        Minecraft.getInstance().getTextureManager().register(new ResourceLocation("storyverse:textures/entity/" + texture), dynamicTexture);
        return new ResourceLocation("storyverse:textures/entity/" + texture);
    }


    public void loadSkins() {
        File[] files = new File(SVEngine.TEXTURES_PATH).listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) continue;
            try {
                loadEntityTexture(file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void init() throws IOException {
        loadSkins();
    }

    @Override
    public LoaderType getType() {
        return LoaderType.ASSETS;
    }

    @Override
    public void load() throws IOException {
        init();
    }
}
