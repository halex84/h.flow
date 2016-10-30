package com.h.contracts;

import com.h.mkt.data.BuySell;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by halex on 10/23/16.
 */
public class TradeContract {

    public TradeContract(String deskId, BuySell buySell, String ticker, int qty, BigDecimal price) {
        this(deskId, null, null, buySell, ticker, qty, price);
    }

    public TradeContract(String deskId, String portfolioId, Date timestamp, BuySell buySell, String ticker, int qty, BigDecimal price) {

        this.deskId = deskId;
        this.portfolioId = portfolioId;
        this.timestamp = timestamp;
        this.buySell = buySell;
        this.ticker = ticker;
        this.qty = qty;
        this.price = price;
    }

    public TradeContract() { /*JSON*/ }

    /**
     * Don't read this field value.
     * Use the getter.
     */
    public String deskId;

    /**
     * Don't read this field value.
     * Use the getter.
     */
    public String portfolioId;
    public BuySell buySell;
    public String ticker;
    public int qty;
    public BigDecimal price;
    public Date timestamp;

    public String getDeskId(){

        if(deskId == null || deskId.trim().equals("")){
            return  "$";
        }
        return deskId;
    }

    public String getPortfolioId(){

        if(portfolioId == null || portfolioId.trim().equals("")){
            return  "$";
        }
        return portfolioId;
    }
}