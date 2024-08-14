package org.thesalutyt.storyverse.common.items.adder.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomArmorMaterial extends ScriptableObject implements IArmorMaterial, EnvResource, JSResource {
    public static HashMap<String, CustomArmorMaterial> materials = new HashMap<>();

    public String name;
    public HashMap<EquipmentSlotType, Integer> durability = new HashMap<>();
    public HashMap<EquipmentSlotType, Integer> defense = new HashMap<>();
    public int enchantmentValue = 0;
    public SoundEvent equipSound;
    public float toughness = 0;
    public float knockbackResistance = 0;
    public Ingredient repairIngredient;
    protected int stage = 0;

    public CustomArmorMaterial() {

    }

    public CustomArmorMaterial create(String name) {
        this.name = name;
        stage++;
        return this;
    }

    public CustomArmorMaterial setDurabilityForSlot(String slot, Integer durability) {
        this.durability.put(EquipmentSlotType.valueOf(slot.toLowerCase()), durability);
        stage++;
        return this;
    }

    public CustomArmorMaterial setDefenseForSlot(String slot, Integer defense) {
        this.defense.put(EquipmentSlotType.valueOf(slot.toLowerCase()), defense);
        stage++;
        return this;
    }

    public CustomArmorMaterial setEnchantmentValue(Integer enchantmentValue) {
        this.enchantmentValue = enchantmentValue;
        stage++;
        return this;
    }

    public CustomArmorMaterial setEquipSound(String equipSound) {
        this.equipSound = new SoundEvent(new ResourceLocation(equipSound));
        stage++;
        return this;
    }

    public CustomArmorMaterial setToughness(Double toughness) {
        this.toughness = toughness.floatValue();
        stage++;
        return this;
    }

    public CustomArmorMaterial setKnockbackResistance(Double knockbackResistance) {
        this.knockbackResistance = knockbackResistance.floatValue();
        stage++;
        return this;
    }

    public CustomArmorMaterial setRepairIngredient(String repairIngredient) {
        this.repairIngredient = Ingredient.of(JSItem.getStack(repairIngredient));
        stage++; // 8
        return this;
    }

    public CustomArmorMaterial build() {
        if (stage != 8) {
            throw new RuntimeException("Not all properties were set");
        }
        materials.put(name, this);
        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        CustomArmorMaterial cs = new CustomArmorMaterial();
        cs.setParentScope(scope);
        try {
            Method create = CustomArmorMaterial.class.getMethod("create", String.class);
            methodsToAdd.add(create);
            Method setDurabilityForSlot = CustomArmorMaterial.class.getMethod("setDurabilityForSlot", String.class, Integer.class);
            methodsToAdd.add(setDurabilityForSlot);
            Method setDefenseForSlot = CustomArmorMaterial.class.getMethod("setDefenseForSlot", String.class, Integer.class);
            methodsToAdd.add(setDefenseForSlot);
            Method setEnchantmentValue = CustomArmorMaterial.class.getMethod("setEnchantmentValue", Integer.class);
            methodsToAdd.add(setEnchantmentValue);
            Method setEquipSound = CustomArmorMaterial.class.getMethod("setEquipSound", String.class);
            methodsToAdd.add(setEquipSound);
            Method setToughness = CustomArmorMaterial.class.getMethod("setToughness", Double.class);
            methodsToAdd.add(setToughness);
            Method setKnockbackResistance = CustomArmorMaterial.class.getMethod("setKnockbackResistance", Double.class);
            methodsToAdd.add(setKnockbackResistance);
            Method setRepairIngredient = CustomArmorMaterial.class.getMethod("setRepairIngredient", String.class);
            methodsToAdd.add(setRepairIngredient);
            Method build = CustomArmorMaterial.class.getMethod("build");
            methodsToAdd.add(build);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, cs);
            cs.put(m.getName(), cs, methodInstance);
        }
        scope.put("armorMaterial", scope, cs);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getDurabilityForSlot(EquipmentSlotType slot) {
        return this.durability.get(slot);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getDefenseForSlot(EquipmentSlotType slot) {
        return this.defense.get(slot);
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    @Override
    public String getClassName() {
        return "CustomArmorMaterial";
    }

    @Override
    public String getResourceId() {
        return "CustomArmorMaterial";
    }
}
