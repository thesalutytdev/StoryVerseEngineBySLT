package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.thesalutyt.storyverse.SVEngine;

import java.util.Scanner;

public class Main {
    public static void run_some_test_idk (String[] args) {
        String path_to_root = SVEngine.SCRIPTS_PATH;
        Interpreter i = new Interpreter(path_to_root);

//        i.executeString("ExternalFunctions.import_file(\"main.js\");");

        /*
            Поясняю
            Тут цикл, который ждет исполнения. EventLoop который я сделал - это не совсем EventLoop,
            это на самом деле небольшой костыль, который выносит работу со скриптами в отдельный поток.
            Если ты захочешь подробных разъяснений про многопоточность - напиши, я объясню тебе все.
            По сути мы тут делаем то же самое, только теперь можно попросить интерпретатор исполнить
            Async.setTimeout(fn, delay);
            Async.clearTimeout(timeoutId);
            Async.setInterval(fn, delay);
            Async.clearInterval(intervalId);
            и это будет аналогично (почти) джаваскриптовому setTimeout, setInterval
        * */
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            String script = sc.nextLine();

            if (script.equals("exit")) {
                exit = true;
            } else {
                i.executeString(script);
            }
        }

        i.close();
    }
}
