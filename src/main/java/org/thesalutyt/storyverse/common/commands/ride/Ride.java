package org.thesalutyt.storyverse.common.commands.ride;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;

public class Ride {
    public Ride(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("ride")
                .then(Commands.argument("entity", EntityArgument.entity()).executes((command) -> {
                    Entity entity = EntityArgument.getEntity(command, "entity");
                    return ride(command.getSource(), entity);
                })));
    }

    public static int ride(CommandSource source, Entity entity) throws CommandSyntaxException {
        source.getPlayerOrException().startRiding(entity, true);
        return 0;
    }
}
