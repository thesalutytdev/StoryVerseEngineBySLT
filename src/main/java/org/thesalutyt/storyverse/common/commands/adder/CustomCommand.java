package org.thesalutyt.storyverse.common.commands.adder;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.EntityArgument;

public class CustomCommand {
    private static final Minecraft mc = Minecraft.getInstance();
    public CustomCommand(CommandDispatcher<CommandSource> dispatcher) {
        if (CommandAdder.commands.isEmpty() || CommandAdder.commandsMap.isEmpty()) return;
        CommandAdder.commands.forEach(commandAdder -> {
            LiteralArgumentBuilder<CommandSource> command;

            if (commandAdder.nested.isEmpty()) {
                command = Commands.literal(commandAdder.name).executes((commandSource) -> {
                    return commandAdder.execute();
                });
            } else {
                command = Commands.literal(commandAdder.name).executes((commandSource) -> {
                    return commandAdder.execute();
                });
                for (CommandAdder i : commandAdder.nested) {
                    command.then(Commands.literal(i.name).executes((commandSource) -> {
                        return i.execute();
                    }));
                }
            }
            if (!commandAdder.nestedWithArg.isEmpty()) {
                command = Commands.literal(commandAdder.name).executes((commandSource) -> {
                    return commandAdder.execute();
                });
                for (CommandAdder.NestedWithArg i : commandAdder.nestedWithArgList) {
                    command.then(Commands.literal(i.name));
                    switch (i.type) {
                        case NUMBER:
                            command.then(Commands.argument(i.argName, IntegerArgumentType.integer()).executes((commandSource) -> {
                                return CommandAdder.executeNestedWithArg(commandAdder.name, i.name, IntegerArgumentType.getInteger(commandSource, i.name));
                            }));
                            break;
                        case ENTITY:
                            command.then(Commands.argument(i.argName, EntityArgument.entity()).executes((commandSource) -> {
                                return CommandAdder.executeNestedWithArg(commandAdder.name, i.name, EntityArgument.getEntity(commandSource, i.name));
                            }));
                            break;
                        case BOOLEAN:
                            command.then(Commands.argument(i.argName, BoolArgumentType.bool()).executes((commandSource) -> {
                                return CommandAdder.executeNestedWithArg(commandAdder.name, i.name, BoolArgumentType.getBool(commandSource, i.name));
                            }));
                            break;
                        case DIMENSION:
                            command.then(Commands.argument(i.argName, DimensionArgument.dimension()).executes((commandSource) -> {
                                return CommandAdder.executeNestedWithArg(commandAdder.name, i.name, DimensionArgument.getDimension(commandSource, i.name));
                            }));
                            break;
                        case STRING:
                        default:
                            command.then(Commands.argument(i.argName, StringArgumentType.string()).executes((commandSource) -> {
                                return CommandAdder.executeNestedWithArg(commandAdder.name, i.name, StringArgumentType.getString(commandSource, i.name));
                            }));
                            break;
                    }
                }
            }
            dispatcher.register(command);
        });
    }
}
