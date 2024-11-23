package org.thesalutyt.storyverse.common.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class DefaultBinds {
    public static KeyBinding keyStory;
    public static KeyBinding startScript;
    public static ArrayList<KeyBinding> keyList = new ArrayList<>();
    public static HashMap<Integer, ArrayList<BaseFunction>> keyMap = new HashMap<>();
    public static HashMap<Integer, KeyBinding> keyBinds = new HashMap<>();

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        startScript = new KeyBinding("key.storyverse.start_script",
                InputMappings.Type.KEYSYM,
                SVEngine.KEY_START_CODE,
                "keylist.storyverse"
        );

        keyList.add(startScript);
        keyList.forEach(ClientRegistry::registerKeyBinding);
    }

    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent event) {
        for (KeyBinding bind : keyList) {
            if (bind.isDown()) {
                EventLoop.getLoopInstance().runImmediate(() -> {
                    for (BaseFunction f : keyMap.get(bind.getKey().getValue())) {
                        f.call(Context.getCurrentContext(),
                                SVEngine.modInterpreter.getScope(),
                                SVEngine.modInterpreter.getScope(), new Object[]{bind.getKey()});
                    }
                });

            }
        }
    }
}
