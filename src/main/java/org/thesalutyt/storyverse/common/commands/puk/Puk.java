package org.thesalutyt.storyverse.common.commands.puk;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class Puk {
    public Puk(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("puk").executes((command) -> {return puk(command.getSource());}));
    }
    public static int puk(CommandSource source) {
        source.sendSuccess(new StringTextComponent("text.storyverse.puk"), true);
        return 1;
    }
}
