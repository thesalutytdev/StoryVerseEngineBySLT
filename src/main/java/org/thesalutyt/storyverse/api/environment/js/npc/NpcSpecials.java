package org.thesalutyt.storyverse.api.environment.js.npc;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.WorldWrapper;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class NpcSpecials extends ScriptableObject implements EnvResource, JSResource {
    public static void stop(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setAnimation("");
    }
    public static void emote(String npcId, String emote) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setEmote(emote);
    }

    public static void anim(String npcId, String emote) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setAnimation(emote);
    }

    public static void setCanPickup(String npcId, Boolean canPickup) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.canPickup = canPickup;
    }
    public static String getEmote(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getEmote();
    }
    public static String getAnimation(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getAnimation();
    }
    public static String getWalkAnim(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getWalkAnim();
    }
    public static String getIdleAnim(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getIdleAnim();
    }
    public static void setWalkAnim(String npcId, String animation) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setWalkAnim(animation);
    }
    public static void setIdleAnim(String npcId, String animation) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setIdleAnim(animation);
    }
    public static int getId(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getId();
    }
    public static void setId(String npcId, Integer id) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setId(id);
    }
    public static void focus(String npcId, String focusedEntity) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.focusedEntity = MobJS.getMob(focusedEntity).getEntity();
    }
    public static void unFocus(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.focusedEntity = null;
    }
    public static void focusOnPlayer(String npcId, String playerName) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.focusedEntity = Server.getPlayerByName(playerName);
    }
    public static void setSleep(String npcId, Boolean sleeping) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setSleep(sleeping);
    }
    public static void clearInventory(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
    }
    public static void setMove(String npcId, Boolean moving) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setMove(moving);
    }
    public static void setHeadRotation(String npcId, Double yaw) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.yRot = yaw.floatValue();
        npc.rotY = (double) yaw.floatValue();
    }
    public static void setRotation(String npcId, Double rotation) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.xRot = rotation.floatValue();
    }
    public static void holdItem(String npcId, Integer hand, String item) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.hold(WorldWrapper.selectHand(hand), JSItem.getStack(item));
    }
    public static void armor(String npcId, String slot, String item) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.armor(WorldWrapper.armorSlot(slot), JSItem.getStack(item));
    }
    public static void armor(String npcId, Integer slot, String item) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.armor(slot, JSItem.getStack(item));
    }
    public static boolean useItem(String npcId, Integer hand) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        ActResult res = npc.useItem(WorldWrapper.selectHand(hand));
        return res == ActResult.SUCCESS;
    }
    public static void setNoDie(String npcId, Boolean noDie) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.isAttackable = noDie;
    }
    public static boolean getNoDie(String npcId) {
        NPCEntity attackable = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return attackable.isAttackable();
    }
    public static ItemStack getLastItemStackPicked(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getLastItemPicked();
    }
    public static String getLastItemPicked(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getLastItemPicked().toString();
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        NpcSpecials ef = new NpcSpecials();
        ef.setParentScope(scope);

        try {
            Method emote = NpcSpecials.class.getMethod("emote", String.class, String.class);
            methodsToAdd.add(emote);
            Method anim = NpcSpecials.class.getMethod("anim", String.class, String.class);
            methodsToAdd.add(anim);
            Method stop = NpcSpecials.class.getMethod("stop", String.class);
            methodsToAdd.add(stop);
            Method setCanPickup = NpcSpecials.class.getMethod("setCanPickup", String.class, Boolean.class);
            methodsToAdd.add(setCanPickup);
            Method getEmote = NpcSpecials.class.getMethod("getEmote", String.class);
            methodsToAdd.add(getEmote);
            Method getAnimation = NpcSpecials.class.getMethod("getAnimation", String.class);
            methodsToAdd.add(getAnimation);
            Method getWalkAnim = NpcSpecials.class.getMethod("getWalkAnim", String.class);
            methodsToAdd.add(getWalkAnim);
            Method getIdleAnim = NpcSpecials.class.getMethod("getIdleAnim", String.class);
            methodsToAdd.add(getIdleAnim);
            Method setIdleAnim = NpcSpecials.class.getMethod("setIdleAnim", String.class, String.class);
            methodsToAdd.add(setIdleAnim);
            Method setId = NpcSpecials.class.getMethod("setId", String.class, Integer.class);
            methodsToAdd.add(setId);
            Method getId = NpcSpecials.class.getMethod("getId", String.class);
            methodsToAdd.add(getId);
            Method focus = NpcSpecials.class.getMethod("focus", String.class, String.class);
            methodsToAdd.add(focus);
            Method unFocus = NpcSpecials.class.getMethod("unFocus", String.class);
            methodsToAdd.add(unFocus);
            Method setSleep = NpcSpecials.class.getMethod("setSleep", String.class, Boolean.class);
            methodsToAdd.add(setSleep);
            Method setMove = NpcSpecials.class.getMethod("setMove", String.class, Boolean.class);
            methodsToAdd.add(setMove);
            Method setWalkAnim = NpcSpecials.class.getMethod("setWalkAnim", String.class, String.class);
            methodsToAdd.add(setWalkAnim);
            Method setHeadRotation = NpcSpecials.class.getMethod("setHeadRotation", String.class, Double.class);
            methodsToAdd.add(setHeadRotation);
            Method setRotation = NpcSpecials.class.getMethod("setRotation", String.class, Double.class);
            methodsToAdd.add(setRotation);
            Method focusOnPlayer = NpcSpecials.class.getMethod("focusOnPlayer", String.class, String.class);
            methodsToAdd.add(focusOnPlayer);
            Method hold = NpcSpecials.class.getMethod("holdItem", String.class, Integer.class, String.class);
            methodsToAdd.add(hold);
            Method armor = NpcSpecials.class.getMethod("armor", String.class, String.class, String.class);
            methodsToAdd.add(armor);
            Method armor_ = NpcSpecials.class.getMethod("armor", String.class, Integer.class, String.class);
            methodsToAdd.add(armor_);
            Method useItem = NpcSpecials.class.getMethod("useItem", String.class, Integer.class);
            methodsToAdd.add(useItem);
            Method setNoDie = NpcSpecials.class.getMethod("setNoDie", String.class, Boolean.class);
            methodsToAdd.add(setNoDie);
            Method getNoDie = NpcSpecials.class.getMethod("getNoDie", String.class);
            methodsToAdd.add(getNoDie);
            Method getLastItemPicked = NpcSpecials.class.getMethod("getLastItemPicked", String.class);
            methodsToAdd.add(getLastItemPicked);
            Method getLastItem = NpcSpecials.class.getMethod("getLastItemStackPicked", String.class);
            methodsToAdd.add(getLastItem);
            Method clearInventory = NpcSpecials.class.getMethod("clearInventory", String.class);
            methodsToAdd.add(clearInventory);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("npc", scope, ef);
    }

    @Override
    public String getClassName() {
        return "NpcSpecials";
    }

    @Override
    public String getResourceId() {
        return "NpcSpecials";
    }
}
