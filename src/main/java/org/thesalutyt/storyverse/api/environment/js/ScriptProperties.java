package org.thesalutyt.storyverse.api.environment.js;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.action.ActionPacket;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;

import javax.swing.text.html.parser.Entity;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class ScriptProperties extends ScriptableObject implements EnvResource, JSResource {
    public static String worldStarterScript;
    public static void onWorldStart(String script_name, Boolean is) {
        if (!is) {
            return;
        } else {
            worldStarterScript = script_name;
        }
    }
    public static void resetWorldStart() {
        worldStarterScript = null;
    }
    @SubscribeEvent
    public static void onJoined (EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof PlayerEntity) {
            SVEngine.interpreter = new Interpreter(SVEngine.SCRIPTS_PATH);
            System.out.println("[ModEvents::onWorldJoin] Created new interpreter");
            System.out.println(Minecraft.getInstance().cameraEntity);
            if (worldStarterScript == null) {
                return;
            } else {
                Script.runScript(worldStarterScript);
            }
        } else {
            return;
        }
    }
    @Override
    public String getClassName() {
        return "ScriptProperties";
    }

    @Override
    public String getResourceId() {
        return "ScriptProperties";
    }
}
