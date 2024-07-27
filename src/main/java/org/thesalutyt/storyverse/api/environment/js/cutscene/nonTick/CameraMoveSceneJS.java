package org.thesalutyt.storyverse.api.environment.js.cutscene.nonTick;

import net.minecraft.util.math.BlockPos;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneArguments;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneType;
import org.thesalutyt.storyverse.api.camera.cutscene.nonTicking.Moving;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Time;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CameraMoveSceneJS extends ScriptableObject implements EnvResource, JSResource {
    public String player;
    public double x;
    public double y;
    public double z;
    public double rotX;
    public double rotY;
    public double finishX;
    public double finishY;
    public double finishZ;
    public double finishRotX;
    public double finishRotY;
    public CutsceneArguments arguments;
    public boolean built = false;
    public Moving cutscene;
    public Time.ITime time;

    public CameraMoveSceneJS create(String playerName, Object time) {
        if (!(time instanceof Time.ITime)) {
            throw new IllegalArgumentException("Time must be an instance of Time.ITime");
        }
        this.time = (Time.ITime) time;
        this.player = playerName;
        return this;
    }

    public CameraMoveSceneJS setStart(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public CameraMoveSceneJS setStartRot(Double rotX, Double rotY) {
        this.rotX = rotX;
        this.rotY = rotY;
        return this;
    }

    public CameraMoveSceneJS setFinish(Double x, Double y, Double z) {
        this.finishX = x;
        this.finishY = y;
        this.finishZ = z;
        return this;
    }

    public CameraMoveSceneJS setFinishRot(Double rotX, Double rotY) {
        this.finishRotX = rotX;
        this.finishRotY = rotY;
        return this;
    }

    public CameraMoveSceneJS build() {
        this.arguments = new CutsceneArguments(CutsceneType.MOVING, new double[]{this.x, this.y, this.z},
                new Object[]{this.rotX, this.rotY});
        this.cutscene = new Moving(this.player, this.arguments,
                new BlockPos(this.finishX, this.finishY, this.finishZ), this.finishRotX, this.finishRotY, this.time);
        this.built = true;
        return this;
    }

    public CameraMoveSceneJS start() {
        if (!this.built) {
            throw new IllegalStateException("Must build before starting");
        }
        this.cutscene.start();
        return this;
    }

    public CameraMoveSceneJS finish() {
        if (!this.built) {
            throw new IllegalStateException("Must build before finishing");
        }
        this.cutscene.finish();
        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        CameraMoveSceneJS ef = new CameraMoveSceneJS();
        ef.setParentScope(scope);

        try {
            Method create = CameraMoveSceneJS.class.getMethod("create", String.class, Object.class);
            methodsToAdd.add(create);
            Method setStart = CameraMoveSceneJS.class.getMethod("setStart", Double.class, Double.class, Double.class);
            methodsToAdd.add(setStart);
            Method setStartRot = CameraMoveSceneJS.class.getMethod("setStartRot", Double.class, Double.class);
            methodsToAdd.add(setStartRot);
            Method setFinish = CameraMoveSceneJS.class.getMethod("setFinish", Double.class, Double.class, Double.class);
            methodsToAdd.add(setFinish);
            Method setFinishRot = CameraMoveSceneJS.class.getMethod("setFinishRot", Double.class, Double.class);
            methodsToAdd.add(setFinishRot);
            Method build = CameraMoveSceneJS.class.getMethod("build");
            methodsToAdd.add(build);
            Method start = CameraMoveSceneJS.class.getMethod("start");
            methodsToAdd.add(start);
            Method finish = CameraMoveSceneJS.class.getMethod("finish");
            methodsToAdd.add(finish);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("nonTickMoving", scope, ef);
    }

    @Override
    public String getClassName() {
        return "CameraMoveSceneJS";
    }

    @Override
    public String getResourceId() {
        return "CameraMoveSceneJS";
    }
}
