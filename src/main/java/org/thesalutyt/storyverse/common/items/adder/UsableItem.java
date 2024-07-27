package org.thesalutyt.storyverse.common.items.adder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.common.items.ModItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

public class UsableItem extends Item {
    public String name;
    public Runnable onUse;
    public ArrayList<BaseFunction> onUseFunctions = new ArrayList<>();

    public UsableItem(String name, Properties properties) {
        super(properties);
        this.name = name;
        reg();
    }

    public UsableItem(String name, Properties properties, Runnable onUse) {
        super(properties);
        this.name = name;
        this.onUse = onUse;
        reg();
    }

    public UsableItem(String name, Properties properties, ArrayList<BaseFunction> onUseFunctions) {
        super(properties);
        this.name = name;
        this.onUseFunctions = onUseFunctions;
        reg();
    }

    private void reg() {
        ModItems.ITEMS.register(name, () -> this);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            return ActionResult.pass(player.getItemInHand(hand));
        }
        if (player.getItemInHand(hand).getItem() == this ||
                player.getItemInHand(hand).getItem() instanceof UsableItem) {
            if (onUse != null) {
                onUse.run();
            }
            if (onUseFunctions != null) {
                EventLoop.getLoopInstance().runImmediate(() -> {
                    for (BaseFunction function : onUseFunctions) {
                        function.call(Context.getCurrentContext(), SVEngine.modInterpreter.getScope(),
                                SVEngine.modInterpreter.getScope(), new Object[]{player.getName().getContents()});
                    }
                });
            }
        }
        return ActionResult.consume(player.getItemInHand(hand));
    }
}
