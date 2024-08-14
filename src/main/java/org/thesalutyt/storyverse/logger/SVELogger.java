package org.thesalutyt.storyverse.logger;

import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SVELogger {
    @Documentate(
            desc = "Creates log file"
    )
    private static void create_file(String file_name) {
        try {
            final String full_file_path = SVEngine.LOGS_PATH + file_name + SVEngine.LOG_FILE_FORMAT;
            File file = new File(full_file_path);
            if (file.createNewFile()) {
                SVEngine.LOG_FILE_PATH = full_file_path;
                SVEngine.LOG_FILE = file;
                System.out.println(SVEngine.PREFIX + " Successfully created new log file " + full_file_path);
            }
        } catch (IOException e) {
            System.out.println(SVEngine.PREFIX + " Can not create log file with this exception:\n" + e.getMessage());
        }
    }
    public static void create_file(String file_path, String file_name) {
        try {
            String full_path;
            if (file_path.endsWith("/") || file_name.startsWith("/")) {
                full_path = file_path + file_name;
            } else if (file_path.endsWith("/") && file_name.startsWith("/")) {
                return;
            } else {
                full_path = file_path + "/" + file_name;
            }
            File file = new File(full_path);
            if (file.createNewFile()) {
                System.out.println("Created new file " + full_path);
            }
        } catch (IOException e) {
            System.out.println("Caught unexpected error while creating file:\n | " + e.getMessage());
        }
    }

    public static String init (String story_name) {
        create_file(story_name);
        System.out.println(SVEngine.PREFIX + " Created log file");
        return SVEngine.LOGS_PATH + story_name;
    };
    public static void write (String file_path, String text) {
        try{
            FileWriter writer = new FileWriter(file_path);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            System.out.println(SVEngine.PREFIX + " Error occurred when tried to write in file || " + file_path + " ||");
            System.out.println(SVEngine.PREFIX + " Error: " + e.getMessage());
        }
    };
    public static void create_dir(String path){
        File file = new File(path);
        if (file.mkdirs()) {
            System.out.println(SVEngine.PREFIX + " Created directory " + path);
        }
        else {
            System.out.println(SVEngine.PREFIX + " Directory " + path + " cannot be created");
        }
    }
}