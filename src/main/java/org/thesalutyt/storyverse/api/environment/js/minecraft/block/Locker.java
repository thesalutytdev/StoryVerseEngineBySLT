package org.thesalutyt.storyverse.api.environment.js.minecraft.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class Locker extends ScriptableObject implements EnvResource, JSResource {
    public static ArrayList<BlockPos> lockedBlocks = new ArrayList<>();

    public static void unlock(Double x, Double y, Double z) {
        lockedBlocks.remove(new BlockPos(x, y, z));
    }

    public static void lock(Double x, Double y, Double z) {
        lockedBlocks.add(new BlockPos(x, y, z));
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld() == null) {
            return;
        }
        if (lockedBlocks.contains(event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getWorld() == null) {
            return;
        }
        if (lockedBlocks.contains(event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onExplode(ExplosionEvent event) {
        lockedBlocks.forEach(blockPos -> {
            if (event.getExplosion().getToBlow().contains(blockPos)) {
                event.getExplosion().clearToBlow();
            }
        });
    }

    @SubscribeEvent
    public static void onPiston(PistonEvent.Pre event) {
        lockedBlocks.forEach(blockPos -> {
           if (event.getPos().equals(blockPos) || event.getDirection().getNormal().equals(new Vector3i(blockPos.getX(), blockPos.getY(), blockPos.getZ()))) {
               event.setCanceled(true);
           }
        });
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        Locker locker = new Locker();
        locker.setParentScope(scope);

        try {
            Method unlock = Locker.class.getMethod("unlock", Double.class, Double.class, Double.class);
            methodsToAdd.add(unlock);
            Method lock = Locker.class.getMethod("lock", Double.class, Double.class, Double.class);
            methodsToAdd.add(lock);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, locker);
            locker.put(m.getName(), locker, methodInstance);
        }
        scope.put("locker", scope, locker);
    }

    @Override
    public String getClassName() {
        return "Locker";
    }

    @Override
    public String getResourceId() {
        return "Locker";
    }
}
