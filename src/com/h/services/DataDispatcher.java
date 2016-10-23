package com.h.services;

import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.logging.LogFactory;
import com.h.logging.Logger;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.Stock;
import com.h.repositories.InMemoryTradeRepository;
import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * DataDispatcher API
 * Created by halex on 10/23/16.
 */
public class DataDispatcher {

    private static final Logger log = LogFactory.makeLogger();

    /**
     * Creates or updates a stock.
     */
    public static void addOrUpdateTicker(EqStockContract ticker){

        addOrUpdateTickers(new EqStockContract[]{ticker});
    }

    /**
     * Creates or updates stocks.
     */
    public static void addOrUpdateTickers(EqStockContract[] tickers){

        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        for (EqStockContract ticker : tickers) {

            log.append("Ticker update requested: %s.", new String[]{ticker.ticker});

            Stock stock = repository.getOrAddStockByTicker(ticker.ticker, ticker.stockType);
            stock.update(ticker.stockType, ticker.lastDividend, ticker.parValue, ticker.parValueFixedDividendPct);

            log.append("Ticker update completed: %s.", new String[]{ticker.ticker});
        }
    }

    /**
     * Creates or updates an equity stock index.
     */
    public static void addOrUpdateEquityIndex(EqIndexContract index) throws InvalidArgumentException {

        log.append("Equity index update requested: %s.", new String[]{index.ticker});

        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        EqIndex eqIndex = repository.getOrAddEquityIndexByTicker(index.ticker);

        eqIndex.setComponents(repository, index.componentTickers);

        log.append("Equity index update completed: %s.", new String[]{index.ticker});
    }
}