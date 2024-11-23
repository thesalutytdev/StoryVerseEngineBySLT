package org.thesalutyt.storyverse.common.entities.adder;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.elements.ICustomElement;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomAnimalEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomFlyingEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.CustomMobEntity;
import org.thesalutyt.storyverse.common.entities.adder.essential.arguments.EntityArgumentList;
import org.thesalutyt.storyverse.common.entities.adder.essential.renderers.CustomAnimalEntityRender;
import org.thesalutyt.storyverse.common.entities.adder.essential.renderers.CustomMobEntityRender;
import org.thesalutyt.storyverse.common.entities.adder.essential.type.CustomEntityType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityAdder extends ScriptableObject implements ICustomElement, EnvResource, JSResource {
    public static HashMap<String, LivingEntity> entityHashMap = new HashMap<>();

    private String name;
    private CustomEntityType type;
    private ResourceLocation path;
    private EntityArgumentList arguments = new EntityArgumentList();
    private LivingEntity finalEntity;

    public EntityAdder(String name, CustomEntityType type, ResourceLocation path) {
        this.name = name;
        this.type = type;
        this.path = path;
    }

    private EntityAdder() {}

    public EntityAdder create(String modId, String name, String type) {
        switch (type.toLowerCase()) {
            case "mob":
                return new EntityAdder(name, CustomEntityType.MOB, new ResourceLocation(modId, name));
            case "animal":
                return new EntityAdder(name, CustomEntityType.ANIMAL, new ResourceLocation(modId, name));
            case "flying":
                return new EntityAdder(name, CustomEntityType.FLYING, new ResourceLocation(modId, name));
            default:
                throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }

    public EntityAdder setInvisible(Boolean invisible) {
        this.arguments.setInvisible(invisible);
        return this;
    }

    public EntityAdder setInvulnerable(Boolean invulnerable) {
        this.arguments.setInvulnerable(invulnerable);
        return this;
    }

    public EntityAdder setSilent(Boolean silent) {
        this.arguments.setSilent(silent);
        return this;
    }

    public EntityAdder setCanPickUp(Boolean canPickUp) {
        this.arguments.setCanPickUp(canPickUp);
        return this;
    }

    public EntityAdder setAttackable(Boolean isAttackable) {
        this.arguments.setAttackable(isAttackable);
        return this;
    }

    public EntityAdder setCanOpenDoor(Boolean canOpenDoor) {
        this.arguments.setOpenDoor(canOpenDoor);
        return this;
    }

    public void build() {
        switch (type) {
            case MOB:
                this.finalEntity = new CustomMobEntity(arguments, name);
                Entities.addEntity(name, finalEntity, CustomMobEntityRender.class);
                break;
            case ANIMAL:
                this.finalEntity = new CustomAnimalEntity(arguments, name);
                Entities.addEntity(name, finalEntity, CustomAnimalEntityRender.class);
                break;
            case FLYING:
                this.finalEntity = new CustomFlyingEntity(arguments, name);
                Entities.addEntity(name, finalEntity, CustomMobEntityRender.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + type);
        }
        entityHashMap.put(name, finalEntity);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        EntityAdder entityAdder = new EntityAdder();
        entityAdder.setParentScope(scope);

        try {
            Method create = EntityAdder.class.getMethod("create", String.class, String.class, String.class);
            methodsToAdd.add(create);
            Method setInvisible = EntityAdder.class.getMethod("setInvisible", Boolean.class);
            methodsToAdd.add(setInvisible);
            Method setInvulnerable = EntityAdder.class.getMethod("setInvulnerable", Boolean.class);
            methodsToAdd.add(setInvulnerable);
            Method setSilent = EntityAdder.class.getMethod("setSilent", Boolean.class);
            methodsToAdd.add(setSilent);
            Method setCanPickUp = EntityAdder.class.getMethod("setCanPickUp", Boolean.class);
            methodsToAdd.add(setCanPickUp);
            Method setAttackable = EntityAdder.class.getMethod("setAttackable", Boolean.class);
            methodsToAdd.add(setAttackable);
            Method setCanOpenDoor = EntityAdder.class.getMethod("setCanOpenDoor", Boolean.class);
            methodsToAdd.add(setCanOpenDoor);
            Method build = EntityAdder.class.getMethod("build");
            methodsToAdd.add(build);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, entityAdder);
            entityAdder.put(m.getName(), entityAdder, methodInstance);
        }

        scope.put("entity", scope, entityAdder);
    }

    @Override
    public Object getDefaultElement() {
        return EntityType.class;
    }

    @Override
    public String getClassName() {
        return "EntityAdder";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "EntityAdder";
    }
}
