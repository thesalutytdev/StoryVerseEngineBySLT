package org.thesalutyt.storyverse.api.environment.js.minecraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class JSItem extends ScriptableObject implements EnvResource, JSResource {
    public String id;
    public Integer count;
    public String nbt;
    public CompoundNBT NBT;
    public ItemStack item;
    public static HashMap<String, JSItem> items = new HashMap<>();

    public JSItem() {}
    public JSItem(String resourceId, Integer count, String nbt) {
        this.id = resourceId;
        this.count = count;
        this.nbt = nbt;
        this.NBT = (CompoundNBT) new CompoundNBT().put(this.nbt, new CompoundNBT());
        this.item = new ItemStack(Registry.ITEM.get(new ResourceLocation(this.id.split(":")[0],
                this.id.split(":")[1])),
                this.count, this.NBT);
        items.put(this.id, this);
    }
    public ItemStack getStack() {
        return this.item;
    }
    public Item getItem() {
        return this.item.getItem();
    }
    public static ItemStack getStack(String id) {
        return items.get(id).item;
    }
    public static Item getItem(String id) {
        return items.get(id).item.getItem();
    }
    public String itemStack(String resourceId, Integer count, String nbt) {
        this.id = resourceId;
        this.count = count;
        this.nbt = nbt;
        this.NBT = (CompoundNBT) new CompoundNBT().put(this.nbt, new CompoundNBT());
        this.item = new ItemStack(Registry.ITEM.get(new ResourceLocation(this.id.split(":")[0],
                this.id.split(":")[1])),
                this.count, this.NBT);
        items.put(this.id, this);
        return this.id;
    }
    public String item(String resourceId, Integer count, String nbt) {
        this.id = resourceId;
        this.count = count;
        this.nbt = nbt;
        this.NBT = (CompoundNBT) new CompoundNBT().put(this.nbt, new CompoundNBT());
        this.item = new ItemStack(Registry.ITEM.get(new ResourceLocation(this.id.split(":")[0],
                this.id.split(":")[1])),
                this.count, this.NBT);
        items.put(this.id, this);
        return this.id;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        JSItem ji = new JSItem();
        ji.setParentScope(scope);

        try {
            Method item = JSItem.class.getMethod("item", String.class, Integer.class, String.class);
            methodsToAdd.add(item);
            Method itemStack = JSItem.class.getMethod("itemStack", String.class, Integer.class, String.class);
            methodsToAdd.add(itemStack);
            Method getItem = JSItem.class.getMethod("getItem", String.class);
            methodsToAdd.add(getItem);
            Method getItemStack = JSItem.class.getMethod("getStack", String.class);
            methodsToAdd.add(getItemStack);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ji);
            ji.put(m.getName(), ji, methodInstance);
        }
        scope.put("item", scope, ji);
    }


    @Override
    public String getResourceId() {
        return "JSItem";
    }

    @Override
    public String getClassName() {
        return "JSItem";
    }
}
