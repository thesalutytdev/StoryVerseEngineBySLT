package org.thesalutyt.storyverse.effekseer;

import Effekseer.swig.EffekseerBackendCore;
import org.thesalutyt.storyverse.effekseer.enums.DeviceType;

import java.util.Objects;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class Effekseer {
    static {
        Library.init();
    }

    private final EffekseerBackendCore core;

    public Effekseer() {
        core = new EffekseerBackendCore();
    }

    public void delete() {
        core.delete();
    }

    public static DeviceType getDevice() {
        return DeviceType.fromOrd(EffekseerBackendCore.GetDevice().swigValue());
    }

    public static boolean setupForOpenGL() {
        return EffekseerBackendCore.InitializeAsOpenGL();
    }

    public static void finish() {
        EffekseerBackendCore.Terminate();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Effekseer)) return false;
        return EffekseerBackendCore.getCPtr(core) == EffekseerBackendCore.getCPtr(((Effekseer) obj).core);
    }

    @Override
    public int hashCode() {
        return Objects.hash(core);
    }

    public EffekseerBackendCore unwrap() {
        return core;
    }

    public static void init(){}
}