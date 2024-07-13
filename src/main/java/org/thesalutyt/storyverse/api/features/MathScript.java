package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MathScript extends ScriptableObject implements EnvResource {

    public static Double PI() {
        return 3.141592653589793;
    }

    public static Double sin(Double var0) {
        return Math.sin(var0);
    }

    public static Double cos(Double var0) {
        return StrictMath.cos(var0);
    }

    public static Double tan(Double var0) {
        return StrictMath.tan(var0);
    }

    public static Double asin(Double var0) {
        return StrictMath.asin(var0);
    }

    public static Double acos(Double var0) {
        return StrictMath.acos(var0);
    }

    public static Double atan(Double var0) {
        return StrictMath.atan(var0);
    }

    public static Double random() {
        return Math.random();
    }

    public static Double toRadians(Double var0) {
        return Math.toRadians(var0);
    }

    public static Double toDegrees(Double var0) {
        return Math.toDegrees(var0);
    }

    public static Double sqrt(Double var0) {
        return StrictMath.sqrt(var0);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        Server ef = new Server();
        ef.setParentScope(scope);

        try {
            Method PI = MathScript.class.getMethod("PI");
            methodsToAdd.add(PI);
            Method sin = MathScript.class.getMethod("sin", Double.class);
            methodsToAdd.add(sin);
            Method cos = MathScript.class.getMethod("cos", Double.class);
            methodsToAdd.add(cos);
            Method tan = MathScript.class.getMethod("tan", Double.class);
            methodsToAdd.add(tan);
            Method asin = MathScript.class.getMethod("asin", Double.class);
            methodsToAdd.add(asin);
            Method acos = MathScript.class.getMethod("acos", Double.class);
            methodsToAdd.add(acos);
            Method atan = MathScript.class.getMethod("atan", Double.class);
            methodsToAdd.add(atan);
            Method random = MathScript.class.getMethod("random");
            methodsToAdd.add(random);
            Method toRadians = MathScript.class.getMethod("toRadians", Double.class);
            methodsToAdd.add(toRadians);
            Method toDegrees = MathScript.class.getMethod("toDegrees", Double.class);
            methodsToAdd.add(toDegrees);
            Method sqrt = MathScript.class.getMethod("sqrt", Double.class);
            methodsToAdd.add(sqrt);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("Math", scope, ef);
    }
    @Override
    public String getResourceId() {
        return "Math";
    }

    @Override
    public String getClassName() {
        return "Math";
    }
}
