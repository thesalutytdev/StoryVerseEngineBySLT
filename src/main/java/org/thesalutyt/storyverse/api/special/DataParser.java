package org.thesalutyt.storyverse.api.special;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class DataParser {
    public static HashMap<ResourceLocation, Boolean> animations = new HashMap<>();
    public static HashMap<ResourceLocation, Boolean> models = new HashMap<>();
    public static HashMap<ResourceLocation, Boolean> textures = new HashMap<>();
    public static ResourceLocation newResourceLocation(String path) {
        return new ResourceLocation(path);
    }
    public static ResourceLocation newAnimationPath(String path) {
        if (path.endsWith(".json")) {
            animations.put(newResourceLocation(path), true);
            return newResourceLocation(path);
        } else {
            return new ResourceLocation("That path don't have any animations file");
        }
    }
    public static ResourceLocation newModelPath(String path) {
        if (path.endsWith(".geo.json")) {
            models.put(newResourceLocation(path), true);
            return newResourceLocation(path);
        } else {
            return newResourceLocation("That path don't have any model files!");
        }
    }
    public static ResourceLocation newTexturePath(String path) {
        if (path.endsWith(".png")) {
            textures.put(newResourceLocation(path), true);
            return newResourceLocation(path);
        } else {
            return newResourceLocation("That path don't have any texture files!");
        }
    }
}
