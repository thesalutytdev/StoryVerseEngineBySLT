package org.thesalutyt.storyverse.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.storyverse.common.entities.sit.SeatEntity;

public class AddSeat {
    public AddSeat(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("add-seat")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(source ->
                                addSeat(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos")))));
    }

    public static int addSeat(CommandSource source, BlockPos pos) {
        if (pos != null) {
            SeatEntity.addEntity(source.getLevel(), pos);
            return 1;
        }
        return 0;
    }
}
