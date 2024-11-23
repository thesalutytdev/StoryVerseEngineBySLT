package org.thesalutyt.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClassSaver {
    public static Class<?> restore(String json, Class<?> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        return (Class<?>) gson.fromJson(json, clazz);
    }

    public static String save(Class<?> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        return gson.toJson(clazz);
    }

    public static String save(Object object) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        return gson.toJson(object);
    }

    public static Object restore(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        return gson.fromJson(json, Object.class);
    }
}
