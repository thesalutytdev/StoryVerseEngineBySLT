package org.thesalutyt.storyverse.api.environment.trader;

import java.util.ArrayList;

public class Offers {
    private final Trader trader;
    private ArrayList<TradeOffer> offers = new ArrayList<>();

    public Offers(Trader trader) {
        this.trader = trader;
    }

    public Offers addOffer(TradeOffer offer) {
        if (offer.trader != this.trader) {
            throw new RuntimeException("Offer trader does not match trader");
        }
        this.offers.add(offer);
        return this;
    }

    public Offers removeOffer(TradeOffer offer) {
        this.offers.remove(offer);
        return this;
    }

    public Offers clearOffers() {
        this.offers.clear();
        return this;
    }

    public Trader getTrader() {
        return this.trader;
    }

    public ArrayList<TradeOffer> getOffers() {
        return this.offers;
    }

    public TradeOffer getOffer(int index) {
        return this.offers.get(index);
    }
}
