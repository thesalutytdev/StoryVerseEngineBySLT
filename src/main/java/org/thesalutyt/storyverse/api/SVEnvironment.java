package org.thesalutyt.storyverse.api;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.camera.cutscene.Cutscene;
import org.thesalutyt.storyverse.api.camera.cutscene.Moving;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.action.Action;
import org.thesalutyt.storyverse.api.environment.js.waiter.WaitCondition;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.features.WorldWrapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public class SVEnvironment{
    public static String envId = "storyverse";
    public static String version = "1";
    public Script script = new Script();
    public WorldWrapper world;
    public SVEnvironment() {
    }
    public static class Root {
        public static String dataSavesPath = SVEngine.getCurrentWorldDir() + "sve_data/";
        public static Integer ticks = 0;
        public static Boolean isDay;
        public static Boolean started = false;
        public static Boolean inCutscene = false;
        public static Boolean inMoving = false;
        public static void tick() {
            ticks++;
            if (inCutscene) {
                Cutscene.tick(ticks);
            }
            if (inMoving) {
                Moving.tick(ticks);
            }
            if (Action.onEveryTick.isEmpty()) {
                return;
            }
            WaitCondition.tick(ticks);
            Action.runOnTick(ticks);
        }
        public static void resetTick() {
            ticks = 0;
        }
        public static void setTicks(Integer new_ticks_amount) {
            ticks = new_ticks_amount;
        }
        public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
            if (ScriptProperties.worldStarterScript == null || started || ScriptProperties.ran) {
                return;
            }
            started = true;
            ScriptProperties.run();
            MobJS.restoreAllNPC();
        }
        public static void playerLeft(WorldEvent.Unload event) {
            SVEnvironment.Root.resetTick();
            MobJS.killAllNPC();
        }
        public static Entity getCameraEntity() {
            return Minecraft.getInstance().cameraEntity;
        }
    }

    public static class ScriptEvaluator extends ScriptableObject {
        public static HashMap<UUID, ScriptEvaluator> evaluators = new HashMap<>();
        private boolean inWorld = false;
        private Thread evaluatorThread;
        private UUID id;
        private boolean evaluating = false;
        public ScriptEvaluator(boolean inWorld) {
            this.inWorld = inWorld;
            this.id = UUID.randomUUID();
            this.evaluating = false;

            evaluators.put(id, this);
        }

        public static ScriptEvaluator getInstance() {
            return new ScriptEvaluator(true);
        }

        public ScriptEvaluator getEvaluator() {
            return this;
        }

        public ScriptEvaluator configure(String script, Scriptable scope) {
                if (evaluating) {
                    throw new IllegalStateException("Already evaluating");
                }
                if (!inWorld) {
                    throw new IllegalStateException("Not in world");
                }

                this.evaluatorThread = new Thread(() -> {
                    Path fullPath = Paths.get(SVEngine.SCRIPTS_PATH + "/" + script).toAbsolutePath();
                    if (fullPath.startsWith(SVEngine.SCRIPTS_PATH)) {
                        try {
                            InputStreamReader reader = new InputStreamReader(new FileInputStream(fullPath.toString()), StandardCharsets.UTF_8);
                            System.out.println("Running script: " + fullPath.toString());
                            Context ctx = Context.getCurrentContext();
                            System.out.println("Got context");
                            System.out.println("Scope: " + scope);
                            ctx.evaluateReader(
                                    scope,
                                    reader,
                                    fullPath.toString(),
                                    1,
                                    null
                            );
                            System.out.println("Done running script: " + fullPath.toString());
                        } catch (final FileNotFoundException e) {
                            System.out.println("Invalid path (" + script + "): file not found");
                            Chat.sendError("Invalid path (" + script + "): file not found");
                        } catch (final IOException e) {
                            System.out.println("Invalid path (" + script + "): IOException " + e);
                            Chat.sendError("Invalid path (" + script + "): IOException " + e);

                        } catch (final RhinoException e) {
                            System.out.println("Script error: " + e);
                            Chat.sendError("Script error: " + e);
                        } catch (final Exception e) {
                            System.out.println("Java exception: " + e.getMessage());
                            Chat.sendError("Java exception: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Invalid path (" + script + "): path ends outside root script directory");
                    }
                });
                this.evaluatorThread.setName(configureName(script));
            return this;
        }

        public ScriptEvaluator waitTime(long timeout) {
            try {
                this.evaluatorThread.wait(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return this;
        }

        public ScriptEvaluator start() {
            this.evaluatorThread.start();
            this.evaluating = true;
            return this;
        }

        public ScriptEvaluator stop() {
            this.evaluating = false;
            this.evaluatorThread.interrupt();
            return this;
        }

        public ScriptEvaluator hardStop() {
            this.evaluating = false;
            this.evaluatorThread.stop();
            return this;
        }

        public void close() {
            if (this.evaluating) {
                this.evaluatorThread.interrupt();
            }
        }

        public Thread getEvaluatorThread() {
            return this.evaluatorThread;
        }

        public boolean isEvaluating() {
            return this.evaluating;
        }

        public UUID getUUID() {
            return this.id;
        }

        private String configureName(String script) {
            return String.format("[SCRIPT_EVALUATOR_%s] %s", this.id, script);
        }

        @Override
        public String toString() {
            return String.format("ScriptEvaluator{" +
                    "environmentVersion=%s," +
                    "id=%s," +
                    "threadName=%s}", SVEnvironment.version, this.id, this.evaluatorThread.getName());
        }

        @Override
        public String getClassName() {
            return "ScriptEvaluator";
        }
    }
}
