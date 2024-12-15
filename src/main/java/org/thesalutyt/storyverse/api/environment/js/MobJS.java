package org.thesalutyt.storyverse.api.environment.js;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.wrappers.EntityData;
import org.thesalutyt.storyverse.api.environment.resource.wrappers.NPCData;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.WorldWrapper;
import org.thesalutyt.storyverse.api.quests.QuestManager;
import org.thesalutyt.storyverse.api.quests.item.ItemQuest;
import org.thesalutyt.storyverse.api.screen.gui.npc_settings.NpcSetter;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.adder.CustomEntity;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;
import org.thesalutyt.storyverse.common.items.EntityDeleter;
import org.thesalutyt.storyverse.common.items.NpcDeleter;
import org.thesalutyt.storyverse.common.items.NpcSettings;
import org.thesalutyt.storyverse.utils.StoryUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class MobJS extends ScriptableObject implements EnvResource {
    private static EventLoop eventLoop;
    public static HashMap<String, MobController> controllers = new HashMap<>();
    public static HashMap<UUID, String> mobNames = new HashMap<>();
    public static HashMap<String, EntityData> entityData = new HashMap<>();
    public static HashMap<String, NPCData> npcData = new HashMap<>();
    public static HashMap<MobController, HashMap<String, ArrayList<BaseFunction>>> events = new HashMap<>();
    private static HashMap<String, NPCEntity> npcs = new HashMap<>();

    public MobJS(EventLoop eventLoop) {
        MobJS.eventLoop = eventLoop;
    }

    public MobController addEventListener(String id, String mobId, BaseFunction function) {
        ArrayList<BaseFunction> functions = new ArrayList<>();
        functions.add(function);
        MobController mob = controllers.get(mobId);
        switch (id) {
            case "interact":
            case "kill":
            case "shift-interact":
            case "on-pickup":
            case "hurt":
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
        if (event.isCanceled()) {
            return;
        }
        System.out.println("Interacted with " + event.getTarget().getUUID() + " (" + event.getTarget().getType() + ")");
        // QuestManager.onMobInteract(event);
        if (event.getItemStack().getItem() instanceof NpcDeleter ||
                event.getItemStack().getItem() instanceof EntityDeleter) {

            if (event.getTarget() instanceof PlayerEntity) {
                if (!SVEngine.ALLOW_PLAYER_DELETING) return;
                event.getTarget().remove();
                String msg = "Player " +
                        event.getPlayer().getName().getContents() +
                        " deleted " +
                        event.getTarget().getName().getContents() +
                        " (" +
                        event.getTarget().getUUID() + ")";
                System.out.println(msg);

                if (SVEngine.SHOUT_PLAYER_DELETING) Chat.sendEveryone(msg);
            }


            if (event.getTarget() instanceof NPCEntity && !((NPCEntity) event.getTarget()).isDeadOrDying()
            && event.getItemStack().getItem() instanceof NpcDeleter) {
                event.getTarget().remove();
            } else if (event.getTarget() instanceof Entity && ((Entity) event.getTarget()).isAlive()
            && event.getItemStack().getItem() instanceof EntityDeleter) {
                event.getTarget().remove();
            }
            return;
        }
        if (event.getTarget() instanceof NPCEntity && !((NPCEntity) event.getTarget()).isDeadOrDying()
                && event.getItemStack().getItem() instanceof NpcSettings) {
            Minecraft.getInstance().setScreen(new NpcSetter((NPCEntity) event.getTarget()));
            return;
        }
        if (event.getTarget() instanceof NPCEntity && !((NPCEntity) event.getTarget()).isDeadOrDying()) {
            NPCEntity npc = (NPCEntity) event.getTarget();
            System.out.println("Trader: " + npc.traderName + " trades: " + npc.offers);
            if(event.getHand() == Hand.MAIN_HAND) {
                if (npc.offers != null && npc.isTrader) {
                    StoryUtils.openTrade((PlayerEntity) event.getPlayer(), new StringTextComponent(npc.traderName),
                            npc.offers, npc.getVillagerXp(), npc.showProgressBar);
                }
            }
        }
        if (QuestManager.itemQuests.containsKey(event.getTarget())) {
            ItemQuest quest = QuestManager.itemQuests.get(event.getTarget());
            quest.tryComplete(Server.getPlayerByName(event.getPlayer().getName().getContents()));
        }
        if (!event.getPlayer().isCrouching()) {
            runEvent(getMob(event.getTarget().getUUID()), "interact");
        } else if (event.getPlayer().isCrouching()) {
            runEvent(getMob(event.getTarget().getUUID()), "shift-interact");
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        runEvent(getMob(entity.getUUID()), "hurt");
        if (entity instanceof LivingEntity) {
            System.out.println("Player hurt " + entity.getUUID() + " (" + event.getEntityLiving().getType() + ")");
            runEvent(getMob(entity.getUUID()), "hurt");
        }
    }

    @SubscribeEvent
    public static void onKilled(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        runEvent(getMob(entity.getUUID()), "kill");

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
                   for (BaseFunction baseFunction : arr) {
                       baseFunction.call(ctx, SVEngine.interpreter.getScope(),
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
                    for (BaseFunction baseFunction : arr) {
                        baseFunction.call(ctx, SVEngine.interpreter.getScope(),
                                SVEngine.interpreter.getScope(), new Object[]{getMob(mobId)});
                    }
                }
            }
        });
    }
    public static void removeEventListener(String id, String mobId) {
        events.remove(getMob(mobId));
    }
    public static MobController create(String id, Double x, Double y, Double z, String type) {
        System.out.println("Started creating");
        MobController mob = new MobController(WorldWrapper.pos(x, y, z), WorldWrapper.toEntityType(type));
        System.out.println("Mob = " + mob);
        controllers.put(id, mob);
        mobNames.put(mob.getUUID(), id);
        System.out.println("Putting mob in base");

        EntityData entityData_ = new EntityData((LivingEntity) mob.getEntity(), x, y, z,
                new Object[]{});

        entityData.put(id, entityData_);

        mob.defineData(entityData_);

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

        EntityData entityData_ = new EntityData((LivingEntity) mob.getEntity(), name, x, y, z,
                new Object[]{});

        entityData_.setNameVisible(visible);

        entityData.put(id, entityData_);

        mob.defineData(entityData_);

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

        NPCData npcData_ = new NPCData(npc, name, x, y, z,
                new Object[]{npc.getModelPath(), npc.getAnimation(), npc.getTexturePath()});

        npcData_.setNameVisible(visible);

        EntityData entityData_ = new EntityData((LivingEntity) npc.getEntity(), name, x, y, z,
                npc.getModelPath(), npc.getAnimation(), npc.getTexturePath());

        entityData_.setNameVisible(visible);

        npcData.put(id, npcData_);
        entityData.put(id, entityData_);

        mob.defineNPCData(npcData_);
        mob.defineData(entityData_);

        npcs.put(id, ((NPCEntity) mob.getEntity()));
        ((NPCEntity) mob.getEntity()).setId(id);

        return mob;
    }

    public static MobController custom(String mobId, String entityId, Double x, Double y, Double z, String type) {
        MobController mob = new MobController(WorldWrapper.pos(x, y, z), WorldWrapper.toEntityType(type));
        controllers.put(mobId, mob);
        mobNames.put(mob.getUUID(), mobId);

        CustomEntity entity = (CustomEntity) mob.getEntity();
        entity.setId(entityId);

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

    public static MobController respawn(String id) {
        EntityData data = entityData.get(id);
        if (data == null) {
            return null;
        }

        if (data.entity == null) {
            return null;
        }

        return create(id, data.x, data.y, data.z, data.entity.getType().toString().toUpperCase(), data.name, data.name_visible)
                .defineData(data);
    }

    public static MobController respawnNpc(String id) {
        NPCData data = npcData.get(id);
        if (data == null) {
            return null;
        }

        if (data.entity == null) {
            return null;
        }

        NativeArray args = new NativeArray(new Object[]{data.texture, data.model, data.animations,
                ((double) data.entity.getSpeed()),
                data.npc.canPickup});

        return npc(id, data.x, data.y, data.z, data.name, data.name_visible, args)
                .defineData(data);
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
            Method respawn = MobJS.class.getMethod("respawn", String.class);
            methodsToAdd.add(respawn);
            Method respawnNpc = MobJS.class.getMethod("respawnNpc", String.class);
            methodsToAdd.add(respawnNpc);
            Method custom = MobJS.class.getMethod("custom", String.class, String.class, Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(custom);
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