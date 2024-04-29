package org.thesalutyt.storyverse.api.special;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;

import java.util.HashMap;
import java.util.UUID;

public class TextDialogue implements EnvResource {
    private MobController entity;
    private ServerPlayerEntity player;
    private LegacyEventManager eventManager;
    public HashMap<UUID, UUID> dialogues = new HashMap<>();
    public HashMap<UUID, String> choices = new HashMap<>();
    public HashMap<String, Runnable> actions = new HashMap<>();
    public UUID TextDialogue(MobController mob, ServerPlayerEntity player, String first_choice) {
        this.entity = mob;
        this.player = player;
        UUID dialogue_UUID = UUID.randomUUID();
        UUID choices_UUID = UUID.randomUUID();
        this.dialogues.put(dialogue_UUID, choices_UUID);
        this.choices.put(choices_UUID, first_choice);
        return dialogue_UUID;
    }
    public TextDialogue addChoice(UUID dialogue_UUID, String new_choice, Runnable runnable) {
        UUID choices_UUID = this.dialogues.get(dialogue_UUID);
        this.choices.put(choices_UUID, new_choice);
        this.actions.put(new_choice, runnable);

        return this;
    }
    public TextDialogue onRightMessage(String message) {
        try {
            actions.get(message).run();
            actions.remove(message);
        } catch (NullPointerException ex) {
            System.out.println("[TextDialogue::onMessage] Message haven't register");
        }

        return this;
    }

    @Override
    public String getResourceId() {
        return "TextDialogue";
    }
}
