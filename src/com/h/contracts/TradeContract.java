package com.h.contracts;

import com.h.mkt.data.BuySell;
import java.math.BigDecimal;

/**
 * Created by halex on 10/23/16.
 */
public class TradeContract {

    public TradeContract(BuySell buySell, String ticker, int qty, BigDecimal price) {
        this("JPM-P", buySell, ticker, qty, price); //i need a cat scarf.
    }

    public TradeContract(String deskId, BuySell buySell, String ticker, int qty, BigDecimal price) {
        this.deskId = deskId;
        this.buySell = buySell;
        this.ticker = ticker;
        this.qty = qty;
        this.price = price;
    }

    public final String deskId;
    public final BuySell buySell;
    public final String ticker;
    public final int qty;
    public final BigDecimal price;
}