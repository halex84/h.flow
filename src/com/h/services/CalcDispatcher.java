package com.h.services;

import com.h.logging.LogFactory;
import com.h.mkt.calc.*;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.Stock;
import com.h.repositories.InMemoryTradeRepository;
import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;
import java.math.BigDecimal;

/**
 * CalcDispatcher API
 * Created by halex on 10/22/16.
 */
public class CalcDispatcher {

    //ToDo replace calculators with expressions.

    public static BigDecimal pvCalc(String ticker) throws InvalidArgumentException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){
            throw new InvalidArgumentException(new String[]{"unknown ticker"});
        }
        return stock.getCurrentPv(repository, pvCalc);
    }

    public static BigDecimal peCalc(String ticker) throws InvalidArgumentException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator peCalc = new PeCalculator(LogFactory.makeLogger(), pvCalc);
        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){
            throw new InvalidArgumentException(new String[]{"unknown ticker"});
        }
        return stock.getCurrentPe(repository, peCalc);
    }

    public static BigDecimal dividendYieldCalc(String ticker) throws InvalidArgumentException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockCalculator dyCalc = new DividendYieldCalculator(LogFactory.makeLogger(), pvCalc);
        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        Stock stock = repository.getStockByTicker(ticker);
        if (stock == null){
            throw new InvalidArgumentException(new String[]{"unknown ticker"});
        }
        //this will cache the value.
        //just invoking the calc won't.
        //the calc doesn't care about the domain and architecture.
        return stock.getCurrentDy(repository, dyCalc);
    }

    public static BigDecimal calcIndex(String ticker) throws InvalidArgumentException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        SimpleStockCalculator idxPvCalc = new EqIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        EqIndex index = repository.getEquityIndexByTicker(ticker);
        if (index == null){
            throw new InvalidArgumentException(new String[]{"unknown eq. index"});
        }
        return index.getCurrentValue(repository, idxPvCalc);
    }

    public static BigDecimal calcIndexAllStocks() throws InvalidArgumentException {

        SimpleStockCalculator pvCalc = new PvCalculator(LogFactory.makeLogger());
        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        SimpleStockCalculator idxPvCalc = new AllStocksIndexValueCalculator(LogFactory.makeLogger(), pvCalc);
        return idxPvCalc.calculate(null, repository);
    }
}