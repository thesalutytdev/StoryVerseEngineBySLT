package org.thesalutyt.storyverse.api.environment.js.cutscene;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.camera.Camera;
import org.thesalutyt.storyverse.api.camera.CameraType;
import org.thesalutyt.storyverse.api.camera.Cutscene;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.WorldWrapper;
import org.thesalutyt.storyverse.common.entities.client.moveGoals.MoveGoal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class CutsceneJS extends ScriptableObject implements JSResource, EnvResource {
    private static final WorldWrapper worldWrapper = new WorldWrapper();
    private static MobController cameraMob;
    private static Cutscene cutscene;
    private static BlockPos firstCutscenePosition;
    private static Entity cameraEntity;
    public static void setCameraMob(String mobId) {
        MobController mob = MobJS.getMob(mobId);
        cameraMob = mob;
        Camera camera = new Camera().setCameraEntity(mob.getEntity());
    }
    public static void enter(String playerName, String mobId, Double x, Double y, Double z, Double xHeadRot, Double yHeadRot, String cameraType) {
        CameraType camType = worldWrapper.toCameraType(cameraType);
        BlockPos pos = worldWrapper.pos(x, y, z);
        MobController mob = MobJS.create(mobId + "_CSMOB", x, y, z, "SHEEP");
        cameraMob = mob;
        ServerPlayerEntity player = Server.getPlayerByName(playerName);
        cutscene = new Cutscene().enterCutscene(player, mob.getEntity().getType(), pos, camType);
        cutscene.getCameraController().getEntity().xRot = xHeadRot.floatValue();
        cutscene.getCameraController().getEntity().setYHeadRot(yHeadRot.floatValue());
        cutscene.getCameraController().getEntity().yRot = yHeadRot.floatValue();
    }
    public static void move(Double x, Double y, Double z, Integer speed) {
        if (cutscene == null) {
            return;
        } if (cutscene.getCameraType() != CameraType.FULL || cutscene.getCameraType() != CameraType.POS_ONLY || cutscene.getCameraType() != CameraType.ROT_ONLY) {
            return;
        } else {
            Objects.requireNonNull(getController()).setNoAI(false);
            CreatureEntity creatureEntity = (CreatureEntity) cameraEntity;
            MoveGoal moveGoal = new MoveGoal(creatureEntity, new BlockPos(x, y, z), speed);
            creatureEntity.goalSelector.getRunningGoals().forEach(prioritizedGoal -> {
                creatureEntity.goalSelector.removeGoal(prioritizedGoal.getGoal());
            });
            creatureEntity.goalSelector.addGoal(0, moveGoal);
        }
    }
    public static void stopMove() {
        if (cutscene == null) {
            return;
        } else {
            cutscene.stopMove();
        }
    }
    public static void setRotation(Double x, Double y) {
        if (cutscene == null) {
            return;
        } else {
            cutscene.setHeadRotation(x, y);
        }
    }
    public static MobController getController() {
        if (cutscene == null) {
            return null;
        } else {
            return cutscene.getCameraController();
        }
    }
    public static CameraType getCameraType() {
        if (cutscene == null) {
            return null;
        } else {
            return cutscene.getCameraType();
        }
    }
    public static void exit() {
        if (cutscene == null) {
            return;
        } else {
            cutscene.exitCutscene();
            cutscene = null;
            try {
                cameraMob.remove();
            } catch (Exception e) {
                Chat.sendError(e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        CutsceneJS ef = new CutsceneJS();
        ef.setParentScope(scope);

        try {
            Method enter = CutsceneJS.class.getMethod("enter", String.class, String.class, Double.class, Double.class, Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(enter);
            Method move = CutsceneJS.class.getMethod("move", Double.class, Double.class, Double.class, Integer.class);
            methodsToAdd.add(move);
            Method stopMove = CutsceneJS.class.getMethod("stopMove");
            methodsToAdd.add(stopMove);
            Method setRotation = CutsceneJS.class.getMethod("setRotation", Double.class, Double.class);
            methodsToAdd.add(setRotation);
            Method getController = CutsceneJS.class.getMethod("getController");
            methodsToAdd.add(getController);
            Method getCameraType = CutsceneJS.class.getMethod("getCameraType");
            methodsToAdd.add(getCameraType);
            Method exit = CutsceneJS.class.getMethod("exit");
            methodsToAdd.add(exit);
            Method setCameraMob = CutsceneJS.class.getMethod("setCameraMob", String.class);
            methodsToAdd.add(setCameraMob);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("cutscene", scope, ef);
    }
    @Override
    public String getClassName() {
        return "CutsceneJS";
    }

    @Override
    public String getResourceId() {
        return "CutsceneJS";
    }
}
