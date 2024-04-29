package org.thesalutyt.storyverse.fs_environment.compiler;

import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.fs_environment.file.FileManager;
import org.thesalutyt.storyverse.fs_environment.story.PackedData;

import java.io.File;

public class Root {
    public static ActResult build() {
        File scriptFolder = SVEngine.SCRIPTS_PATH_FILE;
        File buildFolder = new File(scriptFolder, "builds");
        buildFolder.mkdir();
        PackedData.PackedStoryData data = new PackedData.PackedStoryData();
        data.id = "test";
        FileManager.javaToJson(new File(buildFolder, "test" + ".sv"), data);

        return ActResult.SUCCESS;
    }

}
