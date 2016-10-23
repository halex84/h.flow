package com.h.mkt.data;

import com.h.logging.Logger;
import com.h.mkt.SimpleStockTradingDesk;
import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;

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
    public void buy(SimpleStockTradingRepository repository, String ticker, int qty, BigDecimal price) throws InvalidArgumentException {
        bookEqTrade(repository, ticker, qty, price, BuySell.buy);
    }

    @Override
    public void sell(SimpleStockTradingRepository repository, String ticker, int qty, BigDecimal price) throws InvalidArgumentException {
        bookEqTrade(repository, ticker, qty, price, BuySell.sell);
    }

    private void bookEqTrade(SimpleStockTradingRepository repository, String ticker, int qty, BigDecimal price, BuySell buySell) throws InvalidArgumentException {

        log.append("%s booking a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                new Object[]{deskId, ticker, qty, price, buySell});

        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){

            throw new InvalidArgumentException(new String[]{
                    String.format("Ticker: %s wasn't found while booking a trade: qty=%s, price=%s, buySell=%s, desk=%s",
                            ticker, qty, price, buySell, deskId)});
        }

        EqTrade trade = new EqTrade(deskId, stock.getTicker(), qty, buySell, price);

        repository.addEqTrade(trade);

        trades.add(trade);

        log.append("%s booked a trade: ticker=%s, qty=%s, price=%s, buySell=%s.",
                new Object[]{deskId, ticker, qty, price, buySell});
    }
}