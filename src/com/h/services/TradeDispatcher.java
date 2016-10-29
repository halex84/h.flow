package com.h.services;

import com.h.contexts.CalculationException;
import com.h.contracts.TradeContract;
import com.h.logging.LogFactory;
import com.h.logging.Logger;
import com.h.mkt.data.Desk;
import com.h.contexts.EnvironmentContext;
import com.h.contexts.DomainContext;

/**
 * TradeDispatcher API
 * Created by halex on 10/22/16.
 */
public class TradeDispatcher {

    private final DomainContext context;

    public TradeDispatcher(DomainContext context){
        this.context = context;
    }

    /**
     * Books trades.
     */
    public void bookTrades(TradeContract[] trades) throws CalculationException {

        for (TradeContract trade : trades){
            bookTrade(trade);
        }
    }

    /**
     * Books a trade.
     */
    public void bookTrade(TradeContract trade) throws CalculationException {

        Desk tradeDesk = context.getOrAddDeskById(trade.deskId);
        switch (trade.buySell){
            case buy:
                tradeDesk.buy(context, trade.ticker, trade.qty, trade.price);
                break;
            case sell:
                tradeDesk.sell(context, trade.ticker, trade.qty, trade.price);
                break;
        }
    }
}