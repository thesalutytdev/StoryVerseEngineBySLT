package org.thesalutyt.storyverse.common.echantments.adder;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.minecraft.enchantment.JSEnchant;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.echantments.ModEnchants;
import org.thesalutyt.storyverse.common.elements.ICustomElement;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EnchantAdder extends ScriptableObject implements ICustomElement, EnvResource, JSResource {
    private String id;
    private Enchantment.Rarity rarity;
    private EnchantmentType type;
    private int maxLevel;
    private EquipmentSlotType[] slots;
    private ArrayList<BaseFunction> onTick = new ArrayList<>();
    private ArrayList<BaseFunction> onAttack = new ArrayList<>();
    private ArrayList<BaseFunction> onHurt = new ArrayList<>();
    private ArrayList<Enchantment> incompatible = new ArrayList<>();
    private int stage = 0;
    private boolean built = false;
    private CustomEnchant enchantment;
    private Enchantment enchant;

    public EnchantAdder create(String name) {
        this.id = name;
        this.stage++;

        return this;
    }

    public EnchantAdder setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
        this.stage++;

        return this;
    }

    public EnchantAdder setRarity(String rarity) {
        this.rarity = getRarity(rarity);
        this.stage++;

        return this;
    }

    public EnchantAdder setType(String type) {
        this.type = getEnchantmentType(type);
        this.stage++;

        return this;
    }

    public EnchantAdder setSlots(NativeArray slots_) {
        String[] slots = new String[(int) slots_.getLength()];

        for (int i = 0; i < slots_.getLength(); i++) {
            slots[i] = String.valueOf(slots_.get(i));
        }

        this.slots = new EquipmentSlotType[slots.length];
        for (int i = 0; i < slots.length; i++) {
            this.slots[i] = getEquipmentSlotType(slots[i]);
        }
        this.stage++;

        return this;
    }

    public ArrayList<BaseFunction> getOnAttack() {
        return onAttack;
    }

    public void setOnAttack(BaseFunction onAttack_) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onAttack.add(onAttack_);
        });
    }

    public ArrayList<BaseFunction> getOnHurt() {
        return onHurt;
    }

    public void setOnHurt(BaseFunction onHurt_) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onHurt.add(onHurt_);
        });
    }

    public EnchantAdder addIncompatible(String id, String mod) {
        incompatible.add(JSEnchant.getEnchant(mod, id));

        if (this.enchantment != null) this.enchantment.setIncompatible(incompatible);

        return this;
    }

    public ArrayList<Enchantment> getIncompatible() {
        return incompatible;
    }

    public ArrayList<BaseFunction> getOnTick() {
        return onTick;
    }

    public void setOnTick(BaseFunction onTick_) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onTick.add(onTick_);
        });
    }

    public EnchantAdder build() {
        if (stage < 5) throw new IllegalStateException("Not enough stages");

        this.enchantment = new CustomEnchant(this.id, this.maxLevel, this.rarity, this.type, this.slots);
        this.enchantment.setOnTick(this.onTick);
        this.enchantment.setOnAttack(this.onAttack);
        this.enchantment.setOnHurt(this.onHurt);
        this.enchantment.setIncompatible(this.incompatible);
        this.built = true;

        this.enchant = enchantment;

        ModEnchants.addEnchantment(this.id, enchant);

        return this;
    }

    public static EnchantmentType getEnchantmentType(String type) {
        switch (type.toLowerCase()) {
            case "weapon":
                return EnchantmentType.WEAPON;
            case "armor":
                return EnchantmentType.ARMOR;
            case "armor-feet":
                return EnchantmentType.ARMOR_FEET;
            case "armor-legs":
                return EnchantmentType.ARMOR_LEGS;
            case "armor-chest":
                return EnchantmentType.ARMOR_CHEST;
            case "armor-head":
                return EnchantmentType.ARMOR_HEAD;
            case "crossbow":
                return EnchantmentType.CROSSBOW;
            case "bow":
                return EnchantmentType.BOW;
            case "fishing-rod":
                return EnchantmentType.FISHING_ROD;
            case "trident":
                return EnchantmentType.TRIDENT;
            case "vanishing":
                return EnchantmentType.VANISHABLE;
            case "breakable":
                return EnchantmentType.BREAKABLE;
            case "digger":
                return EnchantmentType.DIGGER;
            case "wearable":
                return EnchantmentType.WEARABLE;
            default:
                return null;
        }
    }

    public static EquipmentSlotType getEquipmentSlotType(String type) {
        switch (type.toLowerCase()) {
            case "head":
                return EquipmentSlotType.HEAD;
            case "chest":
                return EquipmentSlotType.CHEST;
            case "legs":
                return EquipmentSlotType.LEGS;
            case "feet":
                return EquipmentSlotType.FEET;
            case "off-hand":
            case "offhand":
                return EquipmentSlotType.OFFHAND;
            default:
                return null;
        }
    }

    public static Enchantment.Rarity getRarity(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common":
                return Enchantment.Rarity.COMMON;
            case "uncommon":
                return Enchantment.Rarity.UNCOMMON;
            case "rare":
                return Enchantment.Rarity.RARE;
            case "epic":
            case "very-rare":
                return Enchantment.Rarity.VERY_RARE;
            default:
                return null;
        }
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        EnchantAdder ef = new EnchantAdder();
        ef.setParentScope(scope);

        try {
            Method enchant = EnchantAdder.class.getMethod("create", String.class);
            methodsToAdd.add(enchant);
            Method enchant1 = EnchantAdder.class.getMethod("setMaxLevel", Integer.class);
            methodsToAdd.add(enchant1);
            Method enchant2 = EnchantAdder.class.getMethod("setType", String.class);
            methodsToAdd.add(enchant2);
            Method enchant3 = EnchantAdder.class.getMethod("setRarity", String.class);
            methodsToAdd.add(enchant3);
            Method enchant4 = EnchantAdder.class.getMethod("setSlots", NativeArray.class);
            methodsToAdd.add(enchant4);
            Method enchant5 = EnchantAdder.class.getMethod("setOnAttack", BaseFunction.class);
            methodsToAdd.add(enchant5);
            Method enchant6 = EnchantAdder.class.getMethod("setOnHurt", BaseFunction.class);
            methodsToAdd.add(enchant6);
            Method enchant7 = EnchantAdder.class.getMethod("setOnTick", BaseFunction.class);
            methodsToAdd.add(enchant7);
            Method enchant8 = EnchantAdder.class.getMethod("build");
            methodsToAdd.add(enchant8);
            Method getOnAttack = EnchantAdder.class.getMethod("getOnAttack");
            methodsToAdd.add(getOnAttack);
            Method getOnHurt = EnchantAdder.class.getMethod("getOnHurt");
            methodsToAdd.add(getOnHurt);
            Method getOnTick = EnchantAdder.class.getMethod("getOnTick");
            methodsToAdd.add(getOnTick);
            Method addIncompatible = EnchantAdder.class.getMethod("addIncompatible", String.class, String.class);
            methodsToAdd.add(addIncompatible);
            Method getIncompatible = EnchantAdder.class.getMethod("getIncompatible");
            methodsToAdd.add(getIncompatible);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("enchant", scope, ef);
    }

    @Override
    public Object getDefaultElement() {
        return Enchantment.class;
    }

    @Override
    public String getClassName() {
        return "EnchantAdder";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "EnchantAdder";
    }
}
