package org.thesalutyt.storyverse.api.environment.js.minecraft.block;

import net.minecraft.block.Block;
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

public class JSBlock extends ScriptableObject implements EnvResource, JSResource {
    public static HashMap<String, Block> blocks = new HashMap<>();

    public static String block(Object location) {
        if (!(location instanceof ResourceLocation)) {
            throw new RuntimeException("Location must be an instance of ResourceLocation");
        }
        if (!blocks.containsKey(((ResourceLocation) location).toString())) {
            blocks.put(((ResourceLocation) location).toString(),
                    GameRegistry.findRegistry(Block.class).getValue(((ResourceLocation) location)));
        }
        return ((ResourceLocation) location).toString();
    }

    public static String block(String mod, String name) {
        if (!blocks.containsKey(new ResourceLocation(mod, name).toString())) {
            blocks.put(new ResourceLocation(mod, name).toString(),
                    GameRegistry.findRegistry(Block.class).getValue(ResourceLocation.tryParse(
                            new ResourceLocation(mod, name).toString())));
        }
        return new ResourceLocation(mod, name).toString();
    }

    public static String block(String name) {
        if (!blocks.containsKey(new ResourceLocation(name).toString())) {
            blocks.put(new ResourceLocation(name).toString(),
                    GameRegistry.findRegistry(Block.class).getValue(ResourceLocation.tryParse(
                            new ResourceLocation(name).toString())));
        }
        return new ResourceLocation(name).toString();
    }

    public static Block getBlock(String mod, String name) {
        if (!blocks.containsKey(new ResourceLocation(mod, name).toString())) {
            blocks.put(new ResourceLocation(mod, name).toString(),
                    GameRegistry.findRegistry(Block.class).getValue(ResourceLocation.tryParse(
                            new ResourceLocation(mod, name).toString())));
        }
        return blocks.get(new ResourceLocation(mod, name).toString());
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        JSBlock ef = new JSBlock();
        ef.setParentScope(scope);
        try {
            Method block = JSBlock.class.getMethod("block", Object.class);
            methodsToAdd.add(block);
            Method block2 = JSBlock.class.getMethod("block", String.class);
            methodsToAdd.add(block2);
            Method block3 = JSBlock.class.getMethod("block", String.class, String.class);
            methodsToAdd.add(block3);
            Method getBlock = JSBlock.class.getMethod("getBlock", String.class, String.class);
            methodsToAdd.add(getBlock);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("block", scope, ef);
    }

    @Override
    public String getClassName() {
        return "JSBlock";
    }

    @Override
    public String getResourceId() {
        return "JSBlock";
    }
}
