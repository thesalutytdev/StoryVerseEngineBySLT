package org.thesalutyt.storyverse.api.screen.gui.character.trades;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.trader.Offers;
import org.thesalutyt.storyverse.api.environment.trader.TradeOffer;
import org.thesalutyt.storyverse.api.environment.trader.Trader;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.api.features.Sounds;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGui;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IGuiProperties;
import org.thesalutyt.storyverse.api.screen.gui.constructor.IWidgetList;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiItem;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiButton;
import org.thesalutyt.storyverse.api.screen.gui.elements.java.GuiEntity;
import org.thesalutyt.storyverse.utils.ItemUtils;

import java.util.ArrayList;

public class TradeGUI extends IGui {
    protected Trader trader;
    protected Offers offers;
    protected TradeOffer currentTrade;
    protected IWidgetList widgets = new IWidgetList();

    public TradeGUI(Trader trader) {
        super(new IGuiProperties().width(512)
                .height(256)
                .renderBackground(false)
                .closeOnEsc(true)
                .isPause(false)
        );

        this.trader = trader;
        this.offers = trader.offers;
    }

    @Override
    public void init() {
        try {
            super.init();
            LivingEntity entity = trader.entity;
            // 780 y
            // 40 x
            this.widgets = widgets.addEntity(new GuiEntity(entity, 100, 780, 80));

            ArrayList<GuiItem> itemsToRender = new ArrayList<>();
            ArrayList<GuiButton> tradeButtons = new ArrayList<>();

            double y = 40.0;
            for (TradeOffer o : offers.getOffers()) {
                if (o.isActive()) {
                    double x = 40.0;
                    int trId = 0;
                    tradeButtons.add(new GuiButton(
                            String.format("trade%d", trId),
                            "textures/gui/buttons/button_0.png",
                            ((Double) x + 50.0), ((Double) y), (Double) 40.0, (Double) 40.0,
                            "1", () -> {
                        Player.sendNamed("tradeTest", String.format("trade%d::%s",
                                trId,
                                o.trader.trade(o, Player.getPlayerName())));
                        if (o.trader.trade(o, Player.getPlayerName())) {
                            ItemUtils.takeStack(Player.getPlayer(), o.needFirst);
                            if (o.needSecond != null) {
                                ItemUtils.takeStack(Player.getPlayer(), o.needSecond);
                            }
                            Sounds.playSoundGlobal(SoundEvents.VILLAGER_TRADE,
                                    1.0f, 1.0f);
                        }
                    }));
                    for (ItemStack i : o.getStacks()) {
                        itemsToRender.add(new GuiItem(i, x, y));
                        x += 40.0;
                    }
                }
                y += 50.0;
            }

            itemsToRender.forEach((item) -> {
                this.widgets.addItem(item);
            });

            tradeButtons.forEach((button) -> {
                this.widgets.addButton(button);
            });

            setWidgets(this.widgets);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        super.tick();
    }
}