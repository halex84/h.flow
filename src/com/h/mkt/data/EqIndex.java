package com.h.mkt.data;

import com.h.contexts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.calc.SimpleStockCalculator;
import com.h.contexts.DomainContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by halex on 10/22/16.
 */
public class EqIndex {

    public EqIndex(String ticker, Logger logger) {

        this.ticker = ticker;
        this.log = logger;
    }

    private final String ticker;

    private ArrayList<Stock> components = new ArrayList<>();
    private BigDecimal pv;
    private boolean isPvValid;

    private final Logger log;
    private final Lock modLock = new ReentrantLock();

    public String getTicker(){

        return ticker;
    }

    public List<Stock> getComponents() {

        modLock.lock();
        List<Stock> stocks = (List<Stock>) components.clone();
        modLock.unlock();

        return stocks;
    }

    public void setComponents(DomainContext repository, String[] componentTickers) throws CalculationException {

        ArrayList<Stock> components = new ArrayList<>();
        for (String st : componentTickers){

            Stock stock = repository.getStockByTicker(st);
            if (stock == null){

                throw new CalculationException(
                        String.format("Ticker: %s wasn't found while updating index: %s.", st, ticker));
            }
            else {
                components.add(stock);
            }
        }
        setComponents(components);
    }

    private void setComponents(List<Stock> newComponents){

        log.append("Setting components to index: %s.", new String[]{ticker});

        modLock.lock();
        components = new ArrayList<>(newComponents);
        isPvValid = false;
        modLock.unlock();

        log.append("Set components to index: %s.", new String[]{ticker});
    }

    public void addComponent(Stock component) {

        log.append("Adding a component to index: %s.", new String[]{ticker});

        modLock.lock();
        components.add(component);
        isPvValid = false;
        modLock.unlock();

        log.append("Added a component to index: %s.", new String[]{ticker});
    }

    public void removeComponent(Stock component) {

        log.append("Removing a component from index: %s.", new String[]{ticker});

        modLock.lock();
        components.remove(component);
        isPvValid = false;
        modLock.unlock();

        log.append("Removed a component from index: %s.", new String[]{ticker});
    }

    //note on locks: see in Stock, only worse.

    public void invalidateCalculationResults() {

        log.append("Invalidating index value: %s.", new String[]{ticker});

        isPvValid = false;

        log.append("Invalidated index value: %s.", new String[]{ticker});
    }

    public BigDecimal getCurrentValue(DomainContext repository, SimpleStockCalculator indexPvCalculator) throws CalculationException {

        if (isPvValid) {
            return pv;
        }
        pv = indexPvCalculator.calculate(ticker, repository);
        isPvValid = true;
        return pv;
    }
}