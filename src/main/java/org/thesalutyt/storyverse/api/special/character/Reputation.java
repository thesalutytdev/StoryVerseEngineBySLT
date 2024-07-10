package org.thesalutyt.storyverse.api.special.character;

import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.MobController;

import java.util.ArrayList;
import java.util.HashMap;

public class Reputation extends ScriptableObject implements EnvResource, JSResource {
    public static HashMap<MobController, Reputation> repMap = new HashMap<>();
    public MobController character;
    public String mobId;
    public Integer reputation = 0;
    public static ArrayList<MobController> characters = new ArrayList<>();

    public Reputation(String id) {
        MobController mob = MobJS.getMob(id);
        character = mob;
        repMap.put(mob, this);
        characters.add(mob);
        mobId = id;
    }

    public Reputation link(String id) {
        MobController mob = MobJS.getMob(id);
        character = mob;
        repMap.put(mob, this);
        characters.add(mob);
        return this;
    }

    public Integer getReputation(String id) {
        MobController mob = MobJS.getMob(id);
        if (repMap.containsKey(mob)) {
            return repMap.get(mob).reputation;
        }
        return 0;
    }

    @Override
    public String getClassName() {
        return "Reputation";
    }

    @Override
    public String getResourceId() {
        return "Reputation";
    }
}
