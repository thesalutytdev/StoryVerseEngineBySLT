package org.thesalutyt.storyverse.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;

public class EntityDeleter extends Item {
    public EntityDeleter() {
        super(new Properties().stacksTo(1).tab(ModCreativeTabs.ENGINE_TAB).addToolType(ToolType.AXE, 4)
                .fireResistant()
                .setNoRepair()
                .rarity(Rarity.EPIC));
    }
}
