package org.thesalutyt.storyverse.api.screen.gui.npc_settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.screen.gui.elements.CircleRect;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiButton;
import org.thesalutyt.storyverse.api.screen.gui.render.RenderUtils;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class NpcModel extends Screen {
    public NPCEntity npc;
    public String npcModel;
    public String npcTexture;
    public ArrayList<CircleRect> circles = new ArrayList<>();
    public ArrayList<GuiButton> modelButtons = new ArrayList<>();
    public boolean isModelListOpen = false;
    public ArrayList<GuiButton> dropdownItems = new ArrayList<>();
    private int scrollOffset = 0;
    public static final int VISIBLE_ITEMS = 3;

    public NpcModel(NPCEntity npc) {
        super(new StringTextComponent(" "));
        this.npc = npc;
    }

    @Override
    public void init() {
        super.init();
        this.width = 512;
        this.height = 256;
        GuiButton back = new GuiButton("back", "textures/gui/buttons/button_0.png",
                300, 100, 128.0, 64.0, "text.storyverse.back", () -> {
            NpcSetter ns = new NpcSetter(npc);
            assert this.minecraft != null;
            this.minecraft.setScreen(ns);
        });
        this.addButton(back);
        GuiButton modelListOpener = new GuiButton("model_list", "textures/gui/buttons/button_0.png",
                100, 100, 128.0, 64.0, "text.storyverse.model_list", () -> {
            this.isModelListOpen = !this.isModelListOpen;
        });
        this.addButton(modelListOpener);
        try {
            System.out.println("Loading models...");
            File[] geo = getFiles(SVEngine.MODELS_PATH);
            assert geo != null;
            System.out.println(Arrays.toString(Arrays.stream(geo).toArray()));
            System.out.println(SVEngine.MODELS_PATH);
            System.out.println("Models loaded");
            System.out.println("Adding models...");
            int currY = 200;
            for (File f : geo) {
                Chat.sendAsEngine(f.getName());
                if (f.getName().endsWith(".geo.json")) {
                    System.out.println("Added model: " + f.getName());
                    GuiButton btn = new GuiButton(f.getName(), "textures/gui/buttons/button_0.png",
                            100, currY, 128.0, 64.0, f.getName(), () -> {
                        System.out.println("Selected model: " + f.getName());
                        this.npc.setModelPath(SVEngine.MODELS_PATH + f.getName());
                        System.out.println("Model path: " + this.npc.getModelPath());
                    });
                    modelButtons.add(btn);
                    dropdownItems.add(btn);
                    System.out.println("Added button: " + f.getName());
                    currY += 100;
                }
            }
        } catch (Exception e) {
            System.out.printf("On line: %s\nWith message: %s%n", e.getCause(), e.getMessage());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        assert this.minecraft != null;
        if (npc == null) {
            return;
        }
        if (!circles.isEmpty()) {
            for (CircleRect rect : circles) {
                RenderUtils.drawCircleRect(rect.x, rect.y, rect.x1, rect.y1,
                        rect.radius, rect.color);
            }
        }
        InventoryScreen.renderEntityInInventory(512, 300, 90,
                (float) (this.width / 2) - mouseX,
                (float) (this.height / 2 - 50) - mouseY, npc);
        // if (!modelButtons.isEmpty()) {
        //     for (GuiButton button : modelButtons) {
        //         button.render(stack, mouseX, mouseY, partialTicks);
        //         this.addButton(button);
        //     }
        // }
        if (isModelListOpen) {
            int x = this.width / 2 - 50;
            int y = this.height / 2 + 15;
            int itemHeight = 20;

            for (int i = 0; i < VISIBLE_ITEMS; i++) {
                int index = i + scrollOffset;
                if (index >= dropdownItems.size()) break;
                this.addButton(dropdownItems.get(index));
            }
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    public File[] getFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        File[] ret_list;
        int count = 0;
        if (files != null) {
            for (File i : files) {
                if (i.isFile()) {
                    count++;
                } else {
                    continue;
                }
            }
            ret_list = new File[count];
            count = 0;
            for (File i : files) {
                if (i.isFile()) {
                    ret_list[count] = i;
                    count++;
                } else {
                    continue;
                }
            }
            return ret_list;
        }
        return null;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isModelListOpen) {
            scrollOffset -= (int) delta;
            scrollOffset = Math.max(0, Math.min(scrollOffset, dropdownItems.size() - VISIBLE_ITEMS));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
