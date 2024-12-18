package org.thesalutyt.storyverse.api.environment.resource.wrappers;

import net.minecraft.entity.LivingEntity;

import java.util.UUID;

public class EntityData {
    public double x;
    public double y;
    public double z;
    public String name;
    public boolean name_visible = true;
    public Object[] args;
    public LivingEntity entity;
    public UUID entityUUID;

    public EntityData(LivingEntity type, double x, double y, double z, Object... args) {
        this.entity = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.args = args;
    }

    public EntityData(LivingEntity type, String name, double x, double y, double z, Object... args) {
        this(type, x, y, z, args);
        this.name = name;
    }

    public EntityData(LivingEntity type, double x, double y, double z) {
        this.entity = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityData(LivingEntity type, String name, double x, double y, double z) {
        this(type, x, y, z);
        this.name = name;
    }

    public void setNameVisible(boolean visible) {
        this.name_visible = visible;
    }

    public void setUUID(UUID uuid) {
        this.entityUUID = uuid;
    }

    public UUID getUUID() {
        return this.entityUUID;
    }

    public String dump() {
        return "{\" " + getUUID() + " \"{\"name\":\"" + name + "\",\"x\":\"" + x + "\",\"y\":\"" + y + "\",\"z\":\"" + z + "\"}}";
    }
}
