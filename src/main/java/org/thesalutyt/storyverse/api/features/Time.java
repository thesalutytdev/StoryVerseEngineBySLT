package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Time extends ScriptableObject implements EnvResource, JSResource {
    public static void freeze() {

    }

    public static class ITime extends ScriptableObject implements EnvResource, JSResource {
        public double cs_ticks;
        public double milliSeconds;
        public double seconds;
        public int ticks;
        public double minutes;
        public double hours;

        public ITime(double milliSeconds) {
            this.milliSeconds = milliSeconds;
            this.seconds = milliSeconds / 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
        }

        public ITime(int ticks) {
            this.ticks = ticks;
            this.milliSeconds = ticks * 50;
            this.seconds = milliSeconds / 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
        }

        private ITime() {}

        public ITime milliSeconds(Double milliSeconds) {
            this.milliSeconds = milliSeconds;
            this.seconds = milliSeconds / 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
            return this;
        }

        public ITime seconds(Double seconds) {
            this.seconds = seconds;
            this.milliSeconds = seconds * 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
            return this;
        }

        public ITime ticks(Integer ticks) {
            this.ticks = ticks;
            this.milliSeconds = ticks * 50;
            this.seconds = milliSeconds / 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
            return this;
        }

        public ITime minutes(Double minutes) {
            this.minutes = minutes;
            this.seconds = minutes * 60;
            this.milliSeconds = seconds * 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
            return this;
        }

        public ITime hours(Double hours) {
            this.hours = hours;
            this.minutes = hours * 60;
            this.seconds = minutes * 60;
            this.milliSeconds = seconds * 1000;
            this.cs_ticks = (int) (seconds * 60);
            this.ticks = (int) (seconds * 20);
            this.minutes = seconds / 60;
            this.hours = minutes / 60;
            return this;
        }

        public ITime ms(Double ms) {
            return milliSeconds(ms);
        }

        public ITime sec(Double s) {
            return seconds(s);
        }

        public ITime tick(Integer ticks) {
            return ticks(ticks);
        }

        public ITime min(Double m) {
            return minutes(m);
        }

        public ITime hour(Double h) {
            return hours(h);
        }

        public Double getMilliSeconds() {
            return milliSeconds;
        }

        public Double getSeconds() {
            return seconds;
        }

        public Integer getTicks() {
            return ticks;
        }

        public Double getMinutes() {
            return minutes;
        }

        public Double getHours() {
            return hours;
        }

        public static ArrayList<Method> methodsToAdd = new ArrayList<>();

        public static void putIntoScope(Scriptable scope) {
            ITime ef = new ITime(0);
            ef.setParentScope(scope);

            try {
                Method seconds = ITime.class.getMethod("seconds", Double.class);
                methodsToAdd.add(seconds);
                Method ticks = ITime.class.getMethod("ticks", Integer.class);
                methodsToAdd.add(ticks);
                Method minutes = ITime.class.getMethod("minutes", Double.class);
                methodsToAdd.add(minutes);
                Method hours = ITime.class.getMethod("hours", Double.class);
                methodsToAdd.add(hours);
                Method milliSeconds = ITime.class.getMethod("milliSeconds", Double.class);
                methodsToAdd.add(milliSeconds);
                Method ms = ITime.class.getMethod("ms", Double.class);
                methodsToAdd.add(ms);
                Method sec = ITime.class.getMethod("sec", Double.class);
                methodsToAdd.add(sec);
                Method tick = ITime.class.getMethod("tick", Integer.class);
                methodsToAdd.add(tick);
                Method min = ITime.class.getMethod("min", Double.class);
                methodsToAdd.add(min);
                Method hour = ITime.class.getMethod("hour", Double.class);
                methodsToAdd.add(hour);
                Method getMilliSeconds = ITime.class.getMethod("getMilliSeconds");
                methodsToAdd.add(getMilliSeconds);
                Method getSeconds = ITime.class.getMethod("getSeconds");
                methodsToAdd.add(getSeconds);
                Method getTicks = ITime.class.getMethod("getTicks");
                methodsToAdd.add(getTicks);
                Method getMinutes = ITime.class.getMethod("getMinutes");
                methodsToAdd.add(getMinutes);
                Method getHours = ITime.class.getMethod("getHours");
                methodsToAdd.add(getHours);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            for (Method m : methodsToAdd) {
                FunctionObject methodInstance = new FunctionObject(m.getName(),
                        m, ef);
                ef.put(m.getName(), ef, methodInstance);
            }
            scope.put("time", scope, ef);
        }

        @Override
        public String getClassName() {
            return "ITime";
        }

        @Override
        public String getResourceId() {
            return "ITime";
        }
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public String getResourceId() {
        return null;
    }
}
