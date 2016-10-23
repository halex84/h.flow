package com.h.mkt.data;

import com.h.logging.Logger;
import com.h.mkt.calc.SimpleStockCalculator;
import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by halex on 10/22/16.
 */
public class Stock {

    public Stock(String ticker, StockType type, Logger logger) {

        this.ticker = ticker;
        this.type = type;
        this.log = logger;
    }

    private final String ticker;
    private StockType type;
    private double lastDividend; //quoted in cents.
    private double parValue;
    private double parValueDividendPct; //quoted in %.

    private BigDecimal pv;
    private BigDecimal pe;
    private BigDecimal dy;
    private boolean isPvValid;
    private boolean isPeValid;
    private boolean isDyValid;

    private final Logger log;

    public String getTicker(){ return ticker; }
    public StockType getType(){  return type; }
    public double getLastDividend(){ return  lastDividend; }
    public double getParValue() { return parValue; }
    public double getParValueDividendPct() { return parValueDividendPct; }

    public BigDecimal getLastOrPreferredDividend() throws InvalidArgumentException {

        switch (type) {
            case common:
                return BigDecimal.valueOf(lastDividend).divide(BigDecimal.valueOf(100), 16, RoundingMode.HALF_UP);
            case preferred:
                return BigDecimal.valueOf((parValueDividendPct / 100) * parValue);
        }

        throw new InvalidArgumentException(new String[]{"stockType" });
    }

    //a note on locks:
    //if an invalidation occurs @ the moment when a value is retrieved, those are still good results.
    //a calculation will always have to wait for the repository to finish updating, thus will always be correct.
    //if we were to aggressively lock we might get a deadlock between the PvCalculator and addEqTrade in the repo.

    public void update(StockType type, double lastDividend, double parValue, double parValueDividendPct) {

        log.append("Updating ticker: %s. Setting: type=%s, lastDividend=%s, parValue=%s, parValueDividendPct=%s.",
                new String[]{ticker, type.toString(), Double.toString(lastDividend), Double.toString(parValue), Double.toString(parValueDividendPct)});

        this.type = type;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
        this.parValueDividendPct = parValueDividendPct;

        isPeValid = false;
        isDyValid = false;

        log.append("Updated ticker: %s, invalidated pe and dy calculations.", new String[]{ticker});
    }

    public void invalidateCalculationResults(){

        log.append("Invalidating ticker calculation results: %s.", new String[]{ticker});

        isPvValid = false;
        isPeValid = false;
        isDyValid = false;

        log.append("Invalidated ticker calculation results: %s.", new String[]{ticker});
    }

    public BigDecimal getCurrentPv(SimpleStockTradingRepository repository, SimpleStockCalculator calculator) throws InvalidArgumentException {

        if (isPvValid){
            return pv;
        }
        pv = calculator.calculate(ticker, repository);
        isPvValid = true;
        return pv;
    }

    public BigDecimal getCurrentPe(SimpleStockTradingRepository repository, SimpleStockCalculator calculator) throws InvalidArgumentException {

        if (isPeValid) {
            return pe;
        }
        pe = calculator.calculate(ticker, repository);
        isPeValid = true;
        return pe;
    }

    public BigDecimal getCurrentDy(SimpleStockTradingRepository repository, SimpleStockCalculator calculator) throws InvalidArgumentException {

        if (isDyValid){
            return dy;
        }
        dy = calculator.calculate(ticker, repository);
        isDyValid = true;
        return dy;
    }
}