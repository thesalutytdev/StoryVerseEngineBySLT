package org.thesalutyt.storyverse.api.quests;

import net.minecraft.entity.LivingEntity;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.quests.goal.GoalType;
import org.thesalutyt.storyverse.api.quests.resource.IQuest;

import java.util.ArrayList;
import java.util.HashMap;

public class Quest implements IQuest, EnvResource, JSResource {
    public String name;
    public String id;
    public String description;
    public GoalType goal;
    public String questAdder;
    public LivingEntity entityAdder;
    public int progress;
    public boolean finished;
    public static HashMap<String, Quest> quests = new HashMap<>();
    public static ArrayList<String> questList = new ArrayList<>();

    public Quest(String id, String name, String description, GoalType goal, String questAdder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.goal = goal;
        if (MobJS.controllers.containsKey(questAdder)) {
            this.questAdder = questAdder;
            if (MobJS.controllers.get(questAdder).getEntity()
                    instanceof LivingEntity &&
                    MobJS.controllers.get(questAdder).isAlive()) {
                this.entityAdder = (LivingEntity) MobJS.controllers.get(questAdder).getEntity();
            }
        }
        this.questAdder = questAdder;
        this.progress = 0;
        this.finished = false;

        quests.put(id, this);
        questList.add(id);
    }


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
