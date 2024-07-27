package org.thesalutyt.storyverse.api.camera.cutscene.nonTicking;

import org.thesalutyt.storyverse.api.camera.cutscene.instance.CutsceneInstance;

import java.util.HashMap;

public class Manager {
    public static HashMap<String, CutsceneInstance> cutscenes = new HashMap<>();
    public static HashMap<String, Cutscene> default_cutscenes = new HashMap<>();
    public static HashMap<String, Moving> movingInstances = new HashMap<>();
}
