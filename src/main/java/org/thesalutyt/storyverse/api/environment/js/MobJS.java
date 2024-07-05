package org.thesalutyt.storyverse.api.environment.js;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.api.features.WorldWrapper;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class MobJS extends ScriptableObject implements EnvResource {
    private static EventLoop eventLoop;
    public MobJS(EventLoop eventLoop) {
        MobJS.eventLoop = eventLoop;
    }
    public static HashMap<String, MobController> controllers = new HashMap<>();
    public static HashMap<UUID, String> mobNames = new HashMap<>();
    public static HashMap<MobController, HashMap<String, ArrayList<BaseFunction>>> events = new HashMap<>();
    public MobController addEventListener(String id, String mobId, BaseFunction function) {
        ArrayList<BaseFunction> functions = new ArrayList<>();
        functions.add(function);
        MobController mob = controllers.get(mobId);
        switch (id) {
            case "interact":
            case "kill":
                eventLoop.runImmediate(() -> {
                    if (!events.containsKey(mob)) {
                        HashMap<String, ArrayList<BaseFunction>> interactEvent = new HashMap<>();
                        interactEvent.put(id, functions);
                        events.put(mob, interactEvent);
                    }
                });
                break;
            default:
                break;
        }
        return controllers.get(mobId);
    }
    @SubscribeEvent
    public static void onMobInteract(PlayerInteractEvent.EntityInteract event) {
        System.out.println("Interacted with " + event.getTarget().getUUID() + " (" + event.getTarget().getType() + ")");
        runEvent(getMob(event.getTarget().getUUID()), "interact");
    }
    @SubscribeEvent
    public static void onKilled(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!Objects.equals(event.getSource(), DamageSource.playerAttack(Player.getPlayer()))) {
            System.out.printf("%s(%s, %s) killed %s with this damage source: %s%n",
                    event.getEntityLiving().getType(), event.getEntityLiving().getUUID(),
                    event.getEntityLiving(), event.getSource().getEntity(),
                    event.getSource());
            runEvent(getMob(entity.getUUID()), "kill");
            return;
        }
        if (entity instanceof LivingEntity) {
            System.out.println("Player killed " + entity.getUUID() + " (" + event.getEntityLiving().getType() + ")");
            runEvent(getMob(entity.getUUID()), "kill");
        }
    }
    public static void runEvent(MobController mob, String id) {
        EventLoop.getLoopInstance().runImmediate(() -> {
           if (events.containsKey(mob)) {
               if (events.get(mob).containsKey(id)) {
                   ArrayList<BaseFunction> arr = events.get(mob).get(id);
                   Context ctx = Context.getCurrentContext();
                   for (int i=0;i<arr.size(); i++) {
                       arr.get(i).call(ctx, SVEngine.interpreter.getScope(),
                               SVEngine.interpreter.getScope(), new Object[]{mob});
                   }
               }
           }
        });
    }
    public static void runEvent(String mobId, String id) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            if (events.containsKey(getMob(mobId))) {
                if (events.get(getMob(mobId)).containsKey(id)) {
                    ArrayList<BaseFunction> arr = events.get(getMob(mobId)).get(id);
                    Context ctx = Context.getCurrentContext();
                    for (int i=0;i<arr.size(); i++) {
                        arr.get(i).call(ctx, SVEngine.interpreter.getScope(),
                                SVEngine.interpreter.getScope(), new Object[]{getMob(mobId)});
                    }
                }
            }
        });
    }
    public static void removeEventListener(String id, String mobId) {
        if (!Objects.equals(id, "interact") && !Objects.equals(id, "kill")) {
            return;
        } else {
            events.remove(getMob(mobId));
        }
    }
    public static MobController create(String id, Double x, Double y, Double z, String type) {
        System.out.println("Started creating");
        MobController mob = new MobController(WorldWrapper.pos(x, y, z), WorldWrapper.toEntityType(type));
        System.out.println("Mob = " + mob);
        controllers.put(id, mob);
        mobNames.put(mob.getUUID(), id);
        System.out.println("Putting mob in base");
        return mob;
    }
    public static MobController create(String id, Double x, Double y, Double z, String type, String name, Boolean visible) {
        System.out.println("Started creating");
        MobController mob = new MobController(WorldWrapper.pos(x, y, z), WorldWrapper.toEntityType(type));
        System.out.println("Mob = " + mob);
        controllers.put(id, mob);
        mobNames.put(mob.getUUID(), id);
        System.out.println("Putting mob in base");
        mob.setName(name);
        mob.setNameVisible(visible);
        return mob;
    }
    public static MobController npc(String id, Double x, Double y, Double z, String name, Boolean visible,
                                    NativeArray npcArgs) {
        System.out.println("Started creating npc");
        MobController mob = new MobController(WorldWrapper.pos(x, y, z), Entities.NPC.get());
        System.out.println("npc = " + mob);
        controllers.put(id, mob);
        mobNames.put(mob.getUUID(), id);
        System.out.println("Putting npc in base");
        mob.setName(name);
        mob.setNameVisible(visible);
        Object[] args = npcArgs.toArray(new Object[0]);
        NPCEntity npc = (NPCEntity) mob.getEntity();
        if (args.length > 0) {
            if (args[0] != null && args[0] instanceof String) {
                npc.setTexturePath((String) args[0]);
            }
            if (args[1] != null && args[1] instanceof String) {
                npc.setModelPath((String) args[1]);
            }
            if (args[2] != null && args[2] instanceof String) {
                npc.setAnimationPath((String) args[2]);
            }
            if (args[3] != null && args[3] instanceof Integer) {
                npc.setSpeed(((Integer) args[3]).floatValue());
            }
            if (args[4] != null && args[4] instanceof Boolean) {
                npc.canPickup = (Boolean) args[4];
            }
        }
        return mob;
    }
    public static MobController create(String id, Double x, Double y, Double z, NativeArray npcArgs) {
        Object[] args = npcArgs.toArray(new Object[0]);
        MobController mob = new MobController(WorldWrapper.pos(x, y, z), WorldWrapper.toEntityType("NPC"));
        controllers.put(id, mob);
        mobNames.put(mob.getUUID(), id);
        return mob;
    }
    public static MobController getMob(String id) {
        return controllers.get(id);
    }
    public static MobController getMob(UUID id) {
        return controllers.get(mobNames.get(id));
    }
    public static Entity mob(String id) {
        return controllers.get(id).getEntity();
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        MobJS ef = new MobJS(EventLoop.getLoopInstance());
        ef.setParentScope(scope);

        try {
            Method create = MobJS.class.getMethod("create", String.class, Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(create);
            Method getMob = MobJS.class.getMethod("getMob", String.class);
            methodsToAdd.add(getMob);
            Method mob = MobJS.class.getMethod("mob", String.class);
            methodsToAdd.add(mob);
            Method createWP = MobJS.class.getMethod("create",
                    String.class,
                    Double.class,
                    Double.class,
                    Double.class,
                    String.class,
                    String.class,
                    Boolean.class);
            methodsToAdd.add(createWP);
            Method addEventListener = MobJS.class.getMethod("addEventListener",
                    String.class, String.class, BaseFunction.class);
            methodsToAdd.add(addEventListener);
            Method runEvent = MobJS.class.getMethod("runEvent", String.class, String.class);
            methodsToAdd.add(runEvent);
            Method removeEventListener = MobJS.class.getMethod("removeEventListener", String.class, String.class);
            methodsToAdd.add(removeEventListener);
            Method npc = MobJS.class.getMethod("npc",
                    String.class, Double.class, Double.class,
                    Double.class, String.class, Boolean.class, NativeArray.class);
            methodsToAdd.add(npc);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("entity", scope, ef);
    }

    @Override
    public String getClassName() {
        return "MobJS";
    }

    @Override
    public String getResourceId() {
        return "MobJS";
    }
}
