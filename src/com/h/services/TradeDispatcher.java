package com.h.services;

import com.h.contexts.CalculationException;
import com.h.contracts.TradeContract;
import com.h.logging.LogFactory;
import com.h.logging.Logger;
import com.h.mkt.data.Desk;
import com.h.contexts.EnvironmentContext;
import com.h.contexts.DomainContext;
import com.h.mkt.data.EqTrade;

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
     * Exclusively for trade import, doesn't go through desk booking;
     * minding the timestamp. Maintenance tbd. Import tickers before.
     */
    public void bookTrades(TradeContract[] trades) throws CalculationException {

        for (TradeContract trade : trades){

            Desk desk = context.getOrAddDeskById(trade.getDeskId());
            EqTrade eqTrade = new EqTrade(
                    desk.getDeskId(), "$", trade.timestamp, trade.ticker, trade.qty, trade.buySell, trade.price);

            try {
                //we book directly to the context because of the timestamp.
                //the desk is always to book trades with the current timestamp.
                context.addEqTrade(eqTrade);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Books a trade.
     */
    public void bookTrade(TradeContract trade) throws CalculationException {

        Desk tradeDesk = context.getOrAddDeskById(trade.getDeskId());
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