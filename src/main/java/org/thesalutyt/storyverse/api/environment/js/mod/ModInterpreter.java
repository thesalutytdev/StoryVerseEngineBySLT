package org.thesalutyt.storyverse.api.environment.js.mod;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Asynchronous;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.js.thread.Delayed;
import org.thesalutyt.storyverse.api.environment.js.thread.ThreaderJS;
import org.thesalutyt.storyverse.api.features.BackgroundScript;
import org.thesalutyt.storyverse.api.features.Sounds;
import org.thesalutyt.storyverse.common.block.adder.CustomBlock;
import org.thesalutyt.storyverse.common.effects.adder.EffectAdder;
import org.thesalutyt.storyverse.common.events.adder.SpecialListener;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;
import org.thesalutyt.storyverse.common.tabs.adder.CustomTab;

public class ModInterpreter {
    private Scriptable scope;
    public ModInterpreter(String rootDir) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.enter();
            scope = ctx.initStandardObjects();
            ExternalFunctions.putIntoScope(scope, rootDir);
            AsyncJS.putIntoScope(scope);
            Asynchronous.putIntoScope(scope);
            CustomTab.putIntoScope(scope);
            CustomItem.putIntoScope(scope);
            Analyze.putIntoScope(scope);
            CustomBlock.putIntoScope(scope);
            LocationCreator.putIntoScope(scope);
            SpecialListener.putIntoScope(scope);
            ScriptProperties.putIntoScope(scope);
            ThreaderJS.putIntoScope(scope);
            BackgroundScript.putIntoScope(scope);
            Delayed.putIntoScope(scope);
            EffectAdder.putIntoScope(scope);
            Sounds.putIntoScope(scope);
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
