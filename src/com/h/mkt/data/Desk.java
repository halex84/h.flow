package com.h.mkt.data;

import com.h.contexts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.SimpleTradingDesk;
import com.h.contexts.DomainContext;
import com.h.mkt.calc.MarginCalculator;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by halex on 10/22/16.
 */
public class Desk implements SimpleTradingDesk {

    public Desk (String id, Logger logger) {

        deskId  = id;
        log = logger;
        portfolios = new HashMap<>();
        portfolios.put("$", new Portfolio(logger, "$"));
    }

    private final String deskId;
    private final Logger log;
    private final Map<String, Portfolio> portfolios;

    public String getDeskId(){
        return deskId;
    }

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

        try {
            Portfolio portfolio = portfolios.get("$");
            EqTrade trade = new EqTrade(deskId, portfolio.getPortfolioId(), new Date(), ticker, qty, buySell, price);
            context.addEqTrade(trade);
        }
        catch (Exception e){

            log.append("%s experienced an error while booking a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                    new Object[] { deskId, ticker, qty, price, buySell });

            e.printStackTrace();
            throw e;
        }

        log.append("%s booked a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                new Object[]{deskId, ticker, qty, price, buySell});
    }

    public void onTradeAdding(DomainContext context, EqTrade trade) throws CalculationException {

        //ToDo encapsulate this method as an inner-domain event.
        String portfolioId = trade.getPortfolioId();
        if (!portfolios.containsKey(portfolioId)){
            throw  new CalculationException("unknown portfolio");
        }
        portfolios.get(portfolioId).addTrade(context, trade);
    }

    /**
     * Returns margin of the portfolio with the given id, or null if not found.
     */
    public PortfolioMargin getPortfolioMargin(DomainContext context, MarginCalculator calculator, String portfolioId) throws CalculationException {

        Portfolio portfolio;
        if (!portfolios.containsKey(portfolioId)) {
            return null;
        }
        else {
            portfolio = portfolios.get(portfolioId);
        }
        return portfolio.getPortfolioMargin(context, calculator);
    }
}