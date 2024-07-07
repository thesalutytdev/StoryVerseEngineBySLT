package org.thesalutyt.storyverse.api.quests;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

import javax.annotation.ParametersAreNonnullByDefault;

public class QuestDisplay extends Screen {
    public Quest quest;
    public String background = "textures/gui/choice_gui.png";
    public QuestDisplay(Quest quest) {
        super(new StringTextComponent(quest.name));
        this.quest = quest;
    }

    @Override
    public void init() {
        super.init();
        this.width = 256;
        this.height = 256;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        assert this.minecraft != null;
        this.minecraft.textureManager.bind(new ResourceLocation(StoryVerse.MOD_ID, background));
        this.blit(matrixStack, 0, 0, 0, 0, this.width, this.height);
        if (quest.entityAdder != null) {
            InventoryScreen.renderEntityInInventory(100, 100,
                    90, (float)(this.width / 2) - mouseX,
                    (float)(this.height / 2 - 50) - mouseY, quest.entityAdder);
        }

        this.font.draw(matrixStack, quest.name, 10, 10, 0xFFFFFF);
        this.font.draw(matrixStack, quest.description, 10, 20, 0xFFFFFF);

    }
}
