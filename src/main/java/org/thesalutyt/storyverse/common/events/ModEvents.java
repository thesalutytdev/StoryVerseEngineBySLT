package org.thesalutyt.storyverse.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.server.command.ConfigCommand;

import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.common.commands.CrashMyGame;
import org.thesalutyt.storyverse.common.commands.MainCommand;
import org.thesalutyt.storyverse.common.commands.home.ClearHome;
import org.thesalutyt.storyverse.common.commands.home.GetHomePos;
import org.thesalutyt.storyverse.common.commands.home.ReturnHome;
import org.thesalutyt.storyverse.common.commands.home.SetHome;
import org.thesalutyt.storyverse.common.commands.scripts.PlayerFuncsDebug;
import org.thesalutyt.storyverse.common.entities.npc.NPCRenderer;

import java.util.HashMap;
import java.util.UUID;

import static org.thesalutyt.storyverse.common.entities.Entities.NPC;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID)
public class ModEvents {
    public static ServerPlayerEntity player;
    public static EventManager eventManager = new EventManager();
    public static HashMap<UUID, Integer> fadeScreenTimers = new HashMap<>();
    public static HashMap<UUID, Integer> fadeScreenColors = new HashMap<>();

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MainCommand(event.getDispatcher());
        new SetHome(event.getDispatcher());
        new ReturnHome(event.getDispatcher());
        new GetHomePos(event.getDispatcher());
        new CrashMyGame(event.getDispatcher());
        new ClearHome(event.getDispatcher());
        new PlayerFuncsDebug(event.getDispatcher());

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
    public static void onWorldLeave(EntityLeaveWorldEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof PlayerEntity) {
            SVEngine.interpreter.close();
            System.out.println("[ModEvents::onWorldLeave] Interpreter closed");
        }
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(NPC.get(), NPCRenderer::new);
    }
}