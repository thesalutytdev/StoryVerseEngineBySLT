package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.json.JSON;
import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.action.Action;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.EntityCamera;
import org.thesalutyt.storyverse.api.environment.js.cutscene.nonTick.CameraMoveSceneJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.tick.CutsceneJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.tick.EntityCutsceneJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.tick.MovingJS;
import org.thesalutyt.storyverse.api.environment.js.event.EventManagerJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.block.JSBlock;
import org.thesalutyt.storyverse.api.environment.js.minecraft.block.Locker;
import org.thesalutyt.storyverse.api.environment.js.minecraft.enchantment.JSEnchant;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.js.npc.NpcSpecials;
import org.thesalutyt.storyverse.api.environment.js.npc.Trader;
import org.thesalutyt.storyverse.api.environment.js.thread.Delayed;
import org.thesalutyt.storyverse.api.environment.js.thread.ThreaderJS;
import org.thesalutyt.storyverse.api.environment.js.waiter.WaitConditionJS;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.api.quests.Quest;
import org.thesalutyt.storyverse.api.quests.goal.IGoalBuilder;
import org.thesalutyt.storyverse.api.quests.item.ItemQuest;
import org.thesalutyt.storyverse.api.screen.gui.overlay.ScriptableOverlay;
import org.thesalutyt.storyverse.api.screen.gui.script.ScriptableGui;
import org.thesalutyt.storyverse.api.special.FadeScreen;
import org.thesalutyt.storyverse.api.special.character.Reputation;
import org.thesalutyt.storyverse.common.events.adder.SpecialListener;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.PacketJS;
import org.thesalutyt.storyverse.utils.ItemUtils;


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
            EntityCutsceneJS.putIntoScope(scope);
            LocationCreator.putIntoScope(scope);
            ScriptProperties.putIntoScope(scope);
            EventManagerJS.putIntoScope(scope);
            ScriptableGui.putIntoScope(scope);
            FadeScreen.putIntoScope(scope);
            NpcSpecials.putIntoScope(scope);
            JSItem.putIntoScope(scope);
            BackgroundScript.putIntoScope(scope);
            SpecialListener.putIntoScope(scope);
            Timer.putIntoScope(scope);
            MathScript.putIntoScope(scope);
            Trader.putIntoScope(scope);
            ThreaderJS.putIntoScope(scope);
            Delayed.putIntoScope(scope);
            IGoalBuilder.putIntoScope(scope);
            Quest.putIntoScope(scope);
            ItemUtils.putIntoScope(scope);
            Locker.putIntoScope(scope);
            SpecialListener.putIntoScope(scope);
            Action.putIntoScope(scope);
            Reputation.putIntoScope(scope);
            Random.putIntoScope(scope);
            File.putIntoScope(scope);
            CutsceneJS.putIntoScope(scope);
            MovingJS.putIntoScope(scope);
            Time.ITime.putIntoScope(scope);
            JSBlock.putIntoScope(scope);
            RootJS.putIntoScope(scope);
            CameraMoveSceneJS.putIntoScope(scope);
            WaitConditionJS.putIntoScope(scope);
            ItemQuest.putIntoScope(scope);
            EntityCamera.putIntoScope(scope);
            JSON.putIntoScope(scope);
            PacketJS.putIntoScope(scope);
            JSEnchant.putIntoScope(scope);
            ScriptableOverlay.putIntoScope(scope);
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