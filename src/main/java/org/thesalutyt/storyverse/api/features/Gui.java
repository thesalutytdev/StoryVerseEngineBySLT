package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.LivingEntity;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.gui.GuiButton;

import java.util.ArrayList;

public class Gui extends ScriptableObject implements EnvResource, JSResource {

    private static LivingEntity displayEntity;
    private static String label;
    private static String message;
    private static ArrayList<GuiButton> buttons = new ArrayList<>();
    public void create_gui(String mobId, String label, String message, NativeArray buttonIds) {
        displayEntity = MobJS.getMob(mobId).getMobEntity();
        label = label;
        message = message;
        buttons = buttons;
        for (int i = 0; i < buttonIds.getLength(); i++) {
            buttons.add(GuiButton.btns.get(buttonIds.get(i).toString()));
        }
    }
    @Override
    public String getClassName() {
        return "GuiUtils";
    }

    @Override
    public String getResourceId() {
        return "GuiUtils";
    }
}
