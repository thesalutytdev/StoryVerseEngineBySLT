package org.thesalutyt.storyverse.api.quests;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.api.quests.goal.GoalType;
import org.thesalutyt.storyverse.api.quests.goal.NPCItem;
import org.thesalutyt.storyverse.api.quests.item.ItemQuest;
import org.thesalutyt.storyverse.utils.ItemUtils;

import java.util.HashMap;

public class QuestManager {
    public static HashMap<PlayerEntity, Quest> quests = new HashMap<>();
    public static HashMap<LivingEntity, ItemQuest> itemQuests = new HashMap<>();

    public QuestManager() {
        Quest.questList.forEach(quest -> quests.put(Player.getPlayer(), Quest.quests.get(quest)));
    }

    public static void onMobInteract(PlayerInteractEvent.EntityInteract event) {
        System.out.println("Quest found");
        Quest q;
        for (String id : Quest.quests.keySet()) {
            if (Quest.quests.get(id).goal == GoalType.NPC_ITEM && Quest.quests.get(id).entityAdder == event.getTarget()) {
                q = Quest.quests.get(id);
                break;
            }
        }

        MobController mob = MobJS.getMob(event.getTarget().getUUID());
        PlayerEntity player = event.getPlayer();
        System.out.println("Checking NPC item");
        if (ItemUtils.hasItem(player, NPCItem.items.get(mob).stack)) {
            System.out.println("Has stack");
            finishNPCItem(NPCItem.items.get(mob), player);
        }
    }

    public static void finishNPCItem(NPCItem goal, PlayerEntity player) {
        ItemUtils.takeStack(player, goal.stack);
        System.out.println("Took stack");
        goal.complete();
    }
}
