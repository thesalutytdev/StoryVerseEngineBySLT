package org.thesalutyt.storyverse.api.environment.js.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class JSEnchant extends ScriptableObject implements EnvResource, JSResource {
    public static HashMap<String, Enchantment> enchantments = new HashMap<>();

    public static Enchantment enchant(Object location) {
        if (!(location instanceof ResourceLocation)) throw new RuntimeException("Location must be an instance of ResourceLocation");

        enchantments.put(((ResourceLocation) location).getPath(),
                GameRegistry.findRegistry(Enchantment.class).getValue((ResourceLocation) location));

        return GameRegistry.findRegistry(Enchantment.class).getValue((ResourceLocation) location);
    }

    public static Enchantment enchant(String mod, String id) {
        return enchant(new ResourceLocation(mod, id));
    }

    public static Enchantment enchant(String id) {
        return enchant(ResourceLocation.tryParse(id));
    }

    public static Enchantment getEnchant(String mod, String id) {
        return enchant(new ResourceLocation(mod, id));
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        JSEnchant ef = new JSEnchant();
        ef.setParentScope(scope);

        try {
            Method enchant = JSEnchant.class.getMethod("enchant", Object.class);
            methodsToAdd.add(enchant);
            Method enchant2 = JSEnchant.class.getMethod("enchant", String.class, String.class);
            methodsToAdd.add(enchant2);
            Method enchant3 = JSEnchant.class.getMethod("enchant", String.class);
            methodsToAdd.add(enchant3);
            Method getEnchant = JSEnchant.class.getMethod("getEnchant", String.class, String.class);
            methodsToAdd.add(getEnchant);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("enchant", scope, ef);
    }

    @Override
    public String getClassName() {
        return "JSEnchant";
    }

    @Override
    public String getResourceId() {
        return "JSEnchant";
    }
}
