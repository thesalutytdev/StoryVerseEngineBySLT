package org.thesalutyt.storyverse.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;

public class SVELogger {
    @Documentate(
            desc = "Creates log file"
    )
    private static void create_file(String file_name) {
        try {
            final String full_file_path = "../story/output/" + file_name + SVEngine.LOG_FILE_FORMAT;
            File file = new File(full_file_path);
            if (file.createNewFile()) {
                SVEngine.LOG_FILE_PATH = full_file_path;
                SVEngine.LOG_FILE = file;
                System.out.println(SVEngine.prefix + " Successfully created new log file " + full_file_path);
            }
        } catch (IOException e) {
            System.out.println(SVEngine.prefix + " Can not create log file with this exception:\n" + e.getMessage());
        }
    };

    public static void init (String story_name) {
        create_file(story_name);
        System.out.println(SVEngine.prefix + " Created log file");
    };
    public static void write (String file_path, String text) {
        try{
            FileWriter writer = new FileWriter(file_path);
            writer.write(text);
        } catch (IOException e) {
            System.out.println(SVEngine.prefix + " Error occurred when tried to write in file || " + file_path + " ||");
            System.out.println(SVEngine.prefix + " Error: " + e.getMessage());
        }
    };
    public static void create_dir(String path){
        File file = new File(path);
        if (file.mkdirs()) {
            System.out.println(SVEngine.prefix + " Created directory " + path);
        }
        else {
            System.out.println(SVEngine.prefix + " Directory " + path + " cannot be created");
        }
    }
}