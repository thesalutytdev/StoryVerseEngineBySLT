package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.camera.cutscene.math.InterpolationCalculator;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MathScript extends ScriptableObject implements EnvResource {

    public static Double PI() {
        return Math.PI;
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
    public static Double random(Double min, Double max) {
        return Math.random() * (max - min + 1) + min;
    }

    public static Double abs(Double var0) {
        return StrictMath.abs(var0);
    }

    public static Double ceil(Double var0) {
        return StrictMath.ceil(var0);
    }

    public static Double floor(Double var0) {
        return StrictMath.floor(var0);
    }

    public static Double round(Double var0) {
        return (double) StrictMath.round(var0);
    }

    public static Double pow(Double var0, Double var1) {
        return StrictMath.pow(var0, var1);
    }

    public static Double exp(Double var0) {
        return StrictMath.exp(var0);
    }

    public static Double log(Double var0) {
        return StrictMath.log(var0);
    }

    public static Double log10(Double var0) {
        return StrictMath.log10(var0);
    }

    public Integer factorial (Integer target) {
        if (target <= 1) {
            return target;
        } else {
            return target * factorial(target-1);
        }
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

    public static ArrayList<Double> getRange(Double min, Double max) {
        ArrayList<Double> range = new ArrayList<>();
        for (double i = 0; i <= (max-min); i += 0.1) {
            range.add(i + min);
        }

        return range;
    }


    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        Server ef = new Server();
        ef.setParentScope(scope);

        try {
            Method pow = MathScript.class.getMethod("pow", Double.class, Double.class);
            methodsToAdd.add(pow);
            Method exp = MathScript.class.getMethod("exp", Double.class);
            methodsToAdd.add(exp);
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
            Method random2 = MathScript.class.getMethod("random", Double.class, Double.class);
            methodsToAdd.add(random2);
            Method toRadians = MathScript.class.getMethod("toRadians", Double.class);
            methodsToAdd.add(toRadians);
            Method toDegrees = MathScript.class.getMethod("toDegrees", Double.class);
            methodsToAdd.add(toDegrees);
            Method sqrt = MathScript.class.getMethod("sqrt", Double.class);
            methodsToAdd.add(sqrt);
            Method abs = MathScript.class.getMethod("abs", Double.class);
            methodsToAdd.add(abs);
            Method ceil = MathScript.class.getMethod("ceil", Double.class);
            methodsToAdd.add(ceil);
            Method floor = MathScript.class.getMethod("floor", Double.class);
            methodsToAdd.add(floor);
            Method round = MathScript.class.getMethod("round", Double.class);
            methodsToAdd.add(round);
            Method log = MathScript.class.getMethod("log", Double.class);
            methodsToAdd.add(log);
            Method log10 = MathScript.class.getMethod("log10", Double.class);
            methodsToAdd.add(log10);
            Method factorial = MathScript.class.getMethod("factorial", Integer.class);
            methodsToAdd.add(factorial);
            Method getRange = MathScript.class.getMethod("getRange", Double.class, Double.class);
            methodsToAdd.add(getRange);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("math", scope, ef);
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
