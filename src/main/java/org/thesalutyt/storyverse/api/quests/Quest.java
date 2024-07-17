package org.thesalutyt.storyverse.api.quests;

import net.minecraft.entity.LivingEntity;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.quests.goal.Goal;
import org.thesalutyt.storyverse.api.quests.goal.GoalType;
import org.thesalutyt.storyverse.api.quests.resource.IQuest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Quest extends ScriptableObject implements IQuest, EnvResource, JSResource {
    public String name;
    public String id;
    public String description;
    public GoalType goal;
    public String questAdder;
    public LivingEntity entityAdder;
    public int progress;
    public boolean finished;
    public ArrayList<BaseFunction> onFinish = new ArrayList<>();
    public ArrayList<Goal> goals = new ArrayList<>();
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

    public Quest(String id, String name, String description, Goal goal, String questAdder) {
        new QuestManager();
        this.id = id;
        this.name = name;
        this.description = description;
        this.goal = goal.getType();
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
        this.goals.add(goal);
        quests.put(id, this);
        questList.add(id);
    }

    public Quest(String id, String name, String description, String questAdder) {
        new QuestManager();
        this.id = id;
        this.name = name;
        this.description = description;
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

    public Quest() {}

    public Quest newQuest(String id, String name, String description, String questAdder, String playerName) {
        System.out.println("New quest: " + id + " " + name + " " + description +
                " " + questAdder + " Adder Mob: " + MobJS.getMob(questAdder));
        Quest quest = new Quest(id, name, description, questAdder);
        QuestManager.quests.put(Server.getPlayerByName(playerName), quest);
        return quest;
    }

    public Quest addGoal(String goal) {
        if (Goal.goals.containsKey(goal)) {
            this.goals.add(Goal.goals.get(goal));
        }
        return this;
    }

    public static Quest onFinish(String questId, BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            if (quests.containsKey(questId)) {
                quests.get(questId).onFinish.add(function);
            }
        });

        return quests.get(questId);
    }

    public static Quest finish(String questId) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            if (quests.containsKey(questId)) {
                quests.get(questId).finished = true;
                for (BaseFunction f : quests.get(questId).onFinish) {
                    f.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(),
                            new Object[]{});
                }
            }
        });

        return quests.get(questId);
    }

    public void completedGoal(Goal goal) {
        if (!goals.contains(goal)) {
            return;
        }
        goals.remove(goal);
        System.out.println("Removed goal: " + goal.toString());
        if (goals.isEmpty()) {
            finish(id);
        }
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        Quest q = new Quest();
        q.setParentScope(scope);
        try {
            Method newQuest = Quest.class.getMethod("newQuest", String.class,
                    String.class, String.class, String.class, String.class);
            methodsToAdd.add(newQuest);
            Method onFinish = Quest.class.getMethod("onFinish", String.class, BaseFunction.class);
            methodsToAdd.add(onFinish);
            Method finish = Quest.class.getMethod("finish", String.class);
            methodsToAdd.add(finish);
            Method addGoal = Quest.class.getMethod("addGoal", String.class);
            methodsToAdd.add(addGoal);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, q);
            q.put(m.getName(), q, methodInstance);
        }

        scope.put("quest", scope, q);
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
