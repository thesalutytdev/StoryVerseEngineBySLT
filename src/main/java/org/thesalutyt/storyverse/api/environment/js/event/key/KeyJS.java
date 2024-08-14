package org.thesalutyt.storyverse.api.environment.js.event.key;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class KeyJS extends ScriptableObject implements EnvResource, JSResource {
    public String value;
    public Integer id;

//    @SubscribeEvent
//    public static void onKey(InputEvent.KeyInputEvent event) {
//
//    }

    @Override
    public String getClassName() {
        return "KeyJS";
    }

    @Override
    public String getResourceId() {
        return "KeyJS";
    }
}
