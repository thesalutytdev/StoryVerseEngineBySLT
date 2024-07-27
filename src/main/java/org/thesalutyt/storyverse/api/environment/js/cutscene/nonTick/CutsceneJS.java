package org.thesalutyt.storyverse.api.environment.js.cutscene.nonTick;

import net.minecraft.util.math.BlockPos;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.camera.cutscene.nonTicking.Cutscene;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneArguments;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneType;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.features.Time;
import org.thesalutyt.storyverse.utils.ErrorPrinter;
import org.thesalutyt.storyverse.utils.ItemUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CutsceneJS extends ScriptableObject implements EnvResource, JSResource {
    public String player;
    public double x;
    public double y;
    public double z;
    public double rotX;
    public double rotY;
    public CutsceneType type;
    public CutsceneArguments arguments;
    public boolean built = false;
    public Time.ITime time;
    public Cutscene cutscene;

    public CutsceneJS newCutscene(String player, Object time) {
        if (! (time instanceof Time.ITime)) {
            throw new IllegalArgumentException("Time must be an instance of Time.ITime");
        }
        this.player = player;
        this.time = (Time.ITime) time;
        return this;
    }

    public CutsceneJS setType(String type) {
        this.type = toCutsceneType(type);
        return this;
    }

    public CutsceneJS setPosition(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public CutsceneJS setRotation(Double rotX, Double rotY) {
        this.rotX = rotX;
        this.rotY = rotY;
        return this;
    }

    public CutsceneJS build() {
        switch (this.type) {
            case FULL: {
                this.arguments = new CutsceneArguments(this.type, new double[]{this.x, this.y, this.z}, new Object[]{rotX, rotY});
                break;
            }
            case POS_ONLY: {
                this.arguments = new CutsceneArguments(this.type, new double[]{this.x, this.y, this.z}, null);
                break;
            }
            case ROT_ONLY: {
                this.arguments = new CutsceneArguments(this.type, null, new Object[]{rotX, rotY});
                break;
            }
            default:
            case NULL: {
                this.arguments = new CutsceneArguments(this.type, null, null);
                break;
            }
        }
        this.cutscene = new Cutscene(this.player, this.arguments, time);
        this.built = true;
        return this;
    }

    public Cutscene getCutscene() {
        return this.cutscene;
    }

    public CutsceneJS start() {
        if (!built) {
            Chat.sendError("Cutscene has not been built yet");
            return this;
        }
        cutscene.start();
        return this;
    }

    public CutsceneJS finish() {
        if (!built) {
            Chat.sendError("Cutscene has not been built yet");
            return this;
        }
        cutscene.finish();
        return this;
    }

    public CutsceneJS setFinishGameMode(String mode) {
        cutscene.finishMode = ItemUtils.toGameMode(mode);
        return this;
    }

    public CutsceneJS setFinishGameMode(Integer mode) {
        cutscene.finishMode = ItemUtils.toGameMode(mode);
        return this;
    }

    public CutsceneJS setFinishPos(Double x, Double y, Double z) {
        cutscene.finishPos = new BlockPos(x, y, z);
        return this;
    }

    public static CutsceneType toCutsceneType(int type) {
        switch (type) {
            case 0: {
                return CutsceneType.FULL;
            }
            case 1: {
                return CutsceneType.POS_ONLY;
            }
            case 2: {
                return CutsceneType.ROT_ONLY;
            }
            case 3: {
                return CutsceneType.NULL;
            }
        }
        return null;
    }

    public static CutsceneType toCutsceneType(String type) {
        switch (type.toUpperCase()) {
            case "FULL": {
                return CutsceneType.FULL;
            }
            case "POS_ONLY":
            case "POS": {
                return CutsceneType.POS_ONLY;
            }
            case "ROT_ONLY":
            case "ROT": {
                return CutsceneType.ROT_ONLY;
            }
            case "NULL": {
                return CutsceneType.NULL;
            }
        }
        return null;
    }

//    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
//    public static void putIntoScope(Scriptable scope) {
//        CutsceneJS ef = new CutsceneJS();
//        ef.setParentScope(scope);
//        try {
//            Method newCutscene = CutsceneJS.class.getMethod("newCutscene", String.class, Object.class);
//            methodsToAdd.add(newCutscene);
//            Method setType =CutsceneJS.class.getMethod("setType", String.class);
//            methodsToAdd.add(setType);
//            Method setPosition =CutsceneJS.class.getMethod("setPosition", Double.class, Double.class, Double.class);
//            methodsToAdd.add(setPosition);
//            Method setRotation = CutsceneJS.class.getMethod("setRotation", Double.class, Double.class);
//            methodsToAdd.add(setRotation);
//            Method getCutscene = CutsceneJS.class.getMethod("getCutscene");
//            methodsToAdd.add(getCutscene);
//            Method start = CutsceneJS.class.getMethod("start");
//            methodsToAdd.add(start);
//            Method finish = CutsceneJS.class.getMethod("finish");
//            methodsToAdd.add(finish);
//            Method build = CutsceneJS.class.getMethod("build");
//            methodsToAdd.add(build);
//            Method toCutsceneType = CutsceneJS.class.getMethod("toCutsceneType", String.class);
//            methodsToAdd.add(toCutsceneType);
//            Method setFinishGameMode = CutsceneJS.class.getMethod("setFinishGameMode", String.class);
//            methodsToAdd.add(setFinishGameMode);
//            Method setFinishGameMode2 = CutsceneJS.class.getMethod("setFinishGameMode", Integer.class);
//            methodsToAdd.add(setFinishGameMode2);
//            Method setFinishPos = CutsceneJS.class.getMethod("setFinishPos", Double.class, Double.class, Double.class);
//            methodsToAdd.add(setFinishPos);
//        } catch (NoSuchMethodException e) {
//            new ErrorPrinter(e);
//        }
//        for (Method m : methodsToAdd) {
//            FunctionObject methodInstance = new FunctionObject(m.getName(),
//                    m, ef);
//            ef.put(m.getName(), ef, methodInstance);
//        }
//
//        scope.put("cutscene", scope, ef);
//    }

    @Override
    public String getClassName() {
        return "CutsceneJS NOT WORKING";
    }

    @Override
    public String getResourceId() {
        return "CutsceneJS NOT WORKING";
    }
}
