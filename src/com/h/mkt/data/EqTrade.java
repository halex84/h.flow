package com.h.mkt.data;

import com.sun.javaws.exceptions.InvalidArgumentException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by halex on 10/22/16.
 */
public class EqTrade {

    public EqTrade(String deskId, String ticker, int qty, BuySell buySell, BigDecimal price) throws InvalidArgumentException {

        this.deskId = deskId;
        this.ticker = ticker;
        this.qty = qty;
        this.buySell = buySell;
        this.price = price;
        this.timestamp = new Date();
    }

    private final String deskId;
    private final String ticker;
    private final Date timestamp;
    private final int qty;
    private final BuySell buySell;
    private final BigDecimal price;

    public String getDeskId() { return deskId; }
    public String getTicker(){ return ticker; }
    public Date getTimestamp(){
        return timestamp;
    }
    public int getQty(){
        return qty;
    }
    public BuySell getBuySell(){
        return buySell;
    }
    public BigDecimal getPrice(){
        return price;
    }
}