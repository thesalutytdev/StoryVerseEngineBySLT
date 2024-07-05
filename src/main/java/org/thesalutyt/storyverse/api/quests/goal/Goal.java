package org.thesalutyt.storyverse.api.quests.goal;

public class Goal {
    public GoalType type;
    public String progress;
    public boolean completed;

    public Goal(GoalType type) {
        this.type = type;
        this.progress = "Start";
        this.completed = false;
    }

    public Goal(GoalType type, String progress) {
        this.type = type;
        this.progress = progress;
        this.completed = false;
    }

    public Goal(GoalType type, String progress, boolean completed) {
        this.type = type;
        this.progress = progress;
        this.completed = completed;
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
}
