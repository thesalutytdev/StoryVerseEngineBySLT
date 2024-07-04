package org.thesalutyt.storyverse.api.environment.js.npc;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class NpcSpecials extends ScriptableObject implements EnvResource, JSResource {
    public static void play_once(String npcId, String animation) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setOnceAnim(animation);
    }
    public static void play_looped(String npcId, String animation) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setLoopAnim(animation);
    }
    public static void stop(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setLoopAnim("");
        npc.setOnceAnim("");
        npc.setEmote("");
    }
    public static void set_emote(String npcId, String emote) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setEmote(emote);
    }
    public static void play(String npcId, String animation, Integer mode) {
        switch (mode) {
            case 0:
                play_once(npcId, animation);
                break;
            case 1:
                play_looped(npcId, animation);
                break;
            default:
                break;
        }
    }
    public static void setCanPickup(String npcId, Boolean canPickup) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.canPickup = canPickup;
    }
    public static String getEmote(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getEmote();
    }
    public static String getOnceAnim(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getOnceAnim();
    }
    public static String getLoopAnim(String npcId) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        return npc.getLoopAnim();
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
    public static void setSleep(String npcId, Boolean sleeping) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setSleep(sleeping);
    }
    public static void setMove(String npcId, Boolean moving) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.setMove(moving);
    }
    public static void setHeadRotation(String npcId, Double yaw) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.yRot = yaw.floatValue();
    }
    public static void setRotation(String npcId, Double rotation) {
        NPCEntity npc = (NPCEntity) MobJS.getMob(npcId).getEntity();
        npc.xRot = rotation.floatValue();
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        NpcSpecials ef = new NpcSpecials();
        ef.setParentScope(scope);

        try {
            Method playOnce = NpcSpecials.class.getMethod("play_once", String.class, String.class);
            methodsToAdd.add(playOnce);
            Method playLooped = NpcSpecials.class.getMethod("play_looped", String.class, String.class);
            methodsToAdd.add(playLooped);
            Method stop = NpcSpecials.class.getMethod("stop", String.class);
            methodsToAdd.add(stop);
            Method setEmote = NpcSpecials.class.getMethod("set_emote", String.class, String.class);
            methodsToAdd.add(setEmote);
            Method setCanPickup = NpcSpecials.class.getMethod("setCanPickup", String.class, Boolean.class);
            methodsToAdd.add(setCanPickup);
            Method getEmote = NpcSpecials.class.getMethod("getEmote", String.class);
            methodsToAdd.add(getEmote);
            Method getOnceAnim = NpcSpecials.class.getMethod("getOnceAnim", String.class);
            methodsToAdd.add(getOnceAnim);
            Method getLoopAnim = NpcSpecials.class.getMethod("getLoopAnim", String.class);
            methodsToAdd.add(getLoopAnim);
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
            Method play = NpcSpecials.class.getMethod("play", String.class, String.class, Integer.class);
            methodsToAdd.add(play);
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
