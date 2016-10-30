package com.h.mkt.data;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainContext;
import com.h.logging.Logger;
import com.h.mkt.calc.MarginCalculator;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by halex on 10/30/16.
 */
public class Portfolio {

    private final String portfolioId;
    private final Logger log;
    private final Lock tradeLock = new ReentrantLock();
    private final Map<String, List<EqTrade>> trades = new HashMap<>();

    private boolean isMarginValid;
    private PortfolioMargin margin;

    public Portfolio(Logger log, String id) {

        this.portfolioId = id;
        this.log = log;

        margin = new PortfolioMargin(id, BigDecimal.ZERO);
        isMarginValid = true;
    }

    public String getPortfolioId(){

        return portfolioId;
    }

    public void invalidatePortfolioMargin(){

        isMarginValid = false;
        log.append("Invalidated portfolio margin calculation.");
    }

    public PortfolioMargin getPortfolioMargin(DomainContext context, MarginCalculator calculator) throws CalculationException {

        if (isMarginValid){
            return margin;
        }
        margin = calculator.calculate(this, context);
        isMarginValid = true;
        return margin;
    }

    public List<EqTrade> getTrades(){

        tradeLock.lock();
        try {
            return trades.values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        finally {
            tradeLock.unlock();
        }
    }

    /**
     * Returns the total net amount of stock held, long or short.
     */
    public Map<String, Integer> getNetEquityPositions(){

        tradeLock.lock();
        try {
            Map<String, Integer> netPerTicker = new HashMap<>();
            for (String key : trades.keySet()){
                int sum = 0;
                List<EqTrade> kt = trades.get(key);
                for (EqTrade t : kt){
                    sum += t.getLongShortPosition();
                }
                if (sum != 0){
                    netPerTicker.put(key, sum);
                }
            }
            return netPerTicker;
        }
        finally {
            tradeLock.unlock();
        }
    }

    public void addTrade(DomainContext context, EqTrade trade){

        log.append("Portfolio adding trade.");
        tradeLock.lock();
        try {
            List<EqTrade> tickerTradeList;
            if (trades.containsKey(trade.getTicker())) {
                tickerTradeList = trades.get(trade.getTicker());
            } else {
                tickerTradeList = new ArrayList<>();
                trades.put(trade.getTicker(), tickerTradeList);
            }
            tickerTradeList.add(trade);
            invalidatePortfolioMargin();
        }
        finally {
            tradeLock.unlock();
            log.append("Portfolio added trade.");
        }
    }
}