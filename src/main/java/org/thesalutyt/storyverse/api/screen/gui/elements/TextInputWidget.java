package org.thesalutyt.storyverse.api.screen.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class TextInputWidget extends Widget {
    private final int x, y, width, height;
    private int textColor = 0xFF000000;
    private int borderColor = 0xFFA0A0A0;
    private int backgroundColor = 0xFFD4D4D4;
    private int cursorColor = 0xFF00FF00;
    private int placeholderColor = 0xFFA0A0A0;
    private int cursorWidth = 2;
    private String text = "";
    private float cursorPosition = 0;
    private int multiplier = 6;
    private boolean cursorTick = true;
    private int ticks = 0;
    private boolean widgetActive = false;
    private String placeholder = "Enter text here";

    public TextInputWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent("Text Input"));
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public TextInputWidget(int x, int y, int width, int height, String text) {
        this(x, y, width, height);
        this.text = text;
    }


    public void setPlaceholderColor(int color) {
        this.placeholderColor = color;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    public void setCursorColor(int color) {
        this.cursorColor = color;
    }

    public void setCursorWidth(int width) {
        this.cursorWidth = width;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.widgetActive = true;
        fill(matrices, x, y, x + width, y + height, backgroundColor);

        Minecraft.getInstance().font.draw(matrices, text, x + 4, y + 6, textColor);

        if (text.isEmpty() && !placeholder.isEmpty()) {
            Minecraft.getInstance().font.draw(matrices, placeholder, x + 4, y + 6, placeholderColor);
        }

        drawCursor(matrices);
        updateCursorPosition();
        cursorTick();

        this.hLine(matrices, x, y, x + width, borderColor);
    }

    public void tick() {
        ticks++;
        cursorTick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            setFocused(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (isFocused()) {
            if(Screen.isPaste(scanCode)) {
                String clipboardText = Minecraft.getInstance().keyboardHandler.getClipboard();
                for (char c : clipboardText.toCharArray()) {
                    charTyped(c, scanCode);
                    return true;
                }
            }
            if (key == 259) { // Backspace
                if (!text.isEmpty()) {
                    if (Screen.hasControlDown()) {
                        String[] words = text.split(" ");
                        if (words.length > 1) {
                            text = words[0] + " " + words[1];
                        } else {
                            text = "";
                        }
                    } else {
                        text = text.substring(0, text.length() - 1);
                    }
                    updateCursorPosition();
                }
            } else if (key == 257) { // Enter
                setFocused(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int scanCode) {
        if (isFocused()) {
            if (scanCode == 259) { // Backspace
                if (!text.isEmpty()) {
                    if (Screen.hasControlDown()) {
                        String[] words = text.split(" ");
                        if (words.length > 1) {
                            text = words[0] + " " + words[1];
                        } else {
                            text = "";
                        }
                    } else {
                        text = text.substring(0, text.length() - 1);
                    }
                    updateCursorPosition();
                }
            } else if (scanCode == 257) { // Enter
                setFocused(false);
            } else {
                if (text.length() >= width / multiplier) {
                    updateCursorPosition();
                    return true;
                }
                text += chr;
                updateCursorPosition();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    protected void updateCursorPosition() {
        if (text.contains(" ")) {
            cursorPosition = (float) (text.length() - 1.5);
            return;
        }
        cursorPosition = text.length();
    }

    protected void drawCursor(MatrixStack matrices) {
        if (!isFocused()) return;

        if (cursorTick) {
            fill(matrices,
                    (int) (x + 4 + cursorPosition * 6), y + 6,
                    (int) (x + 5 + cursorPosition * 6), y + 14,
                    cursorColor);
        }
    }

    protected void cursorTick() {
        // cursorTick = !cursorTick;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateCursorPosition();
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        updateCursorPosition();
    }

    public void removed() {
        setFocused(false);
        this.widgetActive = false;
    }

    public float getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void clear() {
        text = "";
        cursorPosition = 0;
    }
}