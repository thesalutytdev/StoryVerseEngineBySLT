package org.thesalutyt.storyverse.api.environment.resource.wrappers;

import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import java.util.ArrayList;

public class NPCData extends EntityData {
    public String model;
    public String animations;
    public String texture;
    public NPCEntity npc;
    public static ArrayList<NPCData> npcs = new ArrayList<>();

    public NPCData(NPCEntity npc, double x, double y, double z, Object... args) {
        super(npc, x, y, z, args);
        this.model = (String) args[0];
        this.animations = (String) args[1];
        this.texture = (String) args[2];
        this.npc = npc;

        npcs.add(this);
    }

    public NPCData(NPCEntity npc, String name, double x, double y, double z, Object... args) {
        super(npc, name, x, y, z, args);
        this.model = (String) args[0];
        this.animations = (String) args[1];
        this.texture = (String) args[2];

        this.npc = npc;

        npcs.add(this);
    }

    public String dump() {
        return "{\"" + getUUID() + "\": {\"model\": \"" + model + "\", \"animations\": \"" + animations + "\", \"texture\": \"" + texture + "\"}}";
    }

    public NPCEntity getNpc() {
        return npc;
    }

    public void setNpc(NPCEntity npc) {
        this.npc = npc;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
