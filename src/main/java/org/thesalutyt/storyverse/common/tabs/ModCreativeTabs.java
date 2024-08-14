package org.thesalutyt.storyverse.common.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.thesalutyt.storyverse.common.items.ModItems;

public class ModCreativeTabs {
    public static final ItemGroup ENGINE_TAB = new ItemGroup("storyverse") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.NPC_SPAWNER.get());
        }
    };
}
