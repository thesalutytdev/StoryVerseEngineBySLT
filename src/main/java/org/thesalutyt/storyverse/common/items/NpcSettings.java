package org.thesalutyt.storyverse.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;

public class NpcSettings extends Item {
    public NpcSettings() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                .tab(ModCreativeTabs.ENGINE_TAB).fireResistant().setNoRepair());
    }
}
