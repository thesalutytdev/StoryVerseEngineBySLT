package org.thesalutyt.storyverse.common.commands.adder;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.events.ModEvents;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandAdder extends ScriptableObject implements EnvResource, JSResource {
    public static ArrayList<CommandAdder> commands = new ArrayList<>();
    public static HashMap<String, CommandAdder> commandsMap = new HashMap<>();
    public String name;
    public boolean isNested = false;
    public ArrayList<BaseFunction> onExecute = new ArrayList<>();
    public ArrayList<CommandAdder> nested = new ArrayList<>();
    public HashMap<String, CommandAdder> nestedMap = new HashMap<>();
    public HashMap<String, NestedWithArg> nestedWithArg = new HashMap<>();
    public ArrayList<NestedWithArg> nestedWithArgList = new ArrayList<>();

    public CommandAdder createGeneralCommand(String name, BaseFunction onExecute) {
        CommandAdder command = new CommandAdder();
        EventLoop.getLoopInstance().runImmediate(() -> {
            command.name = name;
            command.onExecute.add(onExecute);
            commands.add(command);
            commandsMap.put(name, command);
        });
        return command;
    }

    public CommandAdder addNested(String generalName, String name, BaseFunction onExecute) {
        CommandAdder command = new CommandAdder();
        EventLoop.getLoopInstance().runImmediate(() -> {
            command.name = name;
            command.onExecute.add(onExecute);
            commandsMap.get(generalName).nested.add(command);
            commandsMap.get(generalName).nestedMap.put(name, command);
            command.isNested = true;
            commandsMap.put(name, command);
        });
        return command;
    }

    public int execute() {
        if (!ModEvents.inWorld) return 0;
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction i : onExecute) {
                i.call(Context.getCurrentContext(),
                        SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{});
            }
        });
        return 1;
    }

    public static int executeNested(String general, String nest) {
        if (!ModEvents.inWorld) return 0;
        EventLoop.getLoopInstance().runImmediate(() -> {
            CommandAdder gen = CommandAdder.commandsMap.get(general);
            CommandAdder nested = gen.nestedMap.get(nest);
            if (!nested.isNested) throw new RuntimeException("Command " + nest + " is not a nested command of " + general);
            for (BaseFunction i : nested.onExecute) {
                i.call(Context.getCurrentContext(),
                        SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{});
            }
        });
        return 1;
    }

    public CommandAdder addNestedWithArg(String general, String name, String argType, String argName, BaseFunction onExecute) {
        CommandAdder command = new CommandAdder();
        NestedWithArg nwa = new NestedWithArg(command, ArgType.fromString(argType), name).setArgName(argName);
        EventLoop.getLoopInstance().runImmediate(() -> {
            command.name = name;
            commandsMap.get(general).nestedWithArg.put(name, nwa);
            command.isNested = true;
            commandsMap.put(name, command);
            commandsMap.get(general).nestedWithArgList.add(nwa);
            command.onExecute.add(onExecute);
        });
        return command;
    }

    public static int executeNestedWithArg(String general, String name, Object argValue) {
        if (!ModEvents.inWorld) return 0;
        EventLoop.getLoopInstance().runImmediate(() -> {
            CommandAdder gen = CommandAdder.commandsMap.get(general);
            NestedWithArg nwa = gen.nestedWithArg.get(name);
            if (!nwa.command.isNested) throw new RuntimeException("Command " + name + " is not a nested command of " + general);
            for (BaseFunction i : nwa.command.onExecute) {
                i.call(Context.getCurrentContext(),
                        SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{argValue});
            }
        });
        return 1;
    }

    public static class NestedWithArg {
        public CommandAdder command;
        public ArgType type;
        public String name;
        public String argName;

        public NestedWithArg(CommandAdder command, ArgType type, String name) {
            this.command = command;
            this.type = type;
            this.name = name;
            this.command.isNested = true;
        }

        public NestedWithArg setArgName(String name) {
            this.argName = name;
            return this;
        }
    }

    public static enum ArgType {
        STRING,
        NUMBER,
        BOOLEAN,
        ENTITY,
        DIMENSION;

        public static ArgType fromString(String s) {
            switch (s.toLowerCase()) {
                case "string":
                    return STRING;
                case "number":
                    return NUMBER;
                case "boolean":
                    return BOOLEAN;
                case "entity":
                    return ENTITY;
                case "dimension":
                    return DIMENSION;
                default:
                    return null;
            }
        }
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        CommandAdder c = new CommandAdder();
        c.setParentScope(scope);

        try {
            Method create = CommandAdder.class.getMethod("createGeneralCommand", String.class, BaseFunction.class);
            methodsToAdd.add(create);
            Method addNested = CommandAdder.class.getMethod("addNested", String.class, String.class, BaseFunction.class);
            methodsToAdd.add(addNested);
            Method execute = CommandAdder.class.getMethod("execute");
            methodsToAdd.add(execute);
            Method executeNested = CommandAdder.class.getMethod("executeNested", String.class, String.class);
            methodsToAdd.add(executeNested);
            Method addNestedWithArg = CommandAdder.class.getMethod("addNestedWithArg", String.class, String.class, String.class, String.class, BaseFunction.class);
            methodsToAdd.add(addNestedWithArg);
            Method executeNestedWithArg = CommandAdder.class.getMethod("executeNestedWithArg", String.class, String.class, Object.class);
            methodsToAdd.add(executeNestedWithArg);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, c);
            c.put(m.getName(), c, methodInstance);
        }

        scope.put("command", scope, c);
    }

    @Override
    public String getClassName() {
        return "CommandAdder";
    }

    @Override
    public String getResourceId() {
        return "CommandAdder";
    }
}
