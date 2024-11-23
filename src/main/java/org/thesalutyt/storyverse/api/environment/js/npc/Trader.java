package org.thesalutyt.storyverse.api.environment.js.npc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.util.text.StringTextComponent;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;
import org.thesalutyt.storyverse.utils.ErrorPrinter;
import org.thesalutyt.storyverse.utils.StoryUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Trader extends ScriptableObject implements EnvResource, JSResource {
    public NPCEntity entity;

    private Trader() {

    }

    public Trader init(String mobId) {
        try {
            this.entity = (NPCEntity) MobJS.getMob(mobId).getEntity();
            this.entity.isTrader = true;
            return this;
        } catch (Exception e) {
            new ErrorPrinter(e);
            return null;
        }
    }

    public Trader setName(String name) {
        this.entity.traderName = name;
        return this;
    }

    public Trader showProgressBar(Boolean show) {
        this.entity.showProgressBar = show;
        return this;
    }

    public Trader addTrade(String costA, String costB, String saleItem, Integer maxUses, Integer xp) {
        this.entity.offers = new MerchantOffers();
        ItemStack cost0 = JSItem.getStack(costA);
        ItemStack cost1 = JSItem.getStack(costB);
        ItemStack sale = JSItem.getStack(saleItem);

        this.entity.offers.add(new MerchantOffer(
                cost0,
                cost1,
                sale,
                0, maxUses, xp, 1f));
        if (SVEngine.IS_DEBUG) {
            System.out.println(this.entity.offers + "\n" + this.entity.offers.createTag());
        }
        return this;
    }

    public Trader open(String player) {
        StoryUtils.openTrade(Server.getPlayerByName(player), new StringTextComponent(entity.traderName),
                entity.offers, entity.getVillagerXp(), entity.showProgressBar);

        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        Trader ef = new Trader();
        ef.setParentScope(scope);
        try {
            Method init = Trader.class.getMethod("init", String.class);
            methodsToAdd.add(init);
            Method addTrade = Trader.class.getMethod("addTrade",
                    String.class, String.class, String.class,
                    Integer.class, Integer.class);
            methodsToAdd.add(addTrade);
            Method showProgressBar = Trader.class.getMethod("showProgressBar", Boolean.class);
            methodsToAdd.add(showProgressBar);
            Method setName = Trader.class.getMethod("setName", String.class);
            methodsToAdd.add(setName);
            Method open = Trader.class.getMethod("open", String.class);
            methodsToAdd.add(open);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("trader", scope, ef);
    }

    @Override
    public String getClassName() {
        return "Trader";
    }

    @Override
    public String getResourceId() {
        return "Trader";
    }
}
