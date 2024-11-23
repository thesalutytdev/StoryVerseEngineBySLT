package org.thesalutyt.storyverse.api.compatibility.imgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import imgui.ImGui;
import imgui.ImguiKt;
import imgui.classes.Context;
import imgui.classes.IO;
import imgui.impl.gl.ImplGL3;
import imgui.impl.glfw.ImplGlfw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL;
import uno.glfw.GlfwWindow;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

@OnlyIn(Dist.CLIENT)
public class SLTScreen extends Screen {
    protected static ImGui imGui = ImGui.INSTANCE;

    protected static ImGui imgui = ImGui.INSTANCE;

    protected static ImplGL3 implGl3;
    protected static ImplGlfw implGlfw;
    protected static IO io;
    protected static Context ctx;

    static {
        ImguiKt.MINECRAFT_BEHAVIORS = true;
        GlfwWindow window = new GlfwWindow(Minecraft.getInstance().getWindow().getWindow());
        GL.createCapabilities();
        glfwMakeContextCurrent(window.getHandle());
        // window.context();
        ctx = new Context();
        implGlfw = new ImplGlfw(window, false, null);
        implGl3 = new ImplGL3();

        io = imgui.getIo();
        imgui.getStyle().scaleAllSizes((float) Minecraft.getInstance().getWindow().getGuiScale());
    }

    public SLTScreen() {
        super(new StringTextComponent("ImGui"));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        implGl3.newFrame();
        imgui.newFrame();

        //Render things here

        imgui.text("Hello Minecraft!");

        imgui.render();
        implGl3.renderDrawData(Objects.requireNonNull(imgui.getDrawData()));
    }
}
