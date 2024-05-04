package org.thesalutyt.storyverse.api.special.character;

import org.thesalutyt.storyverse.api.features.MobController;

import java.util.HashMap;

public class Reputation {
    public HashMap<MobController, Double> reputation = new HashMap<>();

    public Reputation(MobController entity, Double reputation) {
        this.reputation.put(entity, reputation);
    }

    public Reputation setReputation(MobController entity, Double reputation) {
        this.reputation.put(entity, reputation);

        return this;
    }
    public Double getReputation(MobController entity) {
        return this.reputation.get(entity);
    }
}
