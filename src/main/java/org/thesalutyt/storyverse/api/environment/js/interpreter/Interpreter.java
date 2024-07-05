package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.CutsceneJS;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.api.environment.js.event.EventManagerJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.js.npc.NpcSpecials;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.api.screen.gui.script.ScriptableGui;
import org.thesalutyt.storyverse.api.special.FadeScreen;


public class Interpreter {
    private Scriptable scope;

    public Interpreter(String rootDir) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.enter();
            scope = ctx.initStandardObjects();
            ExternalFunctions.putIntoScope(scope, rootDir);
            Player.putIntoScope(scope);
            AsyncJS.putIntoScope(scope);
            Script.putIntoScope(scope);
            Asynchronous.putIntoScope(scope);
            Server.putIntoScope(scope, rootDir);
            Chat.putIntoScope(scope);
            Sounds.putIntoScope(scope);
            WorldWrapper.putIntoScope(scope);
            MobController.putIntoScope(scope);
            MobJS.putIntoScope(scope);
            CutsceneJS.putIntoScope(scope);
            LocationCreator.putIntoScope(scope);
            ScriptProperties.putIntoScope(scope);
            EventManagerJS.putIntoScope(scope);
            ScriptableGui.putIntoScope(scope);
            FadeScreen.putIntoScope(scope);
            NpcSpecials.putIntoScope(scope);
            JSItem.putIntoScope(scope);
            BackgroundScript.putIntoScope(scope);
        });
    }
    public Scriptable getScope() {
        return scope;
    }

    public void close() {
        EventLoop.closeLoopInstance();
    }

    public void executeString(String str) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context.getCurrentContext().evaluateString(
                    scope,
                    str,
                    "<cmd>",
                    1,
                    null
            );
        });
    }

    public void executeString(String str, String sourceName) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context.getCurrentContext().evaluateString(
                    scope,
                    str,
                    sourceName,
                    1,
                    null
            );
        });
    }
}