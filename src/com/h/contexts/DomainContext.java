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

    //ToDo publish domain modified (trade post processing, etc.) events.
    //while adding an eq trade a repo must invalidate its eq and idx calc results.
    //this would help a lot when e.g. adding a new stock to add it to an index. :)
    //without - i'm sorry - no automatic index component mapping updates.
    //u can manually invoke the index modification api after adding a new stock,
    //if u'd like to endeavour to maintain an 'all stocks' index.
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
}