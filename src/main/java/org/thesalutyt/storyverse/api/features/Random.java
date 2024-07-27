package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Random extends ScriptableObject implements EnvResource, JSResource {
    public static Object choice(NativeArray array) {
        int random_index = new java.util.Random().nextInt((int) array.getLength());
        return array.get(random_index);
    }

    public static Integer randInt(Integer min, Integer max) {
        return new java.util.Random().nextInt((max - min) + 1) + min;
    }

    public static Double randDouble(Double min, Double max) {
        return new java.util.Random().nextDouble() * (max - min) + min;
    }

    public static Double randomDouble() {
        return new java.util.Random().nextDouble();
    }

    public static Integer randomInt() {
        return new java.util.Random().nextInt();
    }

    public static NativeArray shuffle(NativeArray array) {
        java.util.Random random = new java.util.Random();
        for (int i = (int) (array.getLength() - 1); i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object temp = array.get(index);
            array.set(index, array.get(i));
            array.set(i, temp);
        }
        return array;
    }

    public static NativeArray shuffle(NativeArray array, Integer seed) {
        java.util.Random random = new java.util.Random(seed);
        for (int i = (int) (array.getLength() - 1); i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object temp = array.get(index);
            array.set(index, array.get(i));
            array.set(i, temp);
        }
        return array;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        Random rn = new Random();
        rn.setParentScope(scope);

        try {
            Method choice = Random.class.getMethod("choice", NativeArray.class);
            methodsToAdd.add(choice);
            Method randInt = Random.class.getMethod("randInt", Integer.class, Integer.class);
            methodsToAdd.add(randInt);
            Method randDouble = Random.class.getMethod("randDouble", Double.class, Double.class);
            methodsToAdd.add(randDouble);
            Method randomDouble = Random.class.getMethod("randomDouble");
            methodsToAdd.add(randomDouble);
            Method randomInt = Random.class.getMethod("randomInt");
            methodsToAdd.add(randomInt);
            Method shuffle = Random.class.getMethod("shuffle", NativeArray.class);
            methodsToAdd.add(shuffle);
            Method shuffle2 = Random.class.getMethod("shuffle", NativeArray.class, Integer.class);
            methodsToAdd.add(shuffle2);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, rn);
            rn.put(m.getName(), rn, methodInstance);
        }

        scope.put("random", scope, rn);
    }

    @Override
    public String getClassName() {
        return "Random";
    }

    @Override
    public String getResourceId() {
        return "Random";
    }
}
