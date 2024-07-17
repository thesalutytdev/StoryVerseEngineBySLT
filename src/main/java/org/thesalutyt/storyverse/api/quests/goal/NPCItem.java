package org.thesalutyt.storyverse.api.quests.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.quests.Quest;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import java.util.HashMap;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class NPCItem extends Goal {
    public Quest quest;
    public LivingEntity adder;
    public ItemStack stack;
    public NPCEntity npc;
    public static HashMap<LivingEntity, NPCItem> items = new HashMap<>();

    public NPCItem(ItemStack item, String questId) {
        super(GoalType.NPC_ITEM, questId);
        System.out.println("NPCItem: " + item);
        this.quest = Quest.quests.get(questId);
        System.out.println("Quest: " + questId);
        this.stack = item;
        System.out.println("Stack: " + stack);
        this.adder = quest.entityAdder;
        System.out.println("Adder: " + adder);
        if (adder instanceof NPCEntity) {
            this.npc = (NPCEntity) adder;
        }
        items.put(adder, this);
    }

}
