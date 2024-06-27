package org.thesalutyt.storyverse.api.screen.gui.elements;

import net.minecraft.util.text.StringTextComponent;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import net.minecraft.client.gui.widget.button.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiButton extends GuiWidget {
    public String message;
    public ArrayList<BaseFunction> onClick = new ArrayList<>();
    public static HashMap<String, GuiButton> btns = new HashMap<>();
    public Button button;
    public GuiButton(Double x, Double y, Double width, Double height, String message, BaseFunction function) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
        EventLoop.getLoopInstance().runImmediate(() -> {
            onClick.add(function);
        });
        this.button = new Button(x.intValue(), y.intValue(), width.intValue(), height.intValue(),
                new StringTextComponent(message), (button) -> {
            System.out.println("Button clicked: " + message);
            onClick();
        });
        btns.put(message, this);
    }
    public void onClick() {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.getCurrentContext();
            for (int i=0;i<onClick.size(); i++) {
                onClick.get(i).call(ctx, SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{});
            }
        });
    }
}
