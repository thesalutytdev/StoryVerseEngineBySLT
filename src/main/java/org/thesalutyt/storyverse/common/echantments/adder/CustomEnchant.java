package org.thesalutyt.storyverse.common.echantments.adder;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class CustomEnchant extends Enchantment {
    public static ArrayList<CustomEnchant> customEnchants = new ArrayList<>();

    private final String id;
    private final int maxLevel;
    private ArrayList<BaseFunction> onTick = new ArrayList<>();
    private ArrayList<BaseFunction> onAttack = new ArrayList<>();
    private ArrayList<BaseFunction> onHurt = new ArrayList<>();
    private ArrayList<Enchantment> incompatible = new ArrayList<>();

    public CustomEnchant(String id, int maxLevel, Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType[] slots) {
        super(rarity, enchantmentType, slots);

        this.id = id;
        this.maxLevel = maxLevel;
    }

    public void onTick(PlayerEntity player) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            int level = EnchantmentHelper.getEnchantmentLevel(this, player);

            if (level <= 0) return;

            for (BaseFunction f : onTick) {
                Context ctx = Context.getCurrentContext();

                if (SVEngine.interpreter == null) {
                    f.call(ctx,
                            SVEngine.modInterpreter.getScope(),
                            SVEngine.modInterpreter.getScope(),
                            new Object[]{level, player}
                    );
                    return;
                }
                f.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(), new Object[]{level,
                        new Player((ServerPlayerEntity) player)});
            }
        });
    }

    @ParametersAreNonnullByDefault
    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        super.doPostAttack(attacker, target, level);

        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction f : onAttack) {
                Context ctx = Context.getCurrentContext();

                if (SVEngine.interpreter == null) {
                    f.call(ctx,
                            SVEngine.modInterpreter.getScope(),
                            SVEngine.modInterpreter.getScope(),
                            new Object[]{new MobController(attacker),
                                    new MobController(target),
                                    level}
                    );
                    return;
                }
                f.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(),
                        new Object[]{new MobController(attacker),
                                new MobController(target),
                                level});
            }
        });
    }

    @ParametersAreNonnullByDefault
    @Override
    public void doPostHurt(LivingEntity target, Entity attacker, int damage) {
        super.doPostHurt(target, attacker, damage);

        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction f : onHurt) {
                Context ctx = Context.getCurrentContext();

                    if (SVEngine.interpreter == null) {
                        f.call(ctx,
                            SVEngine.modInterpreter.getScope(),
                            SVEngine.modInterpreter.getScope(),
                            new Object[]{new MobController(target), new MobController(attacker), damage}
                    );
                    return;
                }

                f.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(), new Object[]{
                        new MobController(target),
                        new MobController(attacker), damage});
            }
        });
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || event.player.level.isClientSide) return;

        customEnchants.forEach(enchant -> enchant.onTick(event.player));
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        if (incompatible.contains(other)) return false;
        return super.checkCompatibility(other);
    }

    public void setOnTick(ArrayList<BaseFunction> onTick) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onTick = onTick;
        });
    }

    public void setOnAttack(ArrayList<BaseFunction> onAttack) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onAttack = onAttack;
        });
    }

    public void setOnHurt(ArrayList<BaseFunction> onHurt) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.onHurt = onHurt;
        });
    }

    public void setIncompatible(ArrayList<Enchantment> incompatible) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.incompatible = incompatible;
        });
    }

    public void addIncompatible(Enchantment enchantment) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            this.incompatible.add(enchantment);
        });
    }

    public ArrayList<BaseFunction> getOnTick() {
        return onTick;
    }

    public ArrayList<BaseFunction> getOnAttack() {
        return onAttack;
    }

    public ArrayList<BaseFunction> getOnHurt() {
        return onHurt;
    }

    public ArrayList<Enchantment> getIncompatible() {
        return incompatible;
    }

    public String getId() {
        return id;
    }
}
