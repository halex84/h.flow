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

    private final DomainContext context;

    public CalcDispatcher(DomainContext context){
        this.context = context;
    }

    //ToDo replace calculators with expressions.

    public BigDecimal pvCalc(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        Stock stock = context.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        //the domain object might want to look around the repo itself,
        //after all it is in charge of caching and consistency of it's root.
        //the calc is only a calc, with it's own dependencies.
        //let's not have a factory - for now.
        return stock.getCurrentPv(context, pvCalc);
    }

    public BigDecimal peCalc(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator peCalc = new PeCalculator(LogFactory.makeLogger(), pvCalc);
        Stock stock = context.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        return stock.getCurrentPe(context, peCalc);
    }

    public BigDecimal dividendYieldCalc(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator dyCalc = new DividendYieldCalculator(LogFactory.makeLogger(), pvCalc);
        Stock stock = context.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        return stock.getCurrentDy(context, dyCalc);
    }

    public BigDecimal calcIndex(String ticker) throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator idxPvCalc = new EqIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        EqIndex index = context.getEquityIndexByTicker(ticker);
        if (index == null){
            throw new CalculationException("unknown eq. index");
        }
        return index.getCurrentValue(context, idxPvCalc);
    }

    public BigDecimal calcIndexAllStocks() throws CalculationException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator idxPvCalc = new AllStocksIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        return idxPvCalc.calculate(null, context);
    }
}