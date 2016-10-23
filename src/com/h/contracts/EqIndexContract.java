package com.h.contracts;

/**
 * Created by halex on 10/23/16.
 */
public class EqIndexContract {

    public EqIndexContract(String ticker, String[] componentTickers) {
        this.ticker = ticker;
        this.componentTickers = componentTickers;
    }

    public final String ticker;
    public final String[] componentTickers;
}