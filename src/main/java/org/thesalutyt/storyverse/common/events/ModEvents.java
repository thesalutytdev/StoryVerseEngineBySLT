package org.thesalutyt.storyverse.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.server.command.ConfigCommand;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.api.environment.js.event.EventManagerJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.common.commands.AddSeat;
import org.thesalutyt.storyverse.common.commands.CrashMyGame;
import org.thesalutyt.storyverse.common.commands.MainCommand;
import org.thesalutyt.storyverse.common.commands.adder.CustomCommand;
import org.thesalutyt.storyverse.common.commands.home.ClearHome;
import org.thesalutyt.storyverse.common.commands.home.GetHomePos;
import org.thesalutyt.storyverse.common.commands.home.ReturnHome;
import org.thesalutyt.storyverse.common.commands.home.SetHome;
import org.thesalutyt.storyverse.common.commands.ride.Ride;
import org.thesalutyt.storyverse.common.commands.scripts.GetAll;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class ModEvents {
    public static boolean inWorld = false;

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MainCommand(event.getDispatcher());
        new SetHome(event.getDispatcher());
        new ReturnHome(event.getDispatcher());
        new GetHomePos(event.getDispatcher());
        new CrashMyGame(event.getDispatcher());
        new ClearHome(event.getDispatcher());
        new GetAll(event.getDispatcher());
        new CustomCommand(event.getDispatcher());
        new Ride(event.getDispatcher());
        new AddSeat(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event){
        if(!event.isWasDeath()){
            event.getPlayer().getPersistentData().putIntArray(StoryVerse.MOD_ID + "homepos",
                    event.getOriginal().getPersistentData().getIntArray(StoryVerse.MOD_ID + "homepos"));
        }
    }
    @SubscribeEvent
    public static void onJoined (PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.isCanceled() && event.getEntity() instanceof PlayerEntity) {
            ModEvents.inWorld = true;
            if (SVEngine.interpreter == null) {
                SVEngine.interpreter = new Interpreter(SVEngine.SCRIPTS_PATH);
                System.out.println("[ModEvents::onJoined] Created new interpreter");
                System.out.println(Minecraft.getInstance().cameraEntity);
                SVEnvironment.Root.playerJoined(event);
            }
        } else {
            return;
        }
    }
    @SubscribeEvent
    public static void onServerStopping(FMLServerStoppingEvent event) {
        if (event.isCanceled()) {
            return;
        } else {
            EventManagerJS.events.clear();
            inWorld = false;
        }
    }
    @SubscribeEvent
    public static void worldUnloaded(WorldEvent.Unload event) {
        if (!event.getWorld().isClientSide()) {
            EventManagerJS.events.clear();
            inWorld = false;
            SVEnvironment.Root.playerLeft(event);
        }
    }
}