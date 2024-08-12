package org.thesalutyt.storyverse.common.items.adder.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;

import java.util.HashMap;

public class ArmorItem extends net.minecraft.item.ArmorItem {
    public static HashMap<String, ArmorItem> items = new HashMap<>();
    public String name;

    public ArmorItem(String name, IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
        super(material, slot, properties);
        this.name = name;

        items.put(material.getName(), this);
    }
}
