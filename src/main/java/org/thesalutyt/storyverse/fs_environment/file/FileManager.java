package org.thesalutyt.storyverse.fs_environment.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class FileManager {
    private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public FileManager() {}

    public static InputStreamReader createInput(File f) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
    }

    public static OutputStreamWriter createOutput(File f) throws FileNotFoundException {
        return new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
    }

    public static void javaToJson(File f, Object data) {
        try {
            f.createNewFile();
            OutputStreamWriter writer = createOutput(f);
            gson.toJson(data, writer);
            writer.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }
    public static void listInDirAndDo(File f, FileFilter filter, Consumer<File> cb) {
        f.mkdir();
        File[] files = f.listFiles(filter);
        File[] var4 = files;
        int var5 = files.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File ff = var4[var6];
            cb.accept(ff);
        }

    }

    public static void listInDirAndDo(File f, String dir, FileFilter filter, Consumer<File> cb) {
        f.mkdir();
        listInDirAndDo(new File(f, dir), filter, cb);
    }

    public static <T> T jsonToJava(File f, Class<T> classOf) throws InstantiationException, IllegalAccessException {
        try {
            f.createNewFile();
            InputStreamReader reader = createInput(f);
            T data = gson.fromJson(reader, classOf);
            if (data == null) {
                data = classOf.newInstance();
            }

            javaToJson(f, data);
            return data;
        } catch (Exception var4) {
            var4.printStackTrace();
            return classOf.newInstance();
        }
    }
}
