package com.h.mkt.data;

import com.h.contexts.CalculationException;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by halex on 10/22/16.
 */
public class EqTrade {

    public EqTrade(String deskId, String portfolioId, Date timestamp, String ticker, int qty, BuySell buySell, BigDecimal price) throws CalculationException {
        this.deskId = deskId;
        this.portfolioId = portfolioId;
        this.ticker = ticker;
        this.qty = qty;
        this.buySell = buySell;
        this.price = price;
        this.timestamp = timestamp;
        if (timestamp == null){
            throw new CalculationException("timestamp");
        }
    }

    private final String deskId;
    private final String portfolioId;
    private final String ticker;
    private final Date timestamp;
    private final int qty;
    private final BuySell buySell;
    private final BigDecimal price;

    public String getDeskId() { return deskId; }
    public String getPortfolioId() { return portfolioId; }
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
    public int getLongShortPosition(){
        if (buySell.equals(BuySell.buy)){
            return qty;
        }
        else {
            return 0 - qty;
        }
    }
}