package org.thesalutyt.storyverse.api.quests.goal;

import net.minecraft.entity.LivingEntity;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.quests.Quest;

import java.util.ArrayList;
import java.util.HashMap;

public class InteractGoal extends Goal {
    public static HashMap<String, InteractGoal> goals = new HashMap<>();
    public static ArrayList<LivingEntity> interactNeed = new ArrayList<>();
    public LivingEntity entity;
    public InteractGoal(String questId) {
        super(GoalType.INTERACT, questId);
        this.entity = (LivingEntity)
                MobJS.controllers.get(Quest.quests.get(questId).questAdder).getEntity();

        goals.put(questId, this);
        interactNeed.add(entity);
    }
}
