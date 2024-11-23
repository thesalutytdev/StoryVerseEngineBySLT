package org.thesalutyt.storyverse.common.keybinds.adder;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.elements.ICustomElement;
import org.thesalutyt.storyverse.common.keybinds.DefaultBinds;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CustomBind extends ScriptableObject implements ICustomElement, EnvResource, JSResource {
    public static void create(String name, Integer keyCode, String group, String type, BaseFunction onPress) {
        String __group;

        if (group.equals("keylist.storyverse")) {
            __group = "keylist.storyverse_mod_sc";
        } else {
            __group = group;
        }
        EventLoop.getLoopInstance().runImmediate(() -> {
            KeyBinding bind = new KeyBinding(name, getType(type), keyCode, __group);
            DefaultBinds.keyList.add(bind);
            ArrayList<BaseFunction> list = new ArrayList<>();
            list.add(onPress);
            DefaultBinds.keyMap.put(keyCode, list);
            DefaultBinds.keyBinds.put(keyCode, bind);
            ClientRegistry.registerKeyBinding(bind);
        });
    }

    public static InputMappings.Type getType(String type) {
        switch (type.toLowerCase()) {
            case "scan":
                return InputMappings.Type.SCANCODE;
            case "mouse":
                return InputMappings.Type.MOUSE;
            case "keyboard":
            default:
                return InputMappings.Type.KEYSYM;
        }
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        CustomBind cs = new CustomBind();

        cs.setParentScope(scope);

        try {
            Method create = CustomBind.class.getMethod("create",
                    String.class, Integer.class,
                    String.class, String.class, BaseFunction.class);
            methodsToAdd.add(create);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, cs);
            cs.put(m.getName(), cs, methodInstance);
        }
        scope.put("bind", scope, cs);
    }

    @Override
    public Object getDefaultElement() {
        return KeyBinding.class;
    }

    @Override
    public String getClassName() {
        return "CustomBind";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "CustomBind";
    }
}
