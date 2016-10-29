package com.h.services;

import com.h.contexts.CalculationException;
import com.h.contracts.TradeContract;
import com.h.mkt.data.Desk;
import com.h.contexts.EnvironmentContext;
import com.h.contexts.DomainContext;

/**
 * TradeDispatcher API
 * Created by halex on 10/22/16.
 */
public class TradeDispatcher {

    /**
     * Books a trade.
     */
    public static void bookTrade(TradeContract trade) throws CalculationException {

        DomainContext repository = EnvironmentContext.getInstance();
        Desk tradeDesk = repository.getOrAddDeskById(trade.deskId);
        switch (trade.buySell){
            case buy:
                tradeDesk.buy(repository, trade.ticker, trade.qty, trade.price);
                break;
            case sell:
                tradeDesk.sell(repository, trade.ticker, trade.qty, trade.price);
                break;
        }
    }
}