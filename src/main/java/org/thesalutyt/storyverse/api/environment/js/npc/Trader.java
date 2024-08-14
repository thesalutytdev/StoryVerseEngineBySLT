package org.thesalutyt.storyverse.api.environment.js.npc;

import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Trader extends ScriptableObject implements EnvResource, JSResource {
    public NPCEntity entity;

    public Trader() {
        super();
    }

    public Trader init(String mobId) {
        try {
            this.entity = (NPCEntity) MobJS.getMob(mobId).getEntity();
            return this;
        } catch (Exception e) {
            new ErrorPrinter(e);
            return null;
        }
    }

    public Trader showProgressBar(Boolean show) {
        this.entity.showProgressBar = show;
        return this;
    }

    public Trader addTrade(String costA, String costB, String saleItem, Integer maxUses, Integer xp) {
        this.entity.offers = new MerchantOffers();
        this.entity.offers.add(new MerchantOffer(JSItem.getStack(costA),
                JSItem.getStack(costB),
                JSItem.getStack(saleItem),
                0, maxUses, xp, 1f));
        if (SVEngine.IS_DEBUG) {
            System.out.println(this.entity.offers + "\n" + this.entity.offers.createTag());
        }
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
