package com.h.mkt.calc;

import com.h.contracts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.data.Stock;
import com.h.repositories.SimpleStockTradingRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by halex on 10/22/16.
 */
public class DividendYieldCalculator implements SimpleStockCalculator {

    private final Logger log;
    private final SimpleStockCalculator stockPvCalculator;

    public DividendYieldCalculator(Logger logger, SimpleStockCalculator pvCalc){
        log = logger;
        stockPvCalculator = pvCalc;
    }

    @Override
    public BigDecimal calculate(String ticker, SimpleStockTradingRepository repository) throws CalculationException {

        log.append("Calculating dividend yield of %s.", new Object[]{ticker});

        Stock stock = repository.getStockByTicker(ticker);

        BigDecimal pv = stock.getCurrentPv(repository, stockPvCalculator);
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