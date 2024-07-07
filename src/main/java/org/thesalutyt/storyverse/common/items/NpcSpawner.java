package org.thesalutyt.storyverse.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.thesalutyt.storyverse.api.features.WorldWrapper;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.npc.NPCEntity;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;

import javax.annotation.ParametersAreNonnullByDefault;

public class NpcSpawner extends Item {
    public NpcSpawner() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                .tab(ModCreativeTabs.ENGINE_TAB).fireResistant()
                .setNoRepair());
    }
    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            return ActionResult.pass(player.getItemInHand(hand));
        }
        if (player.getItemInHand(hand).getItem() == this ||
                player.getItemInHand(hand).getItem() instanceof NpcSpawner) {
            WorldWrapper wrapper = new WorldWrapper(world);
            Vector3d result = player.pick(100, 0.0f, true).getLocation();
            double x = result.x;
            double y = result.y;
            double z = result.z;
            Entity entity = wrapper.spawnEntity(new BlockPos(x, y, z), Entities.NPC.get());

            if (entity instanceof NPCEntity) {
                return ActionResult.success(player.getItemInHand(hand));
            } else {
                return ActionResult.success(player.getItemInHand(hand));
            }
        }
        return ActionResult.pass(player.getItemInHand(hand));
    }
}
