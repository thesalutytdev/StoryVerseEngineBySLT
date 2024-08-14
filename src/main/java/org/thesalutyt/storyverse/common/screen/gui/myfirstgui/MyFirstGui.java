package org.thesalutyt.storyverse.common.screen.gui.myfirstgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.api.features.Chat;

public class MyFirstGui extends Screen {
    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ITextComponent TITLE = new TranslationTextComponent("gui.advancements");
    protected void init() {
        int guiLeft = (this.width - 252) / 2;
        int guiTop = (this.height - 140) / 2;
        addButton(new Button(
                guiLeft, guiTop - 50, 20, 20, new StringTextComponent("<>"), b -> {
                Chat.sendAsEngine("OMG BTN PRESSED");}));
    }
    protected MyFirstGui(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
    }
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;
        this.renderBackground(p_230430_1_);
    }
    public static void open() {
        MyFirstGui myFirstGui = new MyFirstGui(TITLE);
        myFirstGui.init();
        assert myFirstGui.minecraft != null;
        myFirstGui.minecraft.screen = myFirstGui;
    }
}
