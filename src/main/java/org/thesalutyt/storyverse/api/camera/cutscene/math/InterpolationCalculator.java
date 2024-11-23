package org.thesalutyt.storyverse.api.camera.cutscene.math;

import net.minecraft.util.math.BlockPos;
import org.thesalutyt.storyverse.api.features.Time.ITime;

public class InterpolationCalculator {
    public static Vector3d interpolate(Vector3d from, Vector3d to, ITime time) {
        return new Vector3d(calculate(from.x, to.x, time), calculate(from.y, to.y, time), calculate(from.z, to.z, time));
    }

    public static double interpolate(double from, double to, ITime time) {
        return calculate(from, to, time);
    }

    public static double calculate(double from, double to, ITime time) {
        return (to - from) / time.ticks;
    }

    public static double calculateMS(double from, double to, ITime time) {
        return (to - from) / time.milliSeconds;
    }

    public static Vector3d interpolateNonTicking(Vector3d from, Vector3d to, ITime time) {
        return new Vector3d(interpolateNonTicking(from.x, to.x, time), interpolateNonTicking(from.y, to.y, time), interpolateNonTicking(from.z, to.z, time));
    }

    public static double interpolateNonTicking(double from, double to, ITime time) {
        return (to - from) / time.cs_ticks;
    }

    public static class Vector3d {
        public double x;
        public double y;
        public double z;
        public BlockPos pos;

        public Vector3d(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;

            this.pos = new BlockPos(x, y, z);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public BlockPos getPos() {
            return pos;
        }
    }
}
