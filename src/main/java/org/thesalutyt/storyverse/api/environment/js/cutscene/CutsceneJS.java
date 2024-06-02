package org.thesalutyt.storyverse.api.environment.js.cutscene;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.camera.CameraType;
import org.thesalutyt.storyverse.api.camera.Cutscene;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.MobController;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.features.WorldWrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class CutsceneJS extends ScriptableObject implements JSResource, EnvResource {
    private static final WorldWrapper worldWrapper = new WorldWrapper();
    private static Cutscene cutscene;
    public static void enter(String playerName, String mobId, Double x, Double y, Double z, String cameraType) {
        CameraType camType = worldWrapper.toCameraType(cameraType);
        BlockPos pos = worldWrapper.pos(x, y, z);
        MobController mob = MobJS.controllers.get(mobId);
        ServerPlayerEntity player = Server.getPlayerByName(playerName);
        cutscene = new Cutscene().enterCutscene(player, mob.getEntity().getType(), pos, camType);
    }
    public static void move(Double x, Double y, Double z, Integer speed) {
        if (cutscene == null) {
            return;
        } if (cutscene.getCameraType() != CameraType.FULL || cutscene.getCameraType() != CameraType.POS_ONLY || cutscene.getCameraType() != CameraType.ROT_ONLY) {
            return;
        } else {
            Objects.requireNonNull(getController()).setNoAI(false);
            cutscene.move(worldWrapper.pos(x, y, z), speed);
            getController().setNoAI(true);
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
        }
    }
    public static void putIntoScope(Scriptable scope) {
        CutsceneJS ef = new CutsceneJS();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method enter = CutsceneJS.class.getMethod("enter", String.class, String.class, Double.class, Double.class, Double.class, String.class);
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
