package com.h.contracts;

import com.h.mkt.data.StockType;

/**
 * Created by halex on 10/23/16.
 */
public class EqStockContract {

    public EqStockContract(String ticker, double lastDividend) {

        this.ticker = ticker;
        this.lastDividend = lastDividend;

        stockType = StockType.common;
    }

    public EqStockContract(String ticker, double parValue, double parValueFixedDividendPct) {
        this.ticker = ticker;
        this.parValue = parValue;
        this.parValueFixedDividendPct = parValueFixedDividendPct;

        stockType = StockType.preferred;
    }

    public EqStockContract() { /*JSON*/ }

    public String ticker;
    public StockType stockType;
    public double lastDividend;
    public double parValue;
    public double parValueFixedDividendPct;
}