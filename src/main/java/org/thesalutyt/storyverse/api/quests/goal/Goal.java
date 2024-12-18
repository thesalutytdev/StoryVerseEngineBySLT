package org.thesalutyt.storyverse.api.quests.goal;

import org.thesalutyt.storyverse.api.quests.Quest;

import java.util.HashMap;

public abstract class Goal {
    public GoalType type;
    public String progress;
    public Quest quest;
    public int prog;
    public boolean completed;
    public static HashMap<String, Goal> goals = new HashMap<>();

    public Goal(GoalType type, String questId) {
        this.type = type;
        this.progress = "Start";
        this.completed = false;
        this.quest = Quest.quests.get(questId);

        goals.put(questId, this);
    }

    public Goal(GoalType type, String progress, String questId) {
        this.type = type;
        this.progress = progress;
        this.completed = false;
        this.quest = Quest.quests.get(questId);

        goals.put(questId, this);
    }

    public Goal(GoalType type, String progress, boolean completed, String questId) {
        this.type = type;
        this.progress = progress;
        this.completed = completed;
        this.quest = Quest.quests.get(questId);

        goals.put(questId, this);
    }

    protected Goal(GoalType type) {
        this.type = type;
        this.progress = "Start";
        this.completed = false;
    }

    public GoalType getType() {
        return type;
    }

    public String getProgress() {
        return progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setType(GoalType type) {
        this.type = type;
    }

    public void upgradeProgress(Integer state) {
        switch (state) {
            case 0:
                this.progress = "Start";
                break;
            case 1:
                this.progress = "In Progress";
                break;
            case 2:
                this.progress = "Completed";
                this.completed = true;
                break;
        }
    }
    public void upgradeProgress() {
        prog++;
        upgradeProgress(prog);
    }
    public void complete() {
        this.progress = "Completed";
        this.completed = true;
        prog = 2;
        this.quest.completedGoal(this);
        System.out.println("Completed goal: " + this.type);
        this.quest.goals.remove(this);
        if (this.quest.goals.isEmpty()) {
            this.quest.finished = true;
            Quest.finish(this.quest.id);
        }
    }

    public Goal getSelf() {
        return this;
    }
}
