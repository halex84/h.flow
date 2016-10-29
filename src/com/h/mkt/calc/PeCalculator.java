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
public class PeCalculator implements SimpleStockCalculator {

    private final Logger log;
    private final SimpleStockCalculator stockPvCalculator;

    public PeCalculator(Logger logger, SimpleStockCalculator pvCalc){

        log = logger;
        stockPvCalculator = pvCalc;
    }

    @Override
    public BigDecimal calculate(String ticker, DomainContext repository) throws CalculationException {

        log.append("Calculating PE of %s.", new Object[]{ticker});

        Stock stock = repository.getStockByTicker(ticker);

        BigDecimal pe;
        BigDecimal pv = stock.getCurrentPv(repository, stockPvCalculator);
        BigDecimal div = stock.getLastOrPreferredDividend();

        if (div.compareTo(BigDecimal.ZERO) == 0){
            pe = BigDecimal.ZERO;
        }
        else {
            pe = pv.divide(div, 16, RoundingMode.HALF_UP);
        }

        log.append("Calculated PE of %s: %s.", new Object[]{ticker, pe});

        return  pe;
    }
}