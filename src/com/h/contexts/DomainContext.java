package com.h.contexts;

import com.h.mkt.data.*;
import java.util.Date;
import java.util.List;

/**
 * Created by halex on 10/22/16.
 */
public interface DomainContext {

    /**
     * Queries for the given desk; if not found creates one.
     * @param deskId the desk id, eg. "1234"
     * @return an up-to-date object
     */
    Desk getOrAddDeskById(String deskId);

    /**
     * Queries for the given index; if not found creates one.
     * @param ticker the index ticker, eg. ".DJI"
     * @return an up-to-date object
     */
    EqIndex getOrAddEquityIndexByTicker(String ticker);

    /**
     * Queries by the given ticker; if not found returns null.
     */
    EqIndex getEquityIndexByTicker(String ticker);

    /**
     * Queries for the given index;
     * @param component an underlying component
     * @return a list of up-to-date objects
     */
    List<EqIndex> getEquityIndexesByComponent(Stock component);

    /**
     * Queries by the given ticker; if not found creates one.
     * @param ticker to query by, eg. "JPM"
     * @param usedOnlyWhenAdding will be set as the stock type only if a new instance is created.
     * @return an up-to-date object
     */
    Stock getOrAddStockByTicker(String ticker, StockType usedOnlyWhenAdding);

    /**
     * @return all stocks.
     */
    List<Stock> getStocks();

    /**
     * Queries by the given ticker; if not found returns null.
     */
    Stock getStockByTicker(String ticker);

    /**
     * Adds an equity trade to the context.
     * @param trade to add
     * @throws CalculationException in case of failure.
     */
    void addEqTrade(EqTrade trade) throws CalculationException;

    /**
     * Gets the trades for the ticker made after the date.
     * @param ticker to get
     * @param timestamp after which to get
     * @return a list of up-to-date-objects
     */
    List<EqTrade> getEquityTradesByTickerAfter(String ticker, Date timestamp);

    /**
     * Clears the repository of all data.
     */
    void housekeep();

    /**
     * Context will use it to dispatch the events it generates.
     * @param dispatcher to use.
     */
    void setEventDispatcher(DomainEventDispatcher dispatcher);
}