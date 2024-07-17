package org.thesalutyt.storyverse.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public class StoryUtils {
    public static void showTitle(PlayerEntity player, TextComponent title) {
        Minecraft.getInstance().gui.setTitles(title, null, 1, 1, 1);
    }

    public static void openTrade(PlayerEntity entity, ITextComponent component, MerchantOffers offers, int xp, boolean showProgressbar) {
        IMerchant merchant = new IMerchant() {
            @Override
            public void setTradingPlayer(@Nullable PlayerEntity p_70932_1_) {

            }

            @Nullable
            @Override
            public PlayerEntity getTradingPlayer() {
                return entity;
            }

            @Override
            public MerchantOffers getOffers() {
                return offers;
            }

            @Override
            public void overrideOffers(@Nullable MerchantOffers p_213703_1_) {

            }

            @Override
            public void notifyTrade(MerchantOffer p_213704_1_) {

            }

            @Override
            public void notifyTradeUpdated(ItemStack p_110297_1_) {

            }

            @Override
            public World getLevel() {
                return entity.level;
            }

            @Override
            public int getVillagerXp() {
                return xp;
            }

            @Override
            public void overrideXp(int p_213702_1_) {

            }

            @Override
            public boolean showProgressBar() {
                return showProgressbar;
            }

            @Override
            public SoundEvent getNotifyTradeSound() {
                return null;
            }
        };
        OptionalInt optionalint = entity.openMenu(new SimpleNamedContainerProvider((p_213701_1_, p_213701_2_, p_213701_3_) -> new MerchantContainer(p_213701_1_, p_213701_2_, merchant), component));
        if (optionalint.isPresent()) {
            MerchantOffers merchantoffers = merchant.getOffers();
            if (!merchantoffers.isEmpty()) {
                entity.sendMerchantOffers(optionalint.getAsInt(), merchantoffers, 1, merchant.getVillagerXp(), merchant.showProgressBar(), merchant.canRestock());
            }
        }
    }
}
