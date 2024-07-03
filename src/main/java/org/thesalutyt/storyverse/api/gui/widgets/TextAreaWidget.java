package org.thesalutyt.storyverse.api.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.LocationCreator;
import org.thesalutyt.storyverse.api.environment.js.async.AsyncJS;
import org.thesalutyt.storyverse.api.environment.js.cutscene.CutsceneJS;
import org.thesalutyt.storyverse.api.environment.js.event.EventManagerJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Asynchronous;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.api.special.FadeScreen;
import org.thesalutyt.storyverse.utils.RenderUtils;
import org.thesalutyt.storyverse.utils.TimeHelper;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAreaWidget extends Widget {
    private final FontRenderer fontRenderer;
    public final List<String> lines = new ArrayList<>();
    private final TimeHelper timer = new TimeHelper();
    private final int width;
    private final int height;
    public int cursorLine = 0;
    public int cursorColumn = 0;
    int cursorAlpha = 255;
    int lineY = this.y;
    int lineX = this.x;
    public int lineNumberOffset = 15;
    public int cursorX;
    public int cursorY;
    private final Map<String, List<String>> classes = new HashMap<>();
    private final Map<String, String> variableAssignments = new HashMap<>();
    private final List<String> entityList = new ArrayList<>();
    private final List<String> entityCreate = new ArrayList<>();
    private final List<String> playerList = new ArrayList<>();
    private final List<String> cutsceneList = new ArrayList<>();
    private final List<String> scriptList = new ArrayList<>();
    private final List<String> chatList = new ArrayList<>();
    private final List<String> soundList = new ArrayList<>();
    private final List<String> asyncList = new ArrayList<>();
    private final List<String> serverList = new ArrayList<>();
    private final List<String> worldList = new ArrayList<>();
    private final List<String> eventList = new ArrayList<>();
    private final List<String> asynchronousList = new ArrayList<>();
    private final List<String> externalList = new ArrayList<>();
    private final List<String> locationList = new ArrayList<>();
    private final List<String> fadeList = new ArrayList<>();
    private boolean test5 = false;
    private String test6;
    private int index = 0;
    private List<String> matchedItems;
    int offset;
    int moveY;

    public TextAreaWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""));
        this.fontRenderer = Minecraft.getInstance().font;
        this.width = width;
        this.height = height;
        this.lines.add(" ");
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for(Method list1 : MobController.methodsToAdd) {
            if(!entityList.contains(list1.getName())) {
                entityList.add(list1.getName());
            }
        }
        for(Method list2 : Player.methodsToAdd) {
            if(!playerList.contains(list2.getName())) {
                playerList.add(list2.getName());
            }
        }
        for(Method list3 : Script.methodsToAdd) {
            if(!scriptList.contains(list3.getName())) {
                scriptList.add(list3.getName());
            }
        }
        for(Method list3 : CutsceneJS.methodsToAdd) {
            if(!cutsceneList.contains(list3.getName())) {
                cutsceneList.add(list3.getName());
            }
        }
        for(Method list3 : Chat.methodsToAdd) {
            if(!chatList.contains(list3.getName())) {
                chatList.add(list3.getName());
            }
        }
        for(Method list3 : Sounds.methodsToAdd) {
            if(!soundList.contains(list3.getName())) {
                soundList.add(list3.getName());
            }
        }
        for(Method list3 : AsyncJS.methodsToAdd) {
            if(!asyncList.contains(list3.getName())) {
                asyncList.add(list3.getName());
            }
        }
        for(Method list3 : Server.methodsToAdd) {
            if(!serverList.contains(list3.getName())) {
                serverList.add(list3.getName());
            }
        }
        for(Method list3 : WorldWrapper.methodsToAdd) {
            if(!worldList.contains(list3.getName())) {
                worldList.add(list3.getName());
            }
        }
        for(Method list3 : EventManagerJS.methodsToAdd) {
            if(!eventList.contains(list3.getName())) {
                eventList.add(list3.getName());
            }
        }
        for(Method list3 : Asynchronous.methodsToAdd) {
            if(!asynchronousList.contains(list3.getName())) {
                asynchronousList.add(list3.getName());
            }
        }
        for(Method list3 : ExternalFunctions.methodsToAdd) {
            if(!externalList.contains(list3.getName())) {
                externalList.add(list3.getName());
            }
        }
        for(Method list3 : LocationCreator.methodsToAdd) {
            if(!locationList.contains(list3.getName())) {
                locationList.add(list3.getName());
            }
        }
        for(Method list3 : FadeScreen.methodsToAdd) {
            if(!fadeList.contains(list3.getName())) {
                fadeList.add(list3.getName());
            }
        }
        classes.put("Async", asynchronousList);
        classes.put("entity", entityList);
        classes.put("player", playerList);
        classes.put("script", scriptList);
        classes.put("cutscene", cutsceneList);
        classes.put("chat", chatList);
        classes.put("sound", soundList);
        classes.put("async", asyncList);
        classes.put("server", serverList);
        classes.put("world", worldList);
        classes.put("event", eventList);
        classes.put("ExternalFunctions", externalList);
        classes.put("location", locationList);
        classes.put("fade", fadeList);

        cursorX = this.lineX + 36 + this.fontRenderer.width(this.lines.get(this.cursorLine).substring(0, this.cursorColumn));
        cursorY = this.lineY + 2 + this.cursorLine * this.fontRenderer.lineHeight;
        fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, new Color(43, 43, 43).getRGB());
        fill(matrixStack, this.x + 33, this.y, this.x + 34, this.y + this.height, new Color(101, 101, 101).getRGB());

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int lineNumber = 1;
        int yOffset = this.lineY + 2;
        lineNumberOffset = 15;
        for (String line : this.lines) {
            RenderUtils.makeScissorBox(this.x + 255, this.y + 154, this.x + this.width, this.y + this.height + 38);
            if (lineNumber > 9) lineNumberOffset = 10;
            if (lineNumber > 99) lineNumberOffset = 5;
            if (lineNumber > 999) lineNumberOffset = 0;
            if (lineNumber > 9999) lineNumberOffset = -5;
            if (lineNumber > 99999) lineNumberOffset = -10;
            if (lineNumber > 999999) lineNumberOffset = -15;
            if (lineNumber > 9999999) lineNumberOffset = -20;

            this.fontRenderer.draw(matrixStack, lineNumber + "", this.x + lineNumberOffset, yOffset + 1, new Color(101, 101, 101).getRGB());
            this.fontRenderer.draw(matrixStack, line, this.lineX + 36, yOffset, new Color(169, 183, 198).getRGB());
            yOffset += this.fontRenderer.lineHeight;
            lineNumber++;
        }

        if(timer.hasReached(500)) {
            if(cursorAlpha == 0) cursorAlpha = 255; else cursorAlpha = 0;
            timer.reset();
        }
        fill(matrixStack, this.lineX, cursorY - 1, this.lineX + this.width, cursorY + this.fontRenderer.lineHeight, new Color(196, 212, 255, 45).getRGB());
        fill(matrixStack, cursorX, cursorY, cursorX + 1, cursorY + this.fontRenderer.lineHeight, new Color(187, 187, 187, cursorAlpha).getRGB());
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        String currentLine = this.lines.get(this.cursorLine);
        String prefix = currentLine.substring(0, this.cursorColumn).trim();

        updateVariableAssignments();

        matchedItems = findMatches(prefix);
        offset = 3;
        boolean userIsTyping = !prefix.isEmpty();
        if(index > matchedItems.size()) {index = 0; moveY = 0;}
        if(userIsTyping) {
            test5 = !matchedItems.isEmpty();
            if(test5) {
                if(index <= 4) {
                    RenderUtils.drawCircleRect(cursorX - 3, cursorY + 12 + index * 11, cursorX + 120, cursorY + 22 + index * 11, 2, new Color(159, 159, 159, 55).getRGB());
                } else {
                    RenderUtils.drawCircleRect(cursorX - 3, cursorY + 12 + 44, cursorX + 120, cursorY + 22 + 44, 2, new Color(159, 159, 159, 55).getRGB());
                }

                if(matchedItems.size() >= 5) {
                    RenderUtils.drawCircleRect(cursorX - 5, cursorY + 10, cursorX + 122, cursorY + 69, 3, new Color(108, 108, 108, 55).getRGB());
                } else if(matchedItems.size() == 1) {
                    RenderUtils.drawCircleRect(cursorX - 5, cursorY + 10, cursorX + 122, cursorY + 25, 3, new Color(108, 108, 108, 55).getRGB());
                } else if(matchedItems.size() == 2) {
                    RenderUtils.drawCircleRect(cursorX - 5, cursorY + 10, cursorX + 122, cursorY + 36, 3, new Color(108, 108, 108, 55).getRGB());
                } else if(matchedItems.size() == 3) {
                    RenderUtils.drawCircleRect(cursorX - 5, cursorY + 10, cursorX + 122, cursorY + 47, 3, new Color(108, 108, 108, 55).getRGB());
                } else if(matchedItems.size() == 4) {
                    RenderUtils.drawCircleRect(cursorX - 5, cursorY + 10, cursorX + 122, cursorY + 58, 3, new Color(108, 108, 108, 55).getRGB());
                }
            }
            RenderUtils.push(cursorX, cursorY + 13, 85, 52);
            for (String item : matchedItems) {
                this.fontRenderer.draw(matrixStack, item, cursorX, cursorY + 10 + offset + moveY, new Color(176, 176, 176).getRGB());
                offset += 11;
            }
            RenderUtils.pop();
        } else {
            test5 = false;
            index = 0;
            moveY = 0;
        }
    }

    private void updateVariableAssignments() {
        variableAssignments.clear();
        Pattern pattern = Pattern.compile("var\\s+(\\w+)\\s*=\\s*(\\w+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String variable = matcher.group(1);
                String assignedValue = matcher.group(2);
                if (classes.containsKey(assignedValue)) {
                    variableAssignments.put(variable, assignedValue);
                }
            }
        }
    }

    private List<String> extractVariables() {
        List<String> variables = new ArrayList<>();
        Pattern pattern = Pattern.compile("var\\s+(\\w+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                variables.add(matcher.group(1));
            }
        }
        return variables;
    }

    private List<String> findMatches(String prefix) {
        List<String> matchedItems = new ArrayList<>();
        List<String> keywords = Arrays.asList("var", "val");

        // Поиск совпадений среди ключевых слов
        for (String keyword : keywords) {
            if (keyword.startsWith(prefix)) {
                matchedItems.add(keyword);
            }
        }

        // Поиск совпадений среди переменных
        List<String> variables = extractVariables();
        for (String variable : variables) {
            if (variable.startsWith(prefix)) {
                matchedItems.add(variable);
            }
        }

        // Поиск совпадений среди классов и методов
        for (String className : classes.keySet()) {
            if (className.startsWith(prefix)) {
                matchedItems.add(className);
            } else if (prefix.startsWith(className + ".")) {
                String methodPrefix = prefix.substring(className.length() + 1);
                for (String method : classes.get(className)) {
                    if (method.startsWith(methodPrefix)) {
                        matchedItems.add(method);
                    }
                }
            }
        }

        // Поиск совпадений среди переменных, которым присвоены классы
        for (Map.Entry<String, String> entry : variableAssignments.entrySet()) {
            String variable = entry.getKey();
            String className = entry.getValue();
            if (prefix.startsWith(variable + ".")) {
                String methodPrefix = prefix.substring(variable.length() + 1);
                for (String method : classes.get(className)) {
                    if (method.startsWith(methodPrefix)) {
                        matchedItems.add(method);
                    }
                }
            }
        }

        return matchedItems;
    }

    public void charTyped(char codePoint) {
        String currentLine = this.lines.get(this.cursorLine);
        String newLine = currentLine.substring(0, this.cursorColumn) + codePoint + currentLine.substring(this.cursorColumn);
        this.lines.set(this.cursorLine, newLine);
        this.cursorColumn++;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(Screen.isPaste(keyCode)) {
            String clipboardText = Minecraft.getInstance().keyboardHandler.getClipboard();
            if (clipboardText != null) {
                for (char c : clipboardText.toCharArray()) {
                    charTyped(c);
                    return true;
                }
            }
        } else {
            switch (keyCode) {
                case 259: // Backspace key
                    if (this.cursorColumn > 0) {
                        String currentLine = this.lines.get(this.cursorLine);
                        String newLine = currentLine.substring(0, this.cursorColumn - 1) + currentLine.substring(this.cursorColumn);
                        this.lines.set(this.cursorLine, newLine);
                        this.cursorColumn--;
                    } else if (this.cursorLine > 0) {
                        String previousLine = this.lines.remove(this.cursorLine);
                        this.cursorLine--;
                        this.cursorColumn = this.lines.get(this.cursorLine).length();
                        this.lines.set(this.cursorLine, this.lines.get(this.cursorLine) + previousLine);
                        ensureCursorInView();
                    }
                    return true;
                case 257:
                case 335: // Enter key
                    String currentLine = this.lines.get(this.cursorLine);
                    String newLine = currentLine.substring(this.cursorColumn);
                    this.lines.set(this.cursorLine, currentLine.substring(0, this.cursorColumn));
                    this.lines.add(this.cursorLine + 1, newLine);
                    this.cursorLine++;
                    this.cursorColumn = 0;
                    ensureCursorInView();
                    return true;
                case 263: // Left arrow key
                    if (this.cursorColumn > 0) {
                        this.cursorColumn--;
                    } else if (this.cursorLine > 0) {
                        this.cursorLine--;
                        this.cursorColumn = this.lines.get(this.cursorLine).length();
                    }
                    return true;
                case 262: // Right arrow key
                    if (this.cursorColumn < this.lines.get(this.cursorLine).length()) {
                        this.cursorColumn++;
                    } else if (this.cursorLine < this.lines.size() - 1) {
                        this.cursorLine++;
                        this.cursorColumn = 0;
                    }
                    return true;
                case 265: // Up arrow key
                    if (!test5) {
                        if (this.cursorLine > 0) {
                            this.cursorLine--;
                            this.cursorColumn = Math.min(this.cursorColumn, this.lines.get(this.cursorLine).length());
                            ensureCursorInView();
                        }
                    } else {
                        if (index <= 0) {
                            index = matchedItems.size() - 1;
                            moveY = -(matchedItems.size() - 5) * 11;
                        } else {
                            index--;
                            if(index >= 4) moveY += 11;
                        }
                    }
                    return true;
                case 264: // Down arrow key
                    if (!test5) {
                        if (this.cursorLine < this.lines.size() - 1) {
                            this.cursorLine++;
                            this.cursorColumn = Math.min(this.cursorColumn, this.lines.get(this.cursorLine).length());
                            ensureCursorInView();
                        }
                    } else {
                        if (index >= matchedItems.size() - 1) {
                            index = 0;
                            moveY = 0;
                        } else {
                            index++;
                            if(index >= 5) moveY -= 11;
                        }
                    }
                    return true;
                case 258: // Tab key
                    String currentLineTab = this.lines.get(this.cursorLine);
                    test6 = matchedItems.get(index);
                    if(!test5) {
                        String newLineTab = currentLineTab.substring(0, this.cursorColumn) + "    " + currentLineTab.substring(this.cursorColumn);
                        this.lines.set(this.cursorLine, newLineTab);
                        this.cursorColumn += 4;
                    } else {
                        int prefixLength = findPrefixLength(currentLineTab.substring(0, this.cursorColumn), test6);
                        String newLineTab = currentLineTab.substring(0, this.cursorColumn - prefixLength) + test6 + currentLineTab.substring(this.cursorColumn);
                        this.cursorColumn = this.cursorColumn - prefixLength + test6.length();
                        this.lines.set(this.cursorLine, newLineTab);
                    }
                    return true;
                case 83:
                    if(Screen.hasControlDown()) {
                        save();
                        return true;
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    public void save() {
        try {
            FileWriter file = new FileWriter(SVEngine.SCRIPTS_PATH + "script.js");
            StringBuilder writeText = new StringBuilder();
            for(String text : this.lines) {
                writeText.append(text).append("\n");
            }
            file.write(writeText.toString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int findPrefixLength(String line, String match) {
        for (int i = match.length(); i > 0; i--) {
            if (line.endsWith(match.substring(0, i))) {
                return i;
            }
        }
        return 0;
    }

    private void ensureCursorInView() {
        int cursorY = this.lineY + 2 + this.cursorLine * this.fontRenderer.lineHeight;

        if (cursorY < this.y) {
            this.lineY += this.fontRenderer.lineHeight;
        } else if (cursorY + this.fontRenderer.lineHeight > this.y + this.height) {
            this.lineY -= this.fontRenderer.lineHeight;
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int totalTextHeight = this.lines.size() * this.fontRenderer.lineHeight + 3;
        int maxLineY = this.y;
        int minLineY = this.y + this.height - totalTextHeight;

        if (delta > 0 && this.lineY < maxLineY) { // Scrolling up
            this.lineY = Math.min(maxLineY, this.lineY + (int) delta * this.fontRenderer.lineHeight);
            return true;
        } else if (delta < 0 && this.lineY > minLineY) { // Scrolling down
            this.lineY = Math.max(minLineY, this.lineY + (int) delta * this.fontRenderer.lineHeight);
            return true;
        }
        return false;
    }
}