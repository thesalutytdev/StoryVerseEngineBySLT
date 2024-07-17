package org.thesalutyt.storyverse.api.quests;

import net.minecraft.entity.player.PlayerEntity;
import org.thesalutyt.storyverse.api.features.Player;

import java.util.HashMap;

public class QuestManager {
    public static HashMap<PlayerEntity, Quest> quests = new HashMap<>();

    public QuestManager() {
        Quest.questList.forEach(quest -> quests.put(Player.getPlayer(), Quest.quests.get(quest)));
    }
}
