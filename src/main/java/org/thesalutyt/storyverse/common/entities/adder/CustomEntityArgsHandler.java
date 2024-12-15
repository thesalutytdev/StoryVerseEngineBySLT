package org.thesalutyt.storyverse.common.entities.adder;

import org.thesalutyt.storyverse.common.entities.adder.essential.arguments.EntityArgumentList;

import java.util.HashMap;

public class CustomEntityArgsHandler {
    protected static HashMap<String, EntityArgumentList> arguments = new HashMap<>();

    public static EntityArgumentList getArgs(String name) {
        return arguments.get(name);
    }

    public static void setArgs(String name, EntityArgumentList args) {
        arguments.put(name, args);
    }

    public static void removeArgs(String name) {
        arguments.remove(name);
    }

    public static boolean contains(String name) {
        return arguments.containsKey(name);
    }
}
