package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.CutsceneJS;
import org.thesalutyt.storyverse.api.environment.js.event.EventListener;
import org.thesalutyt.storyverse.api.environment.js.event.EventManagerJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.api.features.*;

import javax.swing.text.PlainDocument;


public class Interpreter {
    private Scriptable scope;
    public Interpreter (String rootDir) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.enter();
            scope = ctx.initStandardObjects();
            ExternalFunctions.putIntoScope(scope, rootDir);
            Player.putIntoScope(scope);
            AsyncJS.putIntoScope(scope, rootDir);
            Script.putIntoScope(scope);
            Asynchronous.putIntoScope(scope);
            Server.putIntoScope(scope, rootDir);
            Chat.putIntoScope(scope);
            Sounds.putIntoScope(scope);
            WorldWrapper.putIntoScope(scope);
            MobController.putIntoScope(scope);
            // EventManagerJS.putIntoScope(scope, loop);
            MobJS.putIntoScope(scope);
            EventListener.putIntoScope(scope);
            CutsceneJS.putIntoScope(scope);
            LocationCreator.putIntoScope(scope);
            ScriptProperties.putIntoScope(scope);
        });
    }
    public Scriptable getScope() {
        return scope;
    }
    public void close () {
        EventLoop.getLoopInstance().close();
    }
    public void executeString (String str) {
        EventLoop.getLoopInstance().runImmediate(() -> Context.getCurrentContext().evaluateString(
                scope,
                str,
                "<cmd>",
                1,
                null
        ));
    }
}