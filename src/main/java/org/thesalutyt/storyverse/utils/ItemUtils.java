package org.thesalutyt.storyverse.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ItemUtils extends ScriptableObject implements EnvResource, JSResource {
    public static boolean matches(ItemStack stack1, ItemStack stack2) {
        return ItemStack.matches(stack1, stack2) && ItemStack.tagMatches(stack1, stack2);
    }

    public static void takeStack(PlayerEntity player, ItemStack itemStack) {
        takeStack(player, itemStack, itemStack.getCount());
    }
    public static void takeStack(String playerName, String itemStack) {
        takeStack(Server.getPlayerByName(playerName), JSItem.getStack(itemStack));
    }

    public static void matches(String stack1, String stack2) {
        matches(JSItem.getStack(stack1), JSItem.getStack(stack2));
    }

    public static void takeStack(PlayerEntity player, ItemStack itemStack, int neededCount) {
        if (itemStack.isEmpty()) return;

        for (ItemStack stack : player.inventory.items) {
            if (matches(itemStack, stack)) {
                int count = stack.getCount();
                stack.shrink(neededCount);
                neededCount -= count;
            }

            if (neededCount <= 0) {
                break;
            }
        }
    }

    public static void giveStack(PlayerEntity player, ItemStack itemstack) {
        if (itemstack.isEmpty()) return;

        ItemEntity itemEntity = player.drop(itemstack, true, true);
        if (itemEntity != null) {
            itemEntity.setNoPickUpDelay();
            itemEntity.setOwner(player.getUUID());
        }
    }

    public static void giveStack(String playerName, String itemStack) {
        giveStack(Server.getPlayerByName(playerName), JSItem.getStack(itemStack));
    }

    public static boolean hasAmount(PlayerEntity sender, ItemStack itemStackIn) {
        if (itemStackIn.isEmpty()) return true;

        int neededCount = itemStackIn.getCount();
        int currentCount = 0;
        for(ItemStack itemStack : sender.inventory.items) {
            if (matches(itemStackIn, itemStack))
                currentCount += itemStack.getCount();

            if (currentCount >= neededCount) return true;
        }

        return false;
    }

    public static boolean hasItem(PlayerEntity sender, ItemStack itemStackIn) {
        if (itemStackIn.isEmpty()) return true;

        for(ItemStack itemStack : sender.inventory.items) {
            if (matches(itemStackIn, itemStack))
                return true;
        }

        return false;
    }

    public static boolean hasItem(String playerName, String itemStack) {
        return hasItem(Server.getPlayerByName(playerName), JSItem.getStack(itemStack));
    }

    public static int getAmount(PlayerEntity sender, ItemStack itemStackIn) {
        if (itemStackIn.isEmpty()) return 0;

        int currentCount = 0;
        for(ItemStack itemStack : sender.inventory.items) {
            if (matches(itemStackIn, itemStack))
                currentCount += itemStack.getCount();
        }

        return currentCount;
    }

    public static ItemStack stackFromString(String s) {
        ItemArgument itemArgument = new ItemArgument();
        try {
            ItemInput itemInput = itemArgument.parse(new StringReader(s));
            return itemInput.createItemStack(1, false);
        } catch (CommandSyntaxException e) {
            return ItemStack.EMPTY;
        }
    }

    public static String stackToString(ItemStack itemStack) {
        return itemStack == null || itemStack.isEmpty() ? "minecraft:empty" :
                itemStack.getItem().getRegistryName()
                        +(itemStack.hasTag() ? itemStack.getTag().toString() : "");
    }

    public static void showTitle(String playerName, String title) {
        StoryUtils.showTitle(Server.getPlayerByName(playerName), new StringTextComponent(title));
    }

    public static BlockPos pos(Double x, Double y, Double z) {
        return new BlockPos(x, y, z);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        ItemUtils iu = new ItemUtils();
        iu.setParentScope(scope);

        try {
            Method hasItem = ItemUtils.class.getMethod("hasItem", String.class, String.class);
            methodsToAdd.add(hasItem);
            Method giveStack = ItemUtils.class.getMethod("giveStack", String.class, String.class);
            methodsToAdd.add(giveStack);
            Method showTitle = ItemUtils.class.getMethod("showTitle", String.class, String.class);
            methodsToAdd.add(showTitle);
            Method pos = ItemUtils.class.getMethod("pos", Double.class, Double.class, Double.class);
            methodsToAdd.add(pos);
            Method takeStack = ItemUtils.class.getMethod("takeStack", String.class, String.class);
            methodsToAdd.add(takeStack);
            Method matches = ItemUtils.class.getMethod("matches", String.class, String.class);
            methodsToAdd.add(matches);
        } catch (Exception e) {
            new ErrorPrinter(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, iu);
            iu.put(m.getName(), iu, methodInstance);
        }

        scope.put("utils", scope, iu);
    }

    @Override
    public String getClassName() {
        return "ItemUtils";
    }

    @Override
    public String getResourceId() {
        return "ItemUtils";
    }
}