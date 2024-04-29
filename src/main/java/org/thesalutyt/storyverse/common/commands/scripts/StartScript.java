//package org.thesalutyt.storyverse.common.commands.scripts;
//
//import com.mojang.brigadier.CommandDispatcher;
//import net.minecraft.command.CommandSource;
//import net.minecraft.command.Commands;
//import org.thesalutyt.storyverse.SVEngine;
//import org.thesalutyt.storyverse.StoryVerse;
//import org.thesalutyt.storyverse.api.script.Start;
//
//public class StartScript {
//    public StartScript(CommandDispatcher<CommandSource> dispatcher){
//        dispatcher.register(Commands.literal("start")/*.then(Commands.literal("clear")*/.executes((command) -> {
//            return startScript(command.getSource());
//        })/*)*/);
//    }
//    private int startScript(CommandSource source) {
//        Start.start(SVEngine.SCRIPTS_PATH);
//        return 1;
//    }
//}
