package org.thesalutyt.storyverse.api.environment.js;

import net.minecraft.util.ResourceLocation;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LocationCreator extends ScriptableObject implements EnvResource, JSResource {
    public static HashMap<String, ResourceLocation> locations = new HashMap<>();
    public static ResourceLocation createLocation(String path) {
        if (!path.startsWith(SVEngine.ASSETS_DIR)) {
            return null;
        } else {
            locations.put(path, new ResourceLocation(path));
            return new ResourceLocation(path);
        }
    }
    public static ResourceLocation getByPath(String path) {
        return locations.get(path);
    }
    public static String getAssetsPath() {
        return SVEngine.ASSETS_DIR;
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        LocationCreator ef = new LocationCreator();
        ef.setParentScope(scope);

        try {
            Method createLocation = LocationCreator.class.getMethod("createLocation", String.class);
            methodsToAdd.add(createLocation);
            Method getByPath = LocationCreator.class.getMethod("getByPath", String.class);
            methodsToAdd.add(getByPath);
            Method getAssetsPath = LocationCreator.class.getMethod("getAssetsPath");
            methodsToAdd.add(getAssetsPath);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("location", scope, ef);
    }

    @Override
    public String getClassName() {
        return "LocationCreator";
    }

    @Override
    public String getResourceId() {
        return "LocationCreator";
    }
}
