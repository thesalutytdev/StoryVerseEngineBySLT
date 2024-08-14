package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.common.events.ModEvents;
import org.thesalutyt.storyverse.logger.SVELogger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import static org.thesalutyt.storyverse.SVEngine.interpreter;

public class Script extends ScriptableObject implements EnvResource {
    public final Logger LOGGER = LogManager.getLogger("StoryVerse::Script");
    public final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    public static HashMap<String, SVEnvironment.ScriptEvaluator> tasks = new HashMap<>();

    @Documentate(
            desc = "Freezes working thread"
    )
    public synchronized void waitTime(Integer time) {
        try {
            wait(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notifyAll();
    }

    public synchronized void waitZero(){
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Documentate(
            desc = "Freezes working thread before entity stops moving"
    )
    public synchronized void waitWalkEnd(WalkTask task){
        BlockPos pos = task.getPos();
        MobEntity entity = task.getEntity();
        MobController controller = task.getController();
        while (entity.getNavigation().isDone()) {
            StoryVerse.LOGGER.info(
                    pos.getX()+":"+entity.getX()+"\n"+
                            pos.getY()+":"+entity.getY()+"\n"+
                            pos.getZ()+":"+entity.getZ()+"\n"
            );
            waitZero();
        }
        notifyAll();
        controller.stopMove();
    }
    public synchronized void waitWalkEnd(String taskID){
        try{
            WalkTask task = WalkTask.getTaskByID(taskID);
            BlockPos pos = task.getPos();
            MobEntity entity = task.getEntity();
            MobController controller = task.getController();
            while (entity.getNavigation().isDone()) {
                StoryVerse.LOGGER.info(
                        pos.getX()+":"+entity.getX()+"\n"+
                                pos.getY()+":"+entity.getY()+"\n"+
                                pos.getZ()+":"+entity.getZ()+"\n"
                );
                waitZero();
            }
            notifyAll();
            controller.stopMove();
        } catch (NullPointerException ex) {
            String message = String.format("§4[ERROR] §e%s:§r\n %s", SVEngine.SVError.TYPE_ERROR, ex);
            Chat.sendMessage(message);
        }
    }
    public static void runScript(String scriptName) {
        new Thread(() -> {
            interpreter.executeString(String.format("ExternalFunctions.import_file(\"%s\")", scriptName));
            String logs_path = SVELogger.init(scriptName);
            SVELogger.write(logs_path, "Ran script successfully");
        }).start();
    }

    public static void evalRun(String script) {
        new Thread(() -> {
            tasks.put(script, new SVEnvironment.ScriptEvaluator(ModEvents.inWorld).configure(script, interpreter.getScope()));
            Thread thread = tasks.get(script).getEvaluatorThread();
            tasks.get(script).start();
        }).start();
    }

    public static void waitTime(String script, Double time) {
        new Thread(() -> {
            tasks.get(script).waitTime(time.longValue());
        }).start();
    }

    public static void stop(String script) {
        new Thread(() -> {
            tasks.get(script).stop();
        }).start();
    }

    public static void log(String message) {
        String logs_path = SVELogger.init("ScriptLogs");
        SVELogger.write(logs_path, message);
        ExternalFunctions ef = new ExternalFunctions(SVEngine.SCRIPTS_PATH);
        ef.log(message);
    }
    public static void waitUntilMessage(String message) {
        Integer waitTimeAmount = 1;
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        Script ef = new Script();
        ef.setParentScope(scope);

        try {
            Method waitTime = Script.class.getMethod("waitTime", Integer.class);
            methodsToAdd.add(waitTime);
            Method waitWalkEnd = Script.class.getMethod("waitWalkEnd", String.class);
            methodsToAdd.add(waitWalkEnd);
            Method waitWalkTaskEnd = Script.class.getMethod("waitWalkEnd", String.class);
            methodsToAdd.add(waitWalkTaskEnd);
            Method waitZero = Script.class.getMethod("waitZero");
            methodsToAdd.add(waitZero);
            Method runScript = Script.class.getMethod("runScript", String.class);
            methodsToAdd.add(runScript);
            Method log = Script.class.getMethod("log", String.class);
            methodsToAdd.add(log);
            Method evalRun = Script.class.getMethod("evalRun", String.class);
            methodsToAdd.add(evalRun);
            Method stop = Script.class.getMethod("stop", String.class);
            methodsToAdd.add(stop);
            Method wait = Script.class.getMethod("waitTime", String.class, Double.class);
            methodsToAdd.add(wait);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("script", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "Script";
    }

    @Override
    public String getClassName() {
        return "Script";
    }
}