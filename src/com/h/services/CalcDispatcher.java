package com.h.services;

import com.h.contexts.CalculationException;
import com.h.logging.LogFactory;
import com.h.mkt.calc.*;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.Stock;
import com.h.contexts.EnvironmentContext;
import com.h.contexts.DomainContext;
import java.math.BigDecimal;

/**
 * CalcDispatcher API
 * Created by halex on 10/22/16.
 */
public class CalcDispatcher {

    //ToDo replace calculators with expressions.

    public static BigDecimal pvCalc(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        DomainContext repository = EnvironmentContext.getInstance();
        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        return stock.getCurrentPv(repository, pvCalc);
    }

    public static BigDecimal peCalc(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator peCalc = new PeCalculator(LogFactory.makeLogger(), pvCalc);
        DomainContext repository = EnvironmentContext.getInstance();
        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        return stock.getCurrentPe(repository, peCalc);
    }

    public static BigDecimal dividendYieldCalc(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator dyCalc = new DividendYieldCalculator(LogFactory.makeLogger(), pvCalc);
        DomainContext repository = EnvironmentContext.getInstance();
        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        //this will cache the value.
        //just invoking the calc won't.
        //the calc doesn't care about the domain and architecture.
        return stock.getCurrentDy(repository, dyCalc);
    }

    public static BigDecimal calcIndex(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        DomainContext repository = EnvironmentContext.getInstance();
        SimpleStockCalculator idxPvCalc = new EqIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        EqIndex index = repository.getEquityIndexByTicker(ticker);
        if (index == null){
            throw new CalculationException("unknown eq. index");
        }
        return index.getCurrentValue(repository, idxPvCalc);
    }

    public static BigDecimal calcIndexAllStocks() throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        DomainContext repository = EnvironmentContext.getInstance();
        SimpleStockCalculator idxPvCalc = new AllStocksIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        return idxPvCalc.calculate(null, repository);
    }
}