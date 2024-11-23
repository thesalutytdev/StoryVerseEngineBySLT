package org.thesalutyt.json;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class Serializer {
    public static String listToJson(ArrayList<?> list) {
        return new Gson().toJson(list);
    }

    public static ArrayList<?> jsonToList(String json) {
        return new Gson().fromJson(json, ArrayList.class);
    }

    public static String objectToJson(Object object) {
        return new Gson().toJson(object);
    }

    public static Object jsonToObject(String json, Class<?> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static String mapToJson(HashMap<?, ?> map) {
        return new Gson().toJson(map);
    }

    public static HashMap<?, ?> jsonToMap(String json) {
        return new Gson().fromJson(json, HashMap.class);
    }
}