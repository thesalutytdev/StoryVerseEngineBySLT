package org.thesalutyt.storyverse.api.executers;

public class ScriptWrapper {
    private final ScriptType type;
    private final String className;
    public ScriptWrapper(ScriptType type, String className){
        this.type = type;
        this.className = className;
    }

    public ScriptType getType() {
        return type;
    }

    public String getClazz() {
        return className;
    }

    @Override
    public String toString() {
        return "ScriptWrapper{" +
                "type=" + type +
                ", clazz=" + className +
                '}';
    }
}

