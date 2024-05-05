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
                .then(Commands.literal("message").executes((command) -> { return message(command.getSource());}))
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
                                .then(Commands.literal("reset").executes((command) -> {{
                                return resetCamera(command.getSource());}
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

    private static int message(CommandSource source) throws CommandSyntaxException {
        Chat.sendMessage(source.getPlayerOrException(), "test");
        return 1;
    }
    private static int goUp(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        BlockPos position = player.blockPosition();
        WorldWrapper world = new WorldWrapper();

        world.setBlock(player.blockPosition().above(2), Blocks.DIAMOND_BLOCK);
        player.setPos(player.getX(), player.getY() + 2, player.getZ());
        source.sendSuccess(new StringTextComponent("Whoosh!"), true);
        return 1;
    }
    private static int testFeatures(CommandSource source) throws CommandSyntaxException {
        String playerName = Player.getPlayerName();
        Chat.sendNamed(source.getPlayerOrException(), "§bStory§aVerse", "Привет, " + playerName);
        return 1;
    }

    public static int controllerTest(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        new Thread(() -> {
            WorldWrapper world = new WorldWrapper();
            Script script = new Script();
            LegacyEventManager eventManager = new LegacyEventManager();
            MobController mob0 = new MobController(world.spawnEntity(player.blockPosition(),
                    EntityType.WITHER_SKELETON));
            mob0.canPickUpLoot(true);
            WalkTask walkTask = new WalkTask(player.blockPosition().north(2),
                    mob0.getMobEntity(),
                    mob0.getController());
            script.waitWalkEnd(walkTask);
        }).start();

        return 1; // ❄
    }
    public static int controllerTestNoAI(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        new Thread(() -> {
            WorldWrapper world = new WorldWrapper();
            Script script = new Script();
            MobController mob = new MobController(world.spawnEntity(player.blockPosition().east(2),
                    EntityType.BEE));
            mob.setNoAI(true);
        }).start();


        return 1;
    }
    public int moveCamera(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        new Thread(() -> {
            Camera camera = new Camera();
            WorldWrapper world = new WorldWrapper();

            MobController cameraMob = new MobController(
                    world.spawnEntity(
                            player.blockPosition(),
                            EntityType.PHANTOM
                    )
            );
            cameraMob.setInvisible(true);
            cameraMob.addEffect(Effects.INVISIBILITY,999999,99);
            cameraMob.setNoAI(false);

            player.setGameMode(GameType.SPECTATOR);
            camera.setCameraEntity(cameraMob.getEntity());
            cameraMob.moveTo(new BlockPos(player.getX() + 2,
                    player.getY() + 2,
                    player.getZ() + 2), 1f);
        }).start();
        return 1;
    }
    public int moveCameraNoAI(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        new Thread(() -> {
            Camera camera = new Camera();
            WorldWrapper world = new WorldWrapper();

            MobController cameraMob = new MobController(
                    world.spawnEntity(
                            player.blockPosition(),
                            EntityType.BAT
                    )
            );
            cameraMob.setInvisible(true);
            cameraMob.addEffect(Effects.INVISIBILITY,999999,99);
            cameraMob.setNoAI(true);

            player.setGameMode(GameType.SPECTATOR);
            camera.setCameraEntity(cameraMob.getEntity());
            cameraMob.moveTo(new BlockPos(player.getX() + 2,
                    player.getY() + 2,
                    player.getZ() + 2), 1f);
        }).start();
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
            cutscene.setHeadRotation(new float[] {10f, 10f});
            script.waitTime(5000);
            cutscene.exitCutscene();
        }).start();

        return 1;
    }

    public int resetCamera(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Cutscene cutscene = new Cutscene();

        cutscene.exitCutscene();

        return 1;
    }

    public int testFade(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        Player.showFadeScreen(player, 5000);

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

        Server.execute(player, "tp TheSALUTYT ~1 ~5 ~1");
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
        MobController mob = new MobController(world.spawnEntity(player.blockPosition(), EntityType.WITHER_SKELETON));

        return 1;
    }
    public int getBlockPos(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        Chat.sendAsEngine(player, String.valueOf(player.pick(100, 0.0f, true).getLocation()));

        return 1;
    }
}
