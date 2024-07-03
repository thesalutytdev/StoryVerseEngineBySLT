package org.thesalutyt.storyverse.api.special;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Gui;
import software.bernie.geckolib3.GeckoLib;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FadeScreen extends ScriptableObject implements EnvResource, JSResource {
    public static void fade(String mode, String color) {
        // logic
        // hide/not hide mc stuff based on mode
    }

    public static void fade(String color) {
        // logic
        // do not hide mc stuff
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        FadeScreen ef = new FadeScreen();
        ef.setParentScope(scope);
        try {
            Method fade = FadeScreen.class.getMethod("fade", String.class, String.class);
            methodsToAdd.add(fade);
            Method fade2 = FadeScreen.class.getMethod("fade", String.class);
            methodsToAdd.add(fade2);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("fade", scope, ef);
    }

    @Override
    public String getClassName() {
        return "FadeScreen";
    }

    @Override
    public String getResourceId() {
        return "FadeScreen";
    }
}
