package org.thesalutyt.storyverse.api.camera.cutscene;

import org.thesalutyt.storyverse.api.camera.cutscene.instance.CutsceneInstance;

import java.util.HashMap;

public class CutsceneManager {
    public static HashMap<String, Cutscene> cutscenes = new HashMap<>();
    public static HashMap<String, CutsceneInstance> instances = new HashMap<>();
    public static HashMap<String, Moving> movingInstances = new HashMap<>();

    public CutsceneManager(Cutscene cutscene) {
        cutscenes.put(cutscene.player.getName().getContents(), cutscene);
    }

    public CutsceneManager(Moving moving) {
        movingInstances.put(moving.player.getName().getContents(), moving);
    }
}
