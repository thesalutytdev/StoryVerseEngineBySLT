package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Scanner;

public class File extends ScriptableObject implements EnvResource, JSResource {

    public static String read(String path) {
        try {
            java.io.File file = new java.io.File(path);
            Scanner scanner = new Scanner(file);
            StringBuilder fileContains = new StringBuilder();
            while (scanner.hasNextLine()) {
                fileContains.append(scanner.nextLine());
            }

            return fileContains.toString();
        } catch (FileNotFoundException e) {
            new ErrorPrinter(e);
            return "File not found";
        }
    }

    public static boolean create(String path) {
        java.io.File file = new java.io.File(path);
        try {
            return file.createNewFile();
        } catch (FileAlreadyExistsException e) {
            new ErrorPrinter(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void write(String path, String data) {
        java.io.File file = new java.io.File(path);

        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear(String path) {
        java.io.File file = new java.io.File(path);
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void append(String path, String data) {
        java.io.File file = new java.io.File(path);
        try {
            java.nio.file.Files.write(file.toPath(), data.getBytes(), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete_file(String path) {
        java.io.File file = new java.io.File(path);
        return file.delete();
    }

    public static boolean exists(String path) {
        java.io.File file = new java.io.File(path);
        return file.exists();
    }

    public static boolean is_file(String path) {
        java.io.File file = new java.io.File(path);
        return file.isFile();
    }

    public static boolean is_dir(String path) {
        java.io.File file = new java.io.File(path);
        return file.isDirectory();
    }

    public static boolean is_hidden(String path) {
        java.io.File file = new java.io.File(path);
        return file.isHidden();
    }

    public static boolean is_absolute(String path) {
        java.io.File file = new java.io.File(path);
        return file.isAbsolute();
    }

    public static String get_absolute_path(String path) {
        java.io.File file = new java.io.File(path);
        return file.getAbsolutePath();
    }

    public static String get_name(String path) {
        java.io.File file = new java.io.File(path);
        return file.getName();
    }

    public static String get_parent(String path) {
        java.io.File file = new java.io.File(path);
        return file.getParent();
    }

    public static String get_extension(String path) {
        java.io.File file = new java.io.File(path);
        return file.getName().substring(file.getName().lastIndexOf(".") + 1);
    }

    public static String get_name_without_extension(String path) {
        java.io.File file = new java.io.File(path);
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        File f = new File();
        f.setParentScope(scope);

        try {
            Method read = File.class.getMethod("read", String.class);
            methodsToAdd.add(read);
            Method create = File.class.getMethod("create", String.class);
            methodsToAdd.add(create);
            Method write = File.class.getMethod("write", String.class, String.class);
            methodsToAdd.add(write);
            Method append = File.class.getMethod("append", String.class, String.class);
            methodsToAdd.add(append);
            Method delete_file = File.class.getMethod("delete_file", String.class);
            methodsToAdd.add(delete_file);
            Method exists = File.class.getMethod("exists", String.class);
            methodsToAdd.add(exists);
            Method is_file = File.class.getMethod("is_file", String.class);
            methodsToAdd.add(is_file);
            Method is_dir = File.class.getMethod("is_dir", String.class);
            methodsToAdd.add(is_dir);
            Method is_hidden = File.class.getMethod("is_hidden", String.class);
            methodsToAdd.add(is_hidden);
            Method is_absolute = File.class.getMethod("is_absolute", String.class);
            methodsToAdd.add(is_absolute);
            Method get_absolute_path = File.class.getMethod("get_absolute_path", String.class);
            methodsToAdd.add(get_absolute_path);
            Method get_name = File.class.getMethod("get_name", String.class);
            methodsToAdd.add(get_name);
            Method get_parent = File.class.getMethod("get_parent", String.class);
            methodsToAdd.add(get_parent);
            Method get_extension = File.class.getMethod("get_extension", String.class);
            methodsToAdd.add(get_extension);
            Method get_name_without_extension = File.class.getMethod("get_name_without_extension", String.class);
            methodsToAdd.add(get_name_without_extension);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, f);
            f.put(m.getName(), f, methodInstance);
        }

        scope.put("file", scope, f);
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public String getResourceId() {
        return "";
    }
}
