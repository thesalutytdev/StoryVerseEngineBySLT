package org.thesalutyt.storyverse.api.quests.resource;

import org.thesalutyt.storyverse.api.quests.Quest;

public interface IQuest {
    public Quest getSelf();
    public String getName();
    public String getDescription();
    public void setName(String name);
    public void setDescription(String description);
}
