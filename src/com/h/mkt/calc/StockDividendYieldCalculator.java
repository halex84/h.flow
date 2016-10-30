package com.h.mkt.calc;

import com.h.contexts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.data.Stock;
import com.h.contexts.DomainContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by halex on 10/22/16.
 */
public class StockDividendYieldCalculator implements StockCalculator {

    private final Logger log;
    private final StockCalculator stockPvCalculator;

    public StockDividendYieldCalculator(Logger logger, StockCalculator pvCalc){
        log = logger;
        stockPvCalculator = pvCalc;
    }

    @Override
    public BigDecimal calculate(String ticker, DomainContext context) throws CalculationException {

        log.append("Calculating dividend yield of %s.", new Object[]{ticker});

        Stock stock = context.getStockByTicker(ticker);

        BigDecimal pv = stock.getCurrentPv(context, stockPvCalculator);
        BigDecimal div = stock.getLastOrPreferredDividend();
        BigDecimal dy;
        
        if (pv.compareTo(BigDecimal.ZERO) == 0){
            dy = BigDecimal.ZERO;
        }
        else{
            dy = div.divide(pv, 16, RoundingMode.HALF_UP);
        }

        log.append("Calculated dividend yield of %s: %s.", new Object[]{ticker, dy});

        return dy;
    }
}