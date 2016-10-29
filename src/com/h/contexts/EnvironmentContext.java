package com.h.contexts;

import com.h.logging.LogFactory;
import com.h.logging.Logger;
import com.h.mkt.data.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by halex on 10/22/16.
 */
public class EnvironmentContext implements DomainContext {

    private final static ReentrantLock instanceLock = new ReentrantLock();
    private static DomainContext instance;
    public static DomainContext getInstance(){

        instanceLock.lock();
        try{
            if (instance == null){
                instance = new EnvironmentContext(LogFactory.makeLogger());
            }
            return instance;
        }
        finally {
            instanceLock.unlock();
        }
    }

    private final Lock deskLock = new ReentrantLock();
    private final Map<String, Desk> desks = new HashMap<>();
    private final Lock stockLock = new ReentrantLock();
    private final Map<String, Stock> stocks = new HashMap<>();
    private final Lock tradeLock = new ReentrantLock();
    private final Map<String, List<EqTrade>> trades = new HashMap<>();
    private final Lock indexLock = new ReentrantLock();
    private final Map<String, EqIndex> eqIndexes = new HashMap<>();

    private final Logger log;

    private EnvironmentContext(Logger logger){
        log = logger;
    }

    @Override
    public Desk getOrAddDeskById(String deskId) {

        deskLock.lock();
        try {

            Desk desk;
            if (this.desks.containsKey(deskId)){
                desk = this.desks.get(deskId);
            }
            else{
                log.append("Creating desk with Id: %s.", new String[] {deskId});
                desk = new Desk(deskId, LogFactory.makeLogger());
                this.desks.put(deskId, desk);
                log.append("Created desk with Id: %s and default state.", new String[] {deskId});
            }

            return desk;
        }
        finally {
            deskLock.unlock();
        }
    }

    @Override
    public EqIndex getOrAddEquityIndexByTicker(String ticker) {

        indexLock.lock();
        try {

            EqIndex index;
            if (this.eqIndexes.containsKey(ticker)){
                index = this.eqIndexes.get(ticker);
            }
            else{
                log.append("Importing index with ticker: %s.", new String[] {ticker});
                index = new EqIndex(ticker, LogFactory.makeLogger());
                this.eqIndexes.put(ticker, index);
                log.append("Imported index with ticker: %s and default state.", new String[] {ticker});
            }

            return index;
        }
        finally {
            indexLock.unlock();
        }
    }

    @Override
    public EqIndex getEquityIndexByTicker(String ticker) {

        indexLock.lock();
        try {
            if (eqIndexes.containsKey(ticker)){
                return eqIndexes.get(ticker);
            }
            else{
                return null;
            }
        }
        finally {
            indexLock.unlock();
        }
    }

    @Override
    public List<EqIndex> getEquityIndexesByComponent(Stock component) {

        indexLock.lock();
        try {
            return eqIndexes
                    .values()
                    .stream()
                    .filter(eqIndex -> eqIndex.getComponents().contains(component))
                    .collect(Collectors.toList());
        }
        finally {
            indexLock.unlock();
        }
    }

    @Override
    public Stock getOrAddStockByTicker(String ticker, StockType usedOnlyWhenAdding) {

        stockLock.lock();
        try {

            Stock stock;
            if (stocks.containsKey(ticker)){
                stock = stocks.get(ticker);
            }
            else{
                log.append("Importing ticker: %s.", new String[] {ticker});
                stock = new Stock(ticker, usedOnlyWhenAdding, LogFactory.makeLogger());
                stocks.put(ticker, stock);
                log.append("Imported ticker: %s with default state.", new String[] {ticker});
            }

            return stock;
        }
        finally {
            stockLock.unlock();
        }
    }

    @Override
    public List<Stock> getStocks(){

        stockLock.lock();
        try {
            return stocks
                    .values()
                    .stream()
                    .collect(Collectors.toList());
        }
        finally {
            stockLock.unlock();
        }
    }

    @Override
    public Stock getStockByTicker(String ticker) {

        stockLock.lock();
        try {
            if (stocks.containsKey(ticker)){
                return stocks.get(ticker);
            }
            else{
                return null;
            }
        }
        finally {
            stockLock.unlock();
        }
    }

    @Override
    public void addEqTrade(EqTrade trade) throws CalculationException {

        log.append("Trade order arrived for processing.");
        Stock stock = null;
        tradeLock.lock();
        log.append("Booking a trade.");
        try {
            stock = getStockByTicker(trade.getTicker());
            if (stock == null){
                throw new CalculationException("unknown ticker");
            }
            List<EqTrade> tickerTradeList;
            if (trades.containsKey(stock.getTicker())){
                tickerTradeList = trades.get(stock.getTicker());
            }
            else {
                tickerTradeList = new ArrayList<>();
                trades.put(stock.getTicker(), tickerTradeList);
            }
            tickerTradeList.add(trade);
        }
        finally {

            tradeLock.unlock();

            if (stock != null) {

                //ToDo refactor the repo ifc, per comment there, move the invalidate calls to event handlers.
                log.append("Entering trade order post processing.");
                log.append("Invalidating relevant index and ticker calculations.");
                //this will invalidate the cached data of all indexes which contain the stock now.
                //if the stock is added to the index later, it will invalidate itself.
                //will also unlock indexes.
                stock.invalidateCalculationResults();
                getEquityIndexesByComponent(stock).forEach(EqIndex::invalidateCalculationResults);
            }
        }
    }

    @Override
    public List<EqTrade> getEquityTradesByTickerAfter(String ticker, Date timestamp) {

        tradeLock.lock();
        try{
            if (!trades.containsKey(ticker)) {
                return null;
            }
            //ToDo consider performance (determine a reasonable size limit and chop up).
            return trades
                    .get(ticker)
                    .stream()
                    .filter(trade -> trade.getTimestamp().after(timestamp))
                    .collect(Collectors.toList());
        }
        finally {
            tradeLock.unlock();
        }
    }

    @Override
    public void housekeep() {

        deskLock.lock();
        indexLock.lock();
        stockLock.lock();
        tradeLock.lock();
        try{
            desks.clear();
            eqIndexes.clear();
            stocks.clear();
            trades.clear();
        }
        finally {
            deskLock.unlock();
            indexLock.unlock();
            stockLock.unlock();
            tradeLock.unlock();
        }
    }
}