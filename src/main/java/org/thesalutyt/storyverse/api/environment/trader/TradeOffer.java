package org.thesalutyt.storyverse.api.environment.trader;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class TradeOffer {
    public ItemStack needFirst;
    public ItemStack needSecond;
    public ItemStack give;
    public String name;
    public Trader trader;
    public double xp_p;
    public double xp_l;
    protected boolean isActive = true;

    public TradeOffer(ItemStack needFirst, ItemStack give, String name) {
        this.needFirst = needFirst;
        this.give = give;
        this.name = name;
    }

    public TradeOffer(ItemStack needFirst, ItemStack needSecond,  ItemStack give,  String name) {
        this.needFirst = needFirst;
        this.needSecond = needSecond;
        this.give = give;
        this.name = name;
    }

    public TradeOffer(ItemStack needFirst, ItemStack give, String name, double xp_p, double xp_l) {
        this.needFirst = needFirst;
        this.give = give;
        this.name = name;
        this.xp_p = xp_p;
        this.xp_l = xp_l;
    }

    public TradeOffer(ItemStack needFirst, ItemStack needSecond, ItemStack give, String name, double xp_p, double xp_l) {
        this.needFirst = needFirst;
        this.needSecond = needSecond;
        this.give = give;
        this.name = name;
        this.xp_p = xp_p;
        this.xp_l = xp_l;
    }

    public TradeOffer(ItemStack needFirst, ItemStack give, String name, double xp_p, double xp_l, Trader trader) {
        this.needFirst = needFirst;
        this.give = give;
        this.name = name;
        this.xp_p = xp_p;
        this.xp_l = xp_l;
        this.trader = trader;
    }

    public TradeOffer setTrader(Trader trader) {
        this.trader = trader;
        return this;
    }

    public TradeOffer setXp(double xp_p, double xp_l) {
        this.xp_p = xp_p;
        this.xp_l = xp_l;
        return this;
    }

    public TradeOffer setGive(ItemStack give) {
        this.give = give;
        return this;
    }

    public TradeOffer setName(String name) {
        this.name = name;
        return this;
    }

    public TradeOffer setNeedFirst(ItemStack needFirst) {
        this.needFirst = needFirst;
        return this;
    }

    public TradeOffer setNeedSecond(ItemStack needSecond) {
        this.needSecond = needSecond;
        return this;
    }

    public TradeOffer setNeed(ItemStack needFirst, ItemStack needSecond) {
        this.needFirst = needFirst;
        this.needSecond = needSecond;
        return this;
    }

    public TradeOffer setXpPoints(double xp_p) {
        this.xp_p = xp_p;
        return this;
    }

    public TradeOffer setXpLevels(double xp_l) {
        this.xp_l = xp_l;
        return this;
    }

    public ArrayList<ItemStack> getStacks() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        stacks.add(needFirst);
        if (needSecond != null) stacks.add(needSecond);
        stacks.add(give);
        return stacks;
    }

    public TradeOffer setActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }
}
