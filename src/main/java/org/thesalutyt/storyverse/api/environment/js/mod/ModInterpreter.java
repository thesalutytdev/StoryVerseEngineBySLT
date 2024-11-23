package org.thesalutyt.storyverse.api.environment.js.mod;

import net.minecraftforge.fml.loading.FMLEnvironment;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.json.JSON;
import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.ScriptProperties;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Asynchronous;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.js.thread.Delayed;
import org.thesalutyt.storyverse.api.environment.js.thread.ThreaderJS;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.api.screen.gui.overlay.ScriptableOverlay;
import org.thesalutyt.storyverse.api.screen.gui.script.ScriptableGui;
import org.thesalutyt.storyverse.common.block.adder.CustomBlock;
import org.thesalutyt.storyverse.common.commands.adder.CommandAdder;
import org.thesalutyt.storyverse.common.dimension.Dimensions;
import org.thesalutyt.storyverse.common.echantments.adder.EnchantAdder;
import org.thesalutyt.storyverse.common.effects.adder.EffectAdder;
import org.thesalutyt.storyverse.common.events.adder.SpecialListener;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;
import org.thesalutyt.storyverse.common.items.adder.armor.CustomArmorMaterial;
import org.thesalutyt.storyverse.common.keybinds.adder.CustomBind;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.PacketJS;
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
            Sounds.putIntoScope(scope);
            CustomArmorMaterial.putIntoScope(scope);
            Script.putIntoScope(scope);
            CommandAdder.putIntoScope(scope);
            Dimensions.putIntoScope(scope);
            JSON.putIntoScope(scope);
            File.putIntoScope(scope);
            Random.putIntoScope(scope);
            PacketJS.putIntoScope(scope);
            EnchantAdder.putIntoScope(scope);
            Time.ITime.putIntoScope(scope);
            MobController.putIntoScope(scope);
            if (FMLEnvironment.dist.isClient()) {
                EffectAdder.putIntoScope(scope);
                CustomBind.putIntoScope(scope);
                ScriptableGui.putIntoScope(scope);
                ScriptableOverlay.putIntoScope(scope);
            }
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
