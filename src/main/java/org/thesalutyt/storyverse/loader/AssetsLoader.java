package org.thesalutyt.storyverse.loader;

import org.apache.commons.io.FileUtils;
import org.thesalutyt.storyverse.SVEngine;

import java.io.File;
import java.io.IOException;

public class AssetsLoader implements ILoader {
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        System.out.println("SUU: " + destinationDirectory.getAbsolutePath());
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }
    @Override
    public void init() throws IOException {
        copyDirectory(SVEngine.ASSETS_DIR, "./assets/storyverse/");
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
