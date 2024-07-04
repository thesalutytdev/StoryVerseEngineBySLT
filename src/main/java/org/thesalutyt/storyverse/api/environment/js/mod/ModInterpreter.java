package org.thesalutyt.storyverse.api.environment.js.mod;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.CutsceneJS;
import org.thesalutyt.storyverse.api.environment.js.event.EventManagerJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Asynchronous;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.js.npc.NpcSpecials;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.api.screen.gui.script.ScriptableGui;
import org.thesalutyt.storyverse.api.special.FadeScreen;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;
import org.thesalutyt.storyverse.common.tabs.adder.CustomTab;

public class ModInterpreter {
    private Scriptable scope;
    public ModInterpreter(String rootDir) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.enter();
            scope = ctx.initStandardObjects();
            ExternalFunctions.putIntoScope(scope, rootDir);
            AsyncJS.putIntoScope(scope, rootDir);
            Asynchronous.putIntoScope(scope);
            CustomTab.putIntoScope(scope);
            CustomItem.putIntoScope(scope);
            Analyze.putIntoScope(scope);
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
