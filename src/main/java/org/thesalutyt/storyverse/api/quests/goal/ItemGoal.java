package org.thesalutyt.storyverse.api.quests.goal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;

public class ItemGoal extends Goal {
    public ItemStack item;
    public ItemGoal() {
        super(GoalType.ITEM);
    }
    public ItemGoal itemGoal(String item, Integer amount) {
        this.type = GoalType.ITEM;
        this.upgradeProgress(0);
        this.item = new ItemStack(JSItem.getItem(item), amount);
        this.completed = false;

        return this;
    }

}
