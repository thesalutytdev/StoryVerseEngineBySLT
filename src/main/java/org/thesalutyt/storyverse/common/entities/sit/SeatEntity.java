package org.thesalutyt.storyverse.common.entities.sit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.TransportationHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.entities.Entities;

import javax.annotation.Nullable;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SeatEntity extends Entity {
    public static HashMap<BlockPos, SeatEntity> seats = new HashMap<>();
    private int tick;
    private boolean hasPassenger;

    public SeatEntity(EntityType<?> type, World level) {
        super(type, level);
        this.setInvulnerable(true);
        this.noPhysics = true;
        this.noCulling = true;
        System.out.println("Added seat " + this.blockPosition());
        System.out.println(this.getX() + ", " + this.getY() + ", " + this.getZ());
    }

    public static SeatEntity addEntity(World level, BlockPos pos) {
        SeatEntity entity = new SeatEntity(Entities.SEAT.get(), level);
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        seats.put(pos, entity);
        return entity;
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos0 = event.getHitVec().getBlockPos();
        BlockPos pos1 = event.getPos();

        if (!seats.containsKey(pos0) && !seats.containsKey(pos1)) {
            return;
        }

        System.out.println("Interacted with seat " + pos0 + " and " + pos1);

        if (seats.containsKey(pos0) && !seats.get(pos0).hasPassenger()) {
            event.getPlayer().startRiding(seats.get(pos0));
        }
        if (seats.containsKey(pos1) && !seats.get(pos1).hasPassenger()) {
            event.getPlayer().startRiding(seats.get(pos1));
        }
    }

    private boolean hasPassenger() {
        return hasPassenger;
    }

    @Override
    public void tick() {
        this.tick++;

        super.tick();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT tag) {

    }

    @Override
    protected void addPassenger(Entity entity) {
        this.hasPassenger = true;
        super.addPassenger(entity);
    }

    @Override
    public void positionRider(Entity entity)
    {
        super.positionRider(entity);
        if (entity instanceof MobEntity) {
            MobEntity mobentity = (MobEntity)entity;
            this.yRot = mobentity.yBodyRot;
        } else {
            this.yRot = entity.yRot;
        }
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        super.onPassengerTurned(entity);
        if (entity instanceof MobEntity) {
            MobEntity mobentity = (MobEntity)entity;
            this.yRot = mobentity.yBodyRot;
        } else {
            this.yRot = entity.yRot;
        }
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        player.startRiding(this);
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        player.startRiding(this);
        return ActionResultType.SUCCESS;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    public Vector3d getDismountLocationForPassenger(LivingEntity entity)
    {
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};
        for(Direction dir : offsets)
        {
            Vector3d safeVec = TransportationHelper.findSafeDismountLocation(entity.getType(), this.level, this.blockPosition().relative(dir), false);
            if(safeVec != null)
            {
                return safeVec.add(0, 0.25, 0);
            }
        }
        return super.getDismountLocationForPassenger(entity);
    }

    public void removePassenger(Entity entity) {
        this.hasPassenger = false;
        super.removePassenger(entity);
        entity.stopRiding();
    }

    public void remove() {
        seats.remove(this.blockPosition());
        super.remove();
    }

    @Override
    public void kill() {
        seats.remove(this.blockPosition());
        super.kill();
    }

    @Override
    public double getPassengersRidingOffset()
    {
        return 0.0;
    }

    @Override
    protected boolean canRide(Entity entity)
    {
        return true;
    }

}
