package org.thesalutyt.storyverse.api.environment.trader;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.features.Server;

public class Trader {
    public LivingEntity entity;
    public String id;
    public Offers offers;

    public Trader(String id) {
        this.entity = MobJS.getMob(id).getMobEntity();
        this.id = id;
        this.offers = new Offers(this);
    }

    public boolean trade(int trade_id, String player_name) {
        ServerPlayerEntity player = Server.getPlayerByName(player_name);
        TradeOffer offer = this.offers.getOffer(trade_id);

        if (offer == null) return false;
        if (offer.trader != this) return false;
        if (!offer.isActive) return false;

        boolean second = false;

        if (offer.needSecond != null) {
            second = true;
        }
        boolean hasNeedSecond = false;
        boolean hasNeededFirst = false;

        for (ItemStack item : player.inventory.items) {
            if (item == offer.needFirst ||
                    item.getItem() == offer.needFirst.getItem() &&
                            item.getCount() >= offer.needFirst.getCount()) {
                hasNeededFirst = true;
            }

            if (item == offer.needSecond ||
                    item.getItem() == offer.needSecond.getItem() &&
                            item.getCount() >= offer.needSecond.getCount() &&
                            second) {
                hasNeedSecond = true;
            }
        }

        if (hasNeededFirst && !second || offer.needSecond == null) {
            return true;
        } else if (hasNeededFirst && hasNeedSecond && second) {
            return true;
        } else {
            return false;
        }
    }

    public boolean trade(TradeOffer offer, String player_name) {
        ServerPlayerEntity player = Server.getPlayerByName(player_name);

        if (offer == null) return false;
        if (offer.trader != this) return false;
        if (!offer.isActive) return false;

        boolean second = false;

        if (offer.needSecond != null) {
            second = true;
        }
        boolean hasNeedSecond = false;
        boolean hasNeededFirst = false;

        for (ItemStack item : player.inventory.items) {
            if (item == offer.needFirst ||
                    item.getItem() == offer.needFirst.getItem() &&
                            item.getCount() >= offer.needFirst.getCount()) {
                hasNeededFirst = true;
            }

            if (item == offer.needSecond ||
                    item.getItem() == offer.needSecond.getItem() &&
                            item.getCount() >= offer.needSecond.getCount() &&
                            second) {
                hasNeedSecond = true;
            }
        }

        if (hasNeededFirst && !second || offer.needSecond == null) {
            return true;
        } else if (hasNeededFirst && hasNeedSecond && second) {
            return true;
        } else {
            return false;
        }
    }
}
