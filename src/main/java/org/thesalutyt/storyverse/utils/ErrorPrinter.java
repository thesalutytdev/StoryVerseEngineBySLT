package org.thesalutyt.storyverse.utils;

public class ErrorPrinter {
    public ErrorPrinter(Exception e) {
        System.out.println("[ERROR]: " + e.getMessage());
        System.out.println("Because: " + e.getCause());
    }
}
