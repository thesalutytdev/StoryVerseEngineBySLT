package org.thesalutyt.storyverse.common.tabs.adder;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.elements.ICustomElement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomTab extends ScriptableObject implements ICustomElement, JSResource, EnvResource {
    public static HashMap<String, CustomTab> tabs = new HashMap<>();
    public ItemGroup tab;
    public void create(String name, Boolean hasSearch, Boolean canScroll) {
        tab = new ItemGroup(name) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(Items.DIAMOND);
            }
        };
        if (hasSearch) {
            tab.hasSearchBar();
        }
        if (canScroll) {
            tab.canScroll();
        }
        tabs.put(name, this);
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        CustomTab tab = new CustomTab();
        tab.setParentScope(scope);
        try {
            Method create = CustomTab.class.getMethod("create", String.class, Boolean.class, Boolean.class);
            methodsToAdd.add(create);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, tab);
            tab.put(m.getName(), tab, methodInstance);
        }
        scope.put("tab", scope, tab);
    }

    @Override
    public Object getDefaultElement() {
        return ItemGroup.class;
    }

    @Override
    public String getClassName() {
        return "CustomTabs";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "CustomTabs";
    }
}
