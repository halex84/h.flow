package com.h.mkt.data;

import com.h.contexts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.SimpleStockTradingDesk;
import com.h.contexts.DomainContext;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by halex on 10/22/16.
 */
public class Desk implements SimpleStockTradingDesk {

    public Desk (String id, Logger logger) {

        deskId  = id;
        log = logger;

        trades = new ArrayList<>();
    }

    private final String deskId;
    private final Logger log;

    private final ArrayList<EqTrade> trades;

    @Override
    public void buy(DomainContext context, String ticker, int qty, BigDecimal price) throws CalculationException {
        bookEqTrade(context, ticker, qty, price, BuySell.buy);
    }

    @Override
    public void sell(DomainContext context, String ticker, int qty, BigDecimal price) throws CalculationException {
        bookEqTrade(context, ticker, qty, price, BuySell.sell);
    }

    private void bookEqTrade(DomainContext context, String ticker, int qty, BigDecimal price, BuySell buySell) throws CalculationException {

        log.append("%s booking a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                new Object[]{deskId, ticker, qty, price, buySell});

        EqTrade trade = new EqTrade(deskId, ticker, qty, buySell, price);

        try {
            context.addEqTrade(trade);
        }
        catch (Exception e){

            log.append("%s experienced an error while booking a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                    new Object[] { deskId, ticker, qty, price, buySell });

            e.printStackTrace();
            throw e;
        }

        trades.add(trade);

        log.append("%s booked a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                new Object[]{deskId, ticker, qty, price, buySell});
    }
}