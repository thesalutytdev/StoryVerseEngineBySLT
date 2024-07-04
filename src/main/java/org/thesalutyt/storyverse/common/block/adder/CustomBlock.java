package org.thesalutyt.storyverse.common.block.adder;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.thesalutyt.storyverse.common.block.ModBlocks;
import org.thesalutyt.storyverse.common.elements.ICustomElement;

public class CustomBlock implements ICustomElement {

    public String name;
    public Material material;
    public int harvestLevel;
    public boolean requiresTool;
    public ToolType toolType;
    public float hardnessAndResistance;
    public int lightLevel;
    public Block block;

    public CustomBlock(String name, Material material, int harvestLevel, boolean requiresTool, ToolType toolType, float hardnessAndResistance, int lightLevel) {
        this.name = name;
        this.material = material;
        this.harvestLevel = harvestLevel;
        this.requiresTool = requiresTool;
        this.toolType = toolType;
        this.hardnessAndResistance = hardnessAndResistance;
        this.lightLevel = lightLevel;
        this.block = new Block(
                AbstractBlock.Properties.of(material)
                        .harvestLevel(harvestLevel)
                        .harvestTool(toolType)
                        .strength(hardnessAndResistance));
    }

    public CustomBlock(String name, Material material, int harvestLevel, boolean requiresTool, ToolType toolType, float hardnessAndResistance) {
        this(name, material, harvestLevel, requiresTool, toolType, hardnessAndResistance, 0);
    }

    public CustomBlock(String name, Material material, int harvestLevel, boolean requiresTool, ToolType toolType) {
        this(name, material, harvestLevel, requiresTool, toolType, 5.0f);
    }

    public CustomBlock(String name, Material material, int harvestLevel, boolean requiresTool) {
        this(name, material, harvestLevel, requiresTool, ToolType.PICKAXE);
    }

    public CustomBlock(String name, Material material, int harvestLevel) {
        this(name, material, harvestLevel, false);
    }

    public CustomBlock(String name, Material material) {
        this(name, material, 0, false);
    }

    public void register() {
        ModBlocks.addBlock(this);
    }
    @Override
    public Object getDefaultElement() {
        return Block.class;
    }

    @Override
    public String getClassName() {
        return "CustomBlock";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "CustomBlock";
    }
}
