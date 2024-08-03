package org.thesalutyt.storyverse.api.environment.js.cutscene.tick;

import net.minecraft.util.math.BlockPos;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneArguments;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneType;
import org.thesalutyt.storyverse.api.camera.cutscene.Moving;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.Time;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MovingJS extends ScriptableObject implements EnvResource, JSResource {
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

    public MovingJS newMoving(String player, Object time) {
        if (!(time instanceof Time.ITime)) {
            throw new RuntimeException("Time must be an instance of Time.ITime");
        }
        this.player = player;
        this.time = (Time.ITime) time;
        return this;
    }


    public MovingJS startPosition(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public MovingJS startRot(Double pith, Double yaw) {
        this.rotX = yaw;
        this.rotY = pith;
        return this;
    }

    public MovingJS finishPos(Double x, Double y, Double z) {
        this.finishX = x;
        this.finishY = y;
        this.finishZ = z;
        return this;
    }

    public MovingJS finishRot(Double pith, Double yaw) {
        this.finishRotX = pith;
        this.finishRotY = yaw;
        return this;
    }

    public MovingJS build() {
        this.arguments = new CutsceneArguments(CutsceneType.MOVING, new double[]{this.x, this.y, this.z},
                new Object[]{this.rotX, this.rotY});
        this.cutscene = new Moving(this.player, this.arguments,
                new BlockPos(this.finishX, this.finishY, this.finishZ), this.finishRotX, this.finishRotY, this.time);
        this.built = true;
        return this;
    }

    public MovingJS start() {
        if (!this.built) {
            Chat.sendError("Cutscene has not been built");
            return this;
        }
        this.cutscene.start();
        return this;
    }

    public MovingJS finish() {
        if (!this.built) {
            Chat.sendError("Cutscene has not been built");
            return this;
        }
        this.cutscene.finish();
        return this;
    }

    public MovingJS tick() {
        if (!this.built) {
            Chat.sendError("Cutscene has not been built");
            return this;
        }
        this.cutscene.tick();
        return this;
        }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        MovingJS ef = new MovingJS();
        ef.setParentScope(scope);

        try {
            Method create = MovingJS.class.getMethod("newMoving", String.class, Object.class);
            methodsToAdd.add(create);
            Method startPs = MovingJS.class.getMethod("startPosition", Double.class, Double.class, Double.class);
            methodsToAdd.add(startPs);
            Method startRot = MovingJS.class.getMethod("startRot", Double.class, Double.class);
            methodsToAdd.add(startRot);
            Method finishPos = MovingJS.class.getMethod("finishPos", Double.class, Double.class, Double.class);
            methodsToAdd.add(finishPos);
            Method finishRot = MovingJS.class.getMethod("finishRot", Double.class, Double.class);
            methodsToAdd.add(finishRot);
            Method build = MovingJS.class.getMethod("build");
            methodsToAdd.add(build);
            Method start = MovingJS.class.getMethod("start");
            methodsToAdd.add(start);
            Method finish = MovingJS.class.getMethod("finish");
            methodsToAdd.add(finish);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("moving", scope, ef);
    }

    @Override
    public String getClassName() {
        return "MovingJS";
    }

    @Override
    public String getResourceId() {
        return "MovingJS";
    }
}
