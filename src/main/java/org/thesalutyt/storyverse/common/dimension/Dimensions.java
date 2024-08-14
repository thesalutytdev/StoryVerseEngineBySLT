package org.thesalutyt.storyverse.common.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static net.minecraft.util.registry.Registry.DIMENSION_REGISTRY;

public class Dimensions extends ScriptableObject implements EnvResource, JSResource {
    public static ArrayList<RegistryKey<World>> dimensions = new ArrayList<>();

    public static void addDimension(RegistryKey<World> dimension) {
        dimensions.add(dimension);
    }

    public static void addDimension(String name) {
        dimensions.add(RegistryKey.create(DIMENSION_REGISTRY, new ResourceLocation(StoryVerse.MOD_ID, name)));
    }

    public static void removeDimension(RegistryKey<World> dimension) {
        dimensions.remove(dimension);
    }

    public static void register(IEventBus eventBus) {
        dimensions.forEach(eventBus::register);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        Dimensions ef = new Dimensions();
        ef.setParentScope(scope);

        try {
            Method addDimension = Dimensions.class.getMethod("addDimension", String.class);
            methodsToAdd.add(addDimension);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("dimensions", scope, ef);
    }

    @Override
    public String getClassName() {
        return "Dimensions";
    }

    @Override
    public String getResourceId() {
        return "Dimensions";
    }
}
