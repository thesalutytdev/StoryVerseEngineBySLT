package org.thesalutyt.storyverse.api.quests.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.quests.QuestManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ItemQuest extends ScriptableObject implements EnvResource, JSResource {
    protected boolean completed = false;
    protected LivingEntity adder;
    protected UUID adderId;
    protected String bringMessage = new TranslationTextComponent("text.storyverse.quests.item.bring")
            .getContents();
    protected ServerPlayerEntity playerCompleted;
    protected ArrayList<ItemStack> stacksNeeded = new ArrayList<>();
    protected ArrayList<ItemStack> stacksBrought = new ArrayList<>();
    protected ArrayList<BaseFunction> onComplete = new ArrayList<>();

    public ItemQuest(LivingEntity adder, ArrayList<ItemStack> stacksNeeded) {
        this.adder = adder;
        this.adderId = adder.getUUID();

        this.stacksNeeded.addAll(stacksNeeded);
        QuestManager.itemQuests.put(adder, this);
    }

    private ItemQuest() {}

    public ItemQuest setBringMessage(String message) {
        bringMessage = message;

        return this;
    }

    public ItemQuest addOnComplete(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onComplete.add(function);
        });

        return this;
    }

    public ItemQuest addStack(ItemStack stack) {
        stacksNeeded.add(stack);
        return this;
    }

    public ItemQuest addStacks(ArrayList<ItemStack> stacks) {
        stacksNeeded.addAll(stacks);
        return this;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean tryComplete(ServerPlayerEntity player) {
        for (ItemStack i : stacksNeeded) {
            for (ItemStack j : player.inventory.items) {
                if (i.getItem() == j.getItem() && !(i.getCount() > j.getCount())) {
                    stacksBrought.add(j);
                    stacksNeeded.remove(j);
                    j.shrink(i.getCount());
                    if (broughtStacks()) {
                        playerCompleted = player;
                        completed = true;
                        runOnComplete();
                        return true;
                    }
                }
                if (broughtStacks()) {
                    playerCompleted = player;
                    completed = true;
                    runOnComplete();
                    return true;
                }
            }
        }
        stacksNeeded.forEach(i -> {
            if (!i.isEmpty()) {
                Chat.sendNamed(adder.getName().getContents(), messageReplacer(i));
            }
        });
        return false;
    }

    protected boolean broughtStacks() {
        return stacksBrought.size() == stacksNeeded.size();
    }

    protected String messageReplacer(ItemStack stack) {
        return bringMessage
                .replace("%COUNT%", String.valueOf(stack.getCount()))
                .replace("%ITEM%",
                        Objects.requireNonNull(stack.getItem().getRegistryName()).toString());
    }

    public ItemQuest create(String mob, NativeArray items) {
        for (int i=0; i<items.getLength(); i++) {
            stacksNeeded.add(JSItem.getStack((String) items.get(i)));
        }
        return new ItemQuest(MobJS.getMob(mob).getMobEntity(), stacksNeeded);
    }

    public ItemQuest create(String mob, String item) {
        stacksNeeded.add(JSItem.getStack(item));
        return new ItemQuest(MobJS.getMob(mob).getMobEntity(), stacksNeeded);
    }

    public ItemQuest setAdder(String mob) {
        adder = MobJS.getMob(mob).getMobEntity();
        adderId = adder.getUUID();
        return this;
    }

    public ItemQuest addStack(String item) {
        stacksNeeded.add(JSItem.getStack(item));
        return this;
    }

    public ItemQuest addStacks(NativeArray items) {
        for (int i=0; i<items.getLength(); i++) {
            stacksNeeded.add(JSItem.getStack((String) items.get(i)));
        }
        return this;
    }

    public boolean tryComplete(String player) {
        return tryComplete(Server.getPlayerByName(player));
    }

    public int getStacksNeeded() {
        return stacksNeeded.size();
    }

    public int getStacksBrought() {
        return stacksBrought.size();
    }

    public int getStacksLeft() {
        return stacksNeeded.size() - stacksBrought.size();
    }

    public ServerPlayerEntity getPlayerCompleted() {
        return playerCompleted;
    }

    public LivingEntity getAdder() {
        return adder;
    }

    public UUID getAdderId() {
        return adderId;
    }

    public String getBringMessage() {
        return bringMessage;
    }

    public ItemQuest runOnComplete() {
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction function : onComplete) {
                function.call(Context.getCurrentContext(),
                        SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{playerCompleted.getName().getContents(), completed});
            }
        });
        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        ItemQuest ef = new ItemQuest();
        ef.setParentScope(scope);
        try {
            Method create = ItemQuest.class.getMethod("create", String.class, NativeArray.class);
            methodsToAdd.add(create);
            Method setAdder = ItemQuest.class.getMethod("setAdder", String.class);
            methodsToAdd.add(setAdder);
            Method addStack = ItemQuest.class.getMethod("addStack", String.class);
            methodsToAdd.add(addStack);
            Method addStacks = ItemQuest.class.getMethod("addStacks", NativeArray.class);
            methodsToAdd.add(addStacks);
            Method tryComplete = ItemQuest.class.getMethod("tryComplete", String.class);
            methodsToAdd.add(tryComplete);
            Method getStacksNeeded = ItemQuest.class.getMethod("getStacksNeeded");
            methodsToAdd.add(getStacksNeeded);
            Method getStacksBrought = ItemQuest.class.getMethod("getStacksBrought");
            methodsToAdd.add(getStacksBrought);
            Method getStacksLeft = ItemQuest.class.getMethod("getStacksLeft");
            methodsToAdd.add(getStacksLeft);
            Method getPlayerCompleted = ItemQuest.class.getMethod("getPlayerCompleted");
            methodsToAdd.add(getPlayerCompleted);
            Method getAdder = ItemQuest.class.getMethod("getAdder");
            methodsToAdd.add(getAdder);
            Method getAdderId = ItemQuest.class.getMethod("getAdderId");
            methodsToAdd.add(getAdderId);
            Method addOnComplete = ItemQuest.class.getMethod("addOnComplete", BaseFunction.class);
            methodsToAdd.add(addOnComplete);
            Method isCompleted = ItemQuest.class.getMethod("isCompleted");
            methodsToAdd.add(isCompleted);
            Method setBringMessage = ItemQuest.class.getMethod("setBringMessage", String.class);
            methodsToAdd.add(setBringMessage);
            Method getBringMessage = ItemQuest.class.getMethod("getBringMessage");
            methodsToAdd.add(getBringMessage);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("itemQuest", scope, ef);
    }

    @Override
    public String getClassName() {
        return "ItemQuest";
    }

    @Override
    public String getResourceId() {
        return "ItemQuest";
    }
}
// "text.storyverse.quests.item.bring": "%-0% Bring me %COUNT% %ITEM%!"