package org.thesalutyt.storyverse.fs_environment.story;

import java.util.ArrayList;

public class PackedData {
    public static class PackedStoryData {
        public String id = "";
        public String compiler = "plotter";
        public String version = "3";
        public ArrayList<SceneJSON> scenes = new ArrayList();
        public ArrayList<PackedScriptData> scripts = new ArrayList();
        public ArrayList<PackedLibData> libs = new ArrayList();

        public PackedStoryData() {
        }
    }
    public class PackedScriptData {
        public String id = "";
        public String content = "";

        public PackedScriptData() {
        }
    }
    public class PackedLibData {
        public String id = "";
        public String content = "";

        public PackedLibData() {
        }
    }
}
