package org.thesalutyt.storyverse.api.environment.resource.wrappers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;

import java.util.HashMap;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class FadeScreenWrapper {
    public static HashMap<UUID, FadeScreenWrapper> fades = new HashMap<>();

    private final UUID id;
    private final String text;
    private final int in;
    private final int intro;
    private final int outro;
    private final int color;
    private boolean expired = false;

    public static FadeScreenWrapper parse(String input) {
        String[] parts = input.split(";");
        return new FadeScreenWrapper(parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]));
    }

    public FadeScreenWrapper(String text, int in, int intro, int outro, int color) {
        this.id = UUID.randomUUID();
        this.text = text;
        this.in = in;
        this.intro = intro;
        this.outro = outro;
        this.color = color;
        this.expired = false;

        fades.put(this.id, this);
    }

    public FadeScreenWrapper(int in, int intro, int outro, int color) {
        this(" ", in, intro, outro, color);
    }

    public void fade() {
        if (isExpired()) throw new RuntimeException("This wrapper is already expired!");
        FadeScreenGui.fade(getText(), getInTime(), getColor(), getIntroTime(), getOutroTime());
        expire();
    }

    protected void expire() {
        this.expired = true;
        fades.remove(this.id, this);
    }

    public String getText() {
        return this.text;
    }

    public int getInTime() {
        return this.in;
    }

    public int getIntroTime() {
        return this.intro;
    }

    public int getOutroTime() {
        return this.outro;
    }

    public int getColor() {
        return this.color;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public UUID getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.text + ";" + this.in + ";" + this.intro + ";" + this.outro + ";" + this.color;
    }
}
