package org.thesalutyt.storyverse.api.quests;

import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.quests.goal.GoalType;
import org.thesalutyt.storyverse.api.quests.resource.IQuest;

public class Quest implements IQuest, EnvResource, JSResource {
    public String name;
    public String description;
    public GoalType goal;


    @Override
    public String getResourceId() {
        return "Quest";
    }

    @Override
    public String getClassName() {
        return "Quest";
    }

    @Override
    public Quest getSelf() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
