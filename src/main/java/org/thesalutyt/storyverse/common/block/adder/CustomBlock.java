package org.thesalutyt.storyverse.common.block.adder;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.common.block.ModBlocks;
import org.thesalutyt.storyverse.common.elements.ICustomElement;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CustomBlock extends ScriptableObject implements ICustomElement {
    public String name;
    public Material material;
    public int harvestLevel;
    public boolean requiresTool;
    public ToolType toolType;
    public float hardnessAndResistance;
    public int lightLevel;
    public Block block;
    public ItemGroup group = ModCreativeTabs.ENGINE_TAB;

    public CustomBlock() {}

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

    public CustomBlock(String name) {
        this(name, Material.STONE);
    }
    public static CustomBlock create(String name, String material, Integer harvestLevel, Boolean requiresTool,
                              String toolType, Double hardnessAndResistance, Integer lightLevel) {
        CustomBlock block__ = new CustomBlock(name, getMaterial(material),
                harvestLevel, requiresTool,
                toToolType(toolType), hardnessAndResistance.floatValue(),
                lightLevel);
        block__.register();
        return block__;

    }
    public static CustomBlock create(String name, String tab,
                                     String material, Integer harvestLevel, Boolean requiresTool,
                                     String toolType, Double hardnessAndResistance, Integer lightLevel) {
        CustomBlock block__ = new CustomBlock(name, getMaterial(material),
                harvestLevel, requiresTool,
                toToolType(toolType), hardnessAndResistance.floatValue(),
                lightLevel);
        block__.register();
        block__.group = CustomItem.getGroup(tab);
        return block__;

    }

    public static Material getMaterial(String material) {
        return Material.STONE;
    }

    public static ToolType toToolType(String toolType) {
        return ToolType.get(toolType.toLowerCase());
    }

    public void register() {
        ModBlocks.addBlock(this);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        CustomBlock cs = new CustomBlock();
        cs.setParentScope(scope);

        try {
            Method create = CustomBlock.class.getMethod("create",
                    String.class, String.class,
                    Integer.class, Boolean.class,
                    String.class, Double.class, Integer.class);
            methodsToAdd.add(create);
            Method cWP = CustomBlock.class.getMethod("create",
                    String.class, String.class,
                    String.class, Integer.class,
                    Boolean.class, String.class,
                    Double.class, Integer.class);
            methodsToAdd.add(cWP);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, cs);
            cs.put(m.getName(), cs, methodInstance);
        }
        scope.put("block", scope, cs);
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
