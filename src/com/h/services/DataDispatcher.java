package com.h.services;

import com.h.contexts.CalculationException;
import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.logging.LogFactory;
import com.h.logging.Logger;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.Stock;
import com.h.contexts.EnvironmentContext;
import com.h.contexts.DomainContext;

/**
 * DataDispatcher API
 * Created by halex on 10/23/16.
 */
public class DataDispatcher {

    private final Logger log;
    private final DomainContext context;

    public DataDispatcher(DomainContext context){
        this.context = context;
        this.log = LogFactory.makeLogger();
    }

    /**
     * Creates or updates a stock.
     */
    public void addOrUpdateTicker(EqStockContract ticker){

        addOrUpdateTickers(new EqStockContract[]{ticker});
    }

    /**
     * Creates or updates stocks.
     */
    public void addOrUpdateTickers(EqStockContract[] tickers){

        for (EqStockContract ticker : tickers) {

            log.append("Ticker update requested: %s.", new String[]{ticker.ticker});

            Stock stock = context.getOrAddStockByTicker(ticker.ticker, ticker.stockType);
            stock.update(ticker.stockType, ticker.lastDividend, ticker.parValue, ticker.parValueFixedDividendPct);

            log.append("Ticker update completed: %s.", new String[]{ticker.ticker});
        }
    }

    /**
     * Creates or updates an equity stock index.
     */
    public void addOrUpdateEquityIndex(EqIndexContract index) throws CalculationException {

        addOrUpdateEquityIndexes(new EqIndexContract[]{index});
    }

    /**
     * Creates or updates equity stock indexes.
     */
    public void addOrUpdateEquityIndexes(EqIndexContract[] indexes) throws CalculationException {

        for (EqIndexContract index : indexes) {

            log.append("Equity index update requested: %s.", new String[]{index.ticker});

            EqIndex eqIndex = context.getOrAddEquityIndexByTicker(index.ticker);
            eqIndex.setComponents(context, index.componentTickers);

            log.append("Equity index update completed: %s.", new String[]{index.ticker});
        }
    }
}