package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.mozilla.javascript.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.thesalutyt.storyverse.api.features.Chat;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
    Небольшое пояснение.
    Гипотетически можно все вынести в пространство имен верхнего уровня. Тогда все функции будут доступны сразу.
    Практически, лучше отделять мух от котлет и распихивать функции тематически. Я здесь создал класс ExternalFunctions
    с разнымы функциями. Я совместил мух и котлет, но мне нужно было понять, как должен создаваться такой объект.

    ScriptableObject - это общий класс для всего в JS движке. По сути так можно описать любой объект, который
    ты потом захочешь там использовать, надо просто расширить интерфейс ScriptableObject.
*/

public class ExternalFunctions extends ScriptableObject {
    public static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    /*
        Новое тут. Это путь до корневой директории со скриптами. Зачем это нужно смотри в import_file здесь же
    * */
    private final Path rootDir;

    public ExternalFunctions (String rootDir) {
        this.rootDir = Paths.get(rootDir).toAbsolutePath();
    }

    /*
    Метод getClassName просто надо было переопределить. Забей и повторяй, тут нет особой премудрости, скорее это просто
    формальность.
    */
    @Override
    public String getClassName() {
        return getClass().getName();
    }

    /*
    В статическом методе putIntoScope, этот объект создается и укладывается в пространство имен, которое ему передают.
    Расценивай putIntoScope как экзотический конструктор объекта.
    */
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope, String rootDir) {
        // Создаем объект, к которому потом будем обращаться
        ExternalFunctions ef = new ExternalFunctions(rootDir);
        // Это не обязательно, но я указываю, что у этого объекта есть объект выше уровнем
        ef.setParentScope(scope);

        // Это список функций, которые потом будут добавлены в объект ExternalFunctions
        // Здесь просто повторяй, я и сам не стал сильно глубоко копаться в деталях

        try {
            Method factorial = ExternalFunctions.class.getMethod("factorial", Integer.class);
            methodsToAdd.add(factorial);
            Method log = ExternalFunctions.class.getMethod("log", Object.class);
            methodsToAdd.add(log);
            Method import_file = ExternalFunctions.class.getMethod("import_file", String.class);
            methodsToAdd.add(import_file);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // Здесь функции укладываются в ExternalFunctions
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        // Здесь ExternalFunctions укладывается в пространство имен верхнего уровня
        scope.put("ExternalFunctions", scope, ef);
    }

    public Integer factorial (Integer target) {
        if (target <= 1) {
            return target;
        } else {
            return target * factorial(target-1);
        }
    }

    public void log (Object object) {
        Date now = Calendar.getInstance().getTime();
        System.out.println("[" + ExternalFunctions.DEFAULT_DATE_FORMAT.format(now) + "] " + object.toString());
    }

    public void import_file (String path_to_file) {
        /* Тут внимательно
            Надо обязательно проверять, не пытаются ли вылезти за пределы допустимых границ.
            В интерпретаторе указыватся rootDir - это корневая директория скриптов.
            Если кто-то написал скрипт, который лезет за пределы этой директории - мы его шлем нахрен с ошибкой.
            Остальное ты уже видел
         */

        Path fullPath = Paths.get(this.rootDir + "/" + path_to_file).toAbsolutePath();
        if (fullPath.startsWith(this.rootDir)) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(fullPath.toString()), StandardCharsets.UTF_8);
                Context ctx = Context.getCurrentContext();
                ctx.evaluateReader(
                        this.getParentScope(),
                        reader,
                        fullPath.toString(),
                        1,
                        null
                );
            } catch (final FileNotFoundException e) {
                System.out.println("Invalid path (" + path_to_file + "): file not found");
                Chat.sendError("Invalid path (" + path_to_file + "): file not found");
            } catch (final IOException e) {
                System.out.println("Invalid path (" + path_to_file + "): IOException " + e);
                Chat.sendError("Invalid path (" + path_to_file + "): IOException " + e);
            } catch (final RhinoException e) {
                System.out.println("Script error: " + e);
                Chat.sendError("Script error: " + e);
            } catch (final Exception e) {
                System.out.println("Java exception: " + e.getMessage());
                Chat.sendError("Java exception: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid path (" + path_to_file + "): path ends outside root script directory");
        }
    }
}