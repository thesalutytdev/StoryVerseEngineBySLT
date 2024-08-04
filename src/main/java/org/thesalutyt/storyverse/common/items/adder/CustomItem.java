package org.thesalutyt.storyverse.common.items.adder;

import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.common.elements.ICustomElement;
import org.thesalutyt.storyverse.common.items.ModItems;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;
import org.thesalutyt.storyverse.common.tabs.adder.CustomTab;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class CustomItem extends ScriptableObject implements ICustomElement {
    public String name;
    public int maxStackSize;
    public ItemGroup tab;
    public Runnable onUse;
    public Item item;
    public static HashMap<String, CustomItem> items = new HashMap<>();

    public CustomItem(String name, int maxStackSize, ItemGroup tab) {
        this.name = name;
        this.maxStackSize = maxStackSize;
        this.tab = tab;
        this.item = new Item(new Item.Properties().tab(tab).stacksTo(maxStackSize));
        items.put(name, this);
    }

    public CustomItem(String name, int maxStackSize) {
        this(name, maxStackSize, null);
    }

    public CustomItem(String name) {
        this(name, 64);
    }
    public CustomItem(String name, Runnable onUse) {
        this(name, 64, null);
        this.onUse = onUse;
    }

    public CustomItem(String name, Runnable onUse, ItemGroup tab) {
        this(name, 64, tab);
        this.onUse = onUse;
    }

    public CustomItem(String name, Runnable onUse, int maxStackSize) {
        this(name, maxStackSize, null);
        this.onUse = onUse;
    }

    public CustomItem(String name, Runnable onUse, int maxStackSize, ItemGroup tab) {
        this(name, maxStackSize, tab);
        this.onUse = onUse;
    }
    public CustomItem() {

    }

    public void register() {
        ModItems.addItem(this);
    }
    public void register(String id) {
        ModItems.addItem(items.get(id));
    }

    public CustomItem create(String name, Integer maxStackSize, String group) {
        ItemGroup tab = getGroup(group);
        return new CustomItem(name, maxStackSize, tab);
    }
    public CustomItem foodItem(String name, Integer maxStackSize, String group, Boolean isFast, Integer nutrition,
                               Boolean alwaysEat, Boolean meat, Double saturationModifier) {
        ItemGroup tab = getGroup(group);
        CustomItem cs = new CustomItem(name, maxStackSize, tab);
        if (isFast) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).fast().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        } if (alwaysEat) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).alwaysEat().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        } if (isFast && alwaysEat) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).fast().alwaysEat().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        } if (meat) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).meat().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        } if (isFast && meat) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).fast().meat().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        } if (alwaysEat && meat) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).alwaysEat().meat().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        } if (isFast && alwaysEat && meat) {
            cs.item = new Item(new Item.Properties().tab(tab)
                    .food(new Food.Builder().nutrition(nutrition).fast().alwaysEat().meat().saturationMod(saturationModifier.floatValue()).build())
                    .stacksTo(maxStackSize));
        }
        return cs;
    }

    public void usableItem(String name, Integer maxStackSize, Boolean isFireResistant, String rarity, String group,
                                 BaseFunction onUse) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            ArrayList<BaseFunction> functions = new ArrayList<>();
            functions.add(onUse);
            ItemGroup tab = getGroup(group);
            assert tab != null;
            UsableItem item;
            if (isFireResistant) {
                item = new UsableItem(name, new Item.Properties().tab(tab)
                        .rarity(getRarity(rarity)).stacksTo(maxStackSize).fireResistant(), functions);
            } else {
                item = new UsableItem(name, new Item.Properties().tab(tab).
                        rarity(getRarity(rarity)).stacksTo(maxStackSize), functions);
            }
            ModItems.addItem(item);
        });
    }

    public static ItemGroup getGroup(String name) {
        switch (name) {
            case "engine": {
                return ModCreativeTabs.ENGINE_TAB;
            } case "null": {
                return null;
            } default: {
                return CustomTab.tabs.get(name).tab;
            }
        }
     }

    public static Rarity getRarity(String name) {
        switch (name.toLowerCase()) {
            case "uncommon": {
                return Rarity.UNCOMMON;
            } case "rare": {
                return Rarity.RARE;
            } case "epic": {
                return Rarity.EPIC;
            } default: {
                return Rarity.COMMON;
            }
        }
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        CustomItem cs = new CustomItem();
        cs.setParentScope(scope);
        try {
            Method create = CustomItem.class.getMethod("create", String.class, Integer.class, String.class);
            methodsToAdd.add(create);
            Method register = CustomItem.class.getMethod("register", String.class);
            methodsToAdd.add(register);
            Method food = CustomItem.class.getMethod("foodItem",
                    String.class, Integer.class,
                    String.class, Boolean.class, Integer.class,
                    Boolean.class, Boolean.class,
                    Double.class);
            methodsToAdd.add(food);
            Method usableItem = CustomItem.class.getMethod("usableItem",
                    String.class, Integer.class,
                    Boolean.class, String.class, String.class,
                    BaseFunction.class);
            methodsToAdd.add(usableItem);
            Method getGroup = CustomItem.class.getMethod("getGroup", String.class);
            methodsToAdd.add(getGroup);
            Method getRarity = CustomItem.class.getMethod("getRarity", String.class);
            methodsToAdd.add(getRarity);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, cs);
            cs.put(m.getName(), cs, methodInstance);
        }
        scope.put("item", scope, cs);
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (items.get(event.getUseItem().name()) != null) {
            items.get(event.getUseItem().name()).onUse.run();
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.SUCCESS);
        }
    }

    @Override
    public Object getDefaultElement() {
        return Item.class;
    }

    @Override
    public String getClassName() {
        return "CustomItem";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "CustomItem";
    }
}
