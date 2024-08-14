package org.thesalutyt.storyverse.api.quests.goal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class ItemGoal extends Goal {
    public ItemStack item;
    public Goal self;
    public static HashMap<ItemStack, ItemGoal> goals = new HashMap<>();

    public ItemGoal() {
        super(GoalType.ITEM);

    }

    public ItemGoal(String item, Integer amount, String quest) {
        super(GoalType.ITEM, quest);
        this.self = getSelf();
        this.type = GoalType.ITEM;
        this.upgradeProgress(0);
        this.item = new ItemStack(JSItem.getItem(item), amount);
        this.completed = false;
        goals.put(new ItemStack(JSItem.getItem(item), amount), this);
    }

    public ItemGoal(ItemStack item, String quest) {
        super(GoalType.ITEM, quest);
        this.self = getSelf();
        this.type = GoalType.ITEM;
        this.upgradeProgress(0);
        this.item = item;
        this.completed = false;
        goals.put(item, this);
        System.out.println("ItemGoal added: " + item);
    }

    public ItemGoal itemGoal(String item, Integer amount) {
        this.type = GoalType.ITEM;
        this.upgradeProgress(0);
        this.item = new ItemStack(JSItem.getItem(item), amount);
        this.completed = false;
        goals.put(new ItemStack(JSItem.getItem(item), amount), this);

        return this;
    }

    public ItemGoal itemGoal(ItemStack item, String quest) {
        System.out.println("ItemGoal: " + item);
        return new ItemGoal(item, quest);
    }

    @SubscribeEvent
    public static void onItem(PlayerEvent.ItemPickupEvent event) {
        System.out.println("Item picked up: " + event.getStack().toString());
        for (ItemGoal ig : goals.values()) {
            System.out.println("Checking item goal: " + ig.item.toString());
            if (ig.item.equals(event.getStack())) {
                ig.complete();
                System.out.println("Item goal completed");
                break;
            }
        }
        if (goals.containsKey(event.getStack())) {
            System.out.println("Found item in goals");
            goals.get(event.getStack()).complete();
            goals.remove(event.getStack());
        }
    }
}
