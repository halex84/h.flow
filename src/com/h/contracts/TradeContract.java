package com.h.contracts;

import com.h.mkt.data.BuySell;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by halex on 10/23/16.
 */
public class TradeContract {

    public TradeContract(BuySell buySell, String ticker, int qty, BigDecimal price) {
        this("JPM-P", buySell, ticker, qty, price);
    }

    public TradeContract(String deskId, BuySell buySell, String ticker, int qty, BigDecimal price) {
        this(deskId, null, buySell, ticker, qty, price);
    }

    public TradeContract(String deskId, Date timestamp, BuySell buySell, String ticker, int qty, BigDecimal price) {
        this.deskId = deskId;
        this.timestamp = timestamp;
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
    public final Date timestamp;
}