package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

public class Script implements EnvResource {
        public final Logger LOGGER = LogManager.getLogger("StoryVerse::Script");
        public final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        @Documentate(
            desc = "Freezes working thread"
        )
        public synchronized void waitTime(int time) {
            try {
                wait(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            notifyAll();
        }

        public synchronized void waitZero(){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Documentate(
                desc = "Freezes working thread before entity stops moving"
        )
        public synchronized void waitWalkEnd(WalkTask task){
            BlockPos pos = task.getPos();
            MobEntity entity = task.getEntity();
            MobController controller = task.getController();
            while (entity.getNavigation().isDone()) {
                StoryVerse.LOGGER.info(
                        pos.getX()+":"+entity.getX()+"\n"+
                                pos.getY()+":"+entity.getY()+"\n"+
                                pos.getZ()+":"+entity.getZ()+"\n"
                );
                waitZero();
            }
            notifyAll();
            controller.stopMove();
        }

    @Override
    public String getResourceId() {
        return "Script";
    }
}