package org.thesalutyt.storyverse.api.environment.js.mod;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.features.Chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyze extends ScriptableObject {
    public void analyze() {
        Path fullPath = Paths.get(SVEngine.SCRIPTS_PATH + "/" + SVEngine.MOD_SCRIPT_FILE).toAbsolutePath();
        if (fullPath.startsWith(SVEngine.SCRIPTS_PATH)) {
            try {
                Scanner reader = new Scanner(new InputStreamReader(new FileInputStream(fullPath.toFile()), StandardCharsets.UTF_8));
                while (reader.hasNextLine()) {
                    SVEngine.modInterpreter.executeString(reader.nextLine());
                }
            } catch (final FileNotFoundException e) {
                System.out.println("Mod script file not found");
            }
        } else {
            return;
        }
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        Analyze a = new Analyze();
        a.setParentScope(scope);
        try {
            Method create = Analyze.class.getMethod("analyze");
            methodsToAdd.add(create);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, a);
            a.put(m.getName(), a, methodInstance);
        }
        scope.put("analyze", scope, a);
    }

    @Override
    public String getClassName() {
        return "Analyze";
    }
}