package org.thesalutyt.storyverse.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.block.adder.CustomBlock;
import org.thesalutyt.storyverse.common.items.ModItems;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, StoryVerse.MOD_ID);
    public static void addBlock(CustomBlock block) {
        if (block.requiresTool) {
            BLOCKS.register(block.name, () -> new Block(AbstractBlock.Properties.of(block.material)
                    .harvestLevel(block.harvestLevel).harvestTool(block.toolType).strength(block.hardnessAndResistance)));
            registerBlockItem(block.name, block);
        } else {
            BLOCKS.register(block.name, () -> new Block(AbstractBlock.Properties.of(block.material)
                    .harvestLevel(block.harvestLevel).strength(block.hardnessAndResistance)));
            registerBlockItem(block.name, block);
        }
    }
    public static void registerBlockItem(String name, CustomBlock block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.block, new Item.Properties().tab(block.group)));
    }
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
