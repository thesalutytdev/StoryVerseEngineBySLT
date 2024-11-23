package org.thesalutyt.storyverse.common.entities.adder.essential.type;

public enum CustomEntityType {
    ANIMAL("animal"),
    MOB("mob"),
    FLYING("flying");

    private final String name;

    CustomEntityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
