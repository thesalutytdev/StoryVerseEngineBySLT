package org.thesalutyt.storyverse.fs_environment.file;

import java.io.File;
import java.io.FileFilter;

public class Filters {
    public static FileFilter onlyDir = File::isDirectory;
    public static FileFilter onlyStory = (file) -> {return file.getName().endsWith(".sv");};
    public static FileFilter onlyJs = (file) -> {return file.getName().endsWith(".js");};
    public Filters() {}
}
