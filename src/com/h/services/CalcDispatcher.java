package com.h.services;

import com.h.contexts.CalculationException;
import com.h.logging.LogFactory;
import com.h.mkt.calc.*;
import com.h.mkt.data.Desk;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.PortfolioMargin;
import com.h.mkt.data.Stock;
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

    public BigDecimal pvCalc(String ticker) throws CalculationException {

        StockCalculator pvCalc = new StockPvCalculator(LogFactory.makeLogger());
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

        StockCalculator pvCalc = new StockPvCalculator(LogFactory.makeLogger());
        StockCalculator peCalc = new StockPeCalculator(LogFactory.makeLogger(), pvCalc);
        Stock stock = context.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        return stock.getCurrentPe(context, peCalc);
    }

    public BigDecimal dividendYieldCalc(String ticker) throws CalculationException {

        StockCalculator pvCalc = new StockPvCalculator(LogFactory.makeLogger());
        StockCalculator dyCalc = new StockDividendYieldCalculator(LogFactory.makeLogger(), pvCalc);
        Stock stock = context.getStockByTicker(ticker);
        if (stock == null){
            throw new CalculationException("unknown ticker");
        }
        return stock.getCurrentDy(context, dyCalc);
    }

    public BigDecimal calcIndex(String ticker) throws CalculationException {

        StockCalculator pvCalc = new StockPvCalculator(LogFactory.makeLogger());
        StockCalculator idxPvCalc = new EqIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        EqIndex index = context.getEquityIndexByTicker(ticker);
        if (index == null){
            throw new CalculationException("unknown eq. index");
        }
        return index.getCurrentValue(context, idxPvCalc);
    }

    public BigDecimal calcIndexAllStocks() throws CalculationException {

        StockCalculator pvCalc = new StockPvCalculator(LogFactory.makeLogger());
        StockCalculator idxPvCalc = new AllStocksIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        return idxPvCalc.calculate(null, context);
    }

    public BigDecimal calcDeskPortfolioMargin(String deskId, String portfolioId) throws CalculationException {

        Desk desk = context.getDeskById(deskId);
        if (desk == null){
            throw new CalculationException("unknown desk");
        }
        StockCalculator pvCalc = new StockPvCalculator(LogFactory.makeLogger());
        PortfolioMarginCalculator marginCalculator = new PortfolioMarginCalculator(LogFactory.makeLogger(), pvCalc);
        PortfolioMargin margin = desk.getPortfolioMargin(context, marginCalculator, portfolioId);
        if (margin == null){
            throw new CalculationException("unknown portfolio");
        }
        return margin.getTotalMargin();
    }
}