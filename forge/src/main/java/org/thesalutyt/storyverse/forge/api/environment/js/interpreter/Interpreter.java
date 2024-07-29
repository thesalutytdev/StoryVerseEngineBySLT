package org.thesalutyt.storyverse.forge.api.environment.js.interpreter;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.forge.api.environment.js.MobJS;
import org.thesalutyt.storyverse.forge.api.environment.js.resource.minecraft.block.JSBlock;
import org.thesalutyt.storyverse.forge.api.environment.js.resource.minecraft.item.JSItem;
import org.thesalutyt.storyverse.forge.api.features.managment.MobController;
import org.thesalutyt.storyverse.forge.api.features.managment.Server;
import org.thesalutyt.storyverse.forge.api.features.world.Chat;
import org.thesalutyt.storyverse.forge.api.features.world.WorldWrapper;

public class Interpreter {
    private Scriptable scope;

    public Interpreter(String rootDir) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.enter();
            scope = ctx.initStandardObjects();
            ExternalFunctions.putIntoScope(scope, rootDir);
//            Player.putIntoScope(scope);
//            AsyncJS.putIntoScope(scope);
//            Script.putIntoScope(scope);
            Asynchronous.putIntoScope(scope);
            Server.putIntoScope(scope, rootDir);
            Chat.putIntoScope(scope);
//            Sounds.putIntoScope(scope);
            WorldWrapper.putIntoScope(scope);
            MobController.putIntoScope(scope);
            MobJS.putIntoScope(scope);
//            EntityCutsceneJS.putIntoScope(scope);
//            LocationCreator.putIntoScope(scope);
//            ScriptProperties.putIntoScope(scope);
//            EventManagerJS.putIntoScope(scope);
//            ScriptableGui.putIntoScope(scope);
//            FadeScreen.putIntoScope(scope);
//            NpcSpecials.putIntoScope(scope);
            JSItem.putIntoScope(scope);
//            BackgroundScript.putIntoScope(scope);
//            SpecialListener.putIntoScope(scope);
//            Timer.putIntoScope(scope);
//            MathScript.putIntoScope(scope);
//            Trader.putIntoScope(scope);
//            ThreaderJS.putIntoScope(scope);
//            Delayed.putIntoScope(scope);
//            IGoalBuilder.putIntoScope(scope);
//            Quest.putIntoScope(scope);
//            ItemUtils.putIntoScope(scope);
//            Locker.putIntoScope(scope);
//            SpecialListener.putIntoScope(scope);
//            Action.putIntoScope(scope);
//            Reputation.putIntoScope(scope);
//            Random.putIntoScope(scope);
//            File.putIntoScope(scope);
//            CutsceneJS.putIntoScope(scope);
//            MovingJS.putIntoScope(scope);
//            Time.ITime.putIntoScope(scope);
            JSBlock.putIntoScope(scope);
//            RootJS.putIntoScope(scope);
//            CameraMoveSceneJS.putIntoScope(scope);
//            WaitConditionJS.putIntoScope(scope);
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