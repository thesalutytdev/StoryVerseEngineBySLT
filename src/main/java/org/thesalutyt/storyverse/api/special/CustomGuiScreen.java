package org.thesalutyt.storyverse.api.special;// Import necessary classes
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.api.features.Chat;

// Your custom GUI screen class
public class CustomGuiScreen extends Screen {

    // Constructor for your custom GUI screen
    public CustomGuiScreen() {
        // Call the constructor of the parent class (Screen) with the title of the GUI
        super(new StringTextComponent("Custom GUI"));
    }

    // Initialize GUI components (buttons, text fields, etc.)
    @Override
    protected void init() {
        super.init();

        // Calculate the center of the screen for button placement
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Add a button to the screen
        // Parameters: (id, x, y, width, height, message, onPress)
        this.addButton(new Button(centerX - 50, centerY - 20, 100, 20, new StringTextComponent("Button 1"), button -> {
            // Action when Button 1 is pressed
            // For example, send a chat message
            Chat.sendAsEngine("button 1 pressed");
        }));

        // Add another button to the screen
        this.addButton(new Button(centerX - 50, centerY + 10, 100, 20, new StringTextComponent("Button 2"), button -> {
            // Action when Button 2 is pressed
            // For example, send a chat message
            Chat.sendAsEngine("button 2 pressed");
        }));
    }

    // Render the GUI
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Render the background (darken the background)
        this.renderBackground(matrixStack);

        // Draw the title in the center of the screen
        drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, 15, 0xFFFFFF);

        // Render all the components (buttons, text fields, etc.)
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    // Override the method to prevent the game from pausing when the GUI is open
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}