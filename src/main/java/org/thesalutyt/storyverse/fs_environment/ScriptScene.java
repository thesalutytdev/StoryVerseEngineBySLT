package org.thesalutyt.storyverse.fs_environment;

import org.thesalutyt.storyverse.fs_environment.action.Action;

import java.util.ArrayList;

public class ScriptScene {
    public String id = "";
    public String scriptId = "";
    public ArrayList<Action> actions = new ArrayList();
//    public final Story story;
//    public final SceneJSON json;
//
//    public ScriptScene(Story story, SceneJSON json) {
//        this.id = json.id;
//        this.story = story;
//        if (json.lang.equals("js")) {
//            this.scriptId = json.script;
//        } else if (json.lang.equals("json")) {
//            this.loadActionsFromJSON(json);
//        }
//
//        this.json = json;
//    }
}
