package org.thesalutyt.storyverse.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import org.mozilla.javascript.Scriptable;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.camera.Camera;
import org.thesalutyt.storyverse.api.camera.CameraType;
import org.thesalutyt.storyverse.api.camera.Cutscene;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.api.environment.resource.script.Scripts;
import org.thesalutyt.storyverse.api.features.*;
import org.thesalutyt.storyverse.api.special.FadeScreen;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;

import org.mozilla.javascript.Context;
import java.io.FileNotFoundException;

public class MainCommand {
    Context context = Context.enter();
    Scriptable scope = context.initStandardObjects();
    public MainCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("storyverse")
                                .then(Commands.literal("blockpos")
                                        .executes((command) -> {
                                            return getBlockPos(command.getSource());
                                        }))
                .then(Commands.literal("up").executes((command) -> {return goUp(command.getSource());}))
                .then(Commands.literal("test")
                        .then(Commands.literal("action_packet")
                                .executes((command) -> {
                                    return actionPacketTest(command.getSource());
                                }))
                        .then(Commands.literal("command_executor")
                                .executes((command) -> {
                                    return commandExecutor(command.getSource());
                                }))
                        .then(Commands.literal("camera")
                                .then(Commands.literal("move")
                                        .then(Commands.literal("noAI").
                                                executes((command) -> {
                                                    return moveCameraNoAI(command.getSource());}))
                                        .executes((command) -> {
                                    return moveCamera(command.getSource());
                                }))
                                .executes((command) -> {return cameraTest(command.getSource());}))
                        .then(Commands.literal("controller")
                                .then(Commands.literal("noAI")
                                        .executes((command) -> {return controllerTestNoAI(command.getSource());}))
                                .executes((command) -> {return controllerTest(command.getSource());}))
                        .then(Commands.literal("fadeScreen").executes((command) -> {
                            return testFade(command.getSource());
                                })
                                .then(Commands.literal("color").executes((command) -> {
                                    return testFadeColor(command.getSource());
                                })))
                        .executes((command) -> {return engineTest(command.getSource());}))
        );
    }
    private static int goUp(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        BlockPos position = player.blockPosition();
        WorldWrapper world = new WorldWrapper();

        world.setBlock(player.blockPosition().above(2), Blocks.DIAMOND_BLOCK);
        player.setPos(player.getX(), player.getY() + 2, player.getZ());
        Player.teleportTo(player.getX(), player.getY() + 2, player.getZ());
        source.sendSuccess(new StringTextComponent("Whoosh!"), true);
        return 1;
    }
    public static int controllerTest(CommandSource source) throws CommandSyntaxException {

        return 1; // ❄
    }
    public static int controllerTestNoAI(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        return 1;
    }
    public int moveCamera(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        return 1;
    }
    public int moveCameraNoAI(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        return 1;
    }

    public int cameraTest(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        new Thread(() -> {
            Cutscene cutscene = new Cutscene();
            Script script = new Script();

            cutscene.enterCutscene(player, EntityType.BAT, player.blockPosition().above(2), CameraType.MOVING);
            script.waitTime(5000);
            cutscene.move(player.blockPosition().above(2).north(4), 1);
            script.waitTime(5000);
            cutscene.setHeadRotation(new Double[] {10.00, 10.00});
            script.waitTime(5000);
            cutscene.exitCutscene();
        }).start();

        return 1;
    }

    public int testFade(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        FadeScreen fadeScreen = new FadeScreen();

        return 1;
    }
    public int testFadeColor(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        Player.showFadeScreen(player, 5000, "2FE1ED");

        return 1;
    }

    public int commandExecutor(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Server server = new Server();

        Server.execute(player, "Command executor works fine :D");
        Server.execute(player, "/tp TheSALUTYT ~1 ~5 ~1");

        return 1;
    }

    public int openGui(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Server server = new Server();




        return 1;
    }

    public int engineTest(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Server server = new Server();
        WorldWrapper world = new WorldWrapper();
        Chat chat = new Chat();
        Script script = new Script();
        Sounds sounds = new Sounds();
        Interpreter interpreter = new Interpreter(SVEngine.SCRIPTS_PATH);
        interpreter.executeString("ExternalFunctions.import_file(\"script.js\")");

        return 1;
    }

    public int actionPacketTest(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        WorldWrapper world = new WorldWrapper();
        MobController mob = new MobController(player.blockPosition(), EntityType.WITHER_SKELETON);

        return 1;
    }
    public int getBlockPos(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        Chat.sendAsEngine(String.valueOf(player.pick(100, 0.0f, true).getLocation()));

        return 1;
    }
}
