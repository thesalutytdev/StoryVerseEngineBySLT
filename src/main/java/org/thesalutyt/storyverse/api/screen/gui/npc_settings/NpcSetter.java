package org.thesalutyt.storyverse.api.screen.gui.npc_settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.screen.gui.elements.TextArea;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiButton;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class NpcSetter extends Screen {
    public NPCEntity npc;

    public NpcSetter(NPCEntity npc) {
        super(new StringTextComponent(" "));
        this.npc = npc;
    }

    @Override
    public void init() {
        super.init();
        this.width = 512;
        this.height = 256;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        assert this.minecraft != null;
        GuiButton remove = new GuiButton("remove",
                "textures/gui/buttons/button_0.png",
                100,
                100,
                128.0, 64.0, "text.storyverse.remove", () -> {
            this.npc.remove(false);
            this.minecraft.setScreen(null);
        });
        GuiButton save = new GuiButton("save", "textures/gui/buttons/button_0.png",
                300, 100, 128.0, 64.0, "text.storyverse.save", () -> {
            this.minecraft.setScreen(null);
        });
        GuiButton setFemale = new GuiButton("set_female", "textures/gui/buttons/button_0.png",
                100, 200, 128.0, 64.0, "text.storyverse.set_female", () -> {
                    this.npc.setModelPath("geo/woman.geo.json");
                    this.npc.setTexturePath("textures/entity/npc/eva.png");
                    this.npc.setAnimationPath("animations/npc.animation.json");
                    this.minecraft.setScreen(null);
                });
        this.addButton(remove);
        this.addButton(save);
        this.addButton(setFemale);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
