package com.h.mkt.calc;

import com.h.contexts.CalculationException;
import com.h.logging.Logger;
import com.h.contexts.DomainContext;
import java.math.BigDecimal;

/**
 * Created by halex on 10/23/16.
 */
public class AllStocksIndexValueCalculator extends IndexValueCalculator {

    public AllStocksIndexValueCalculator(Logger logger, SimpleStockCalculator stockPvCalc){
        super(logger, stockPvCalc);
    }

    @Override
    public BigDecimal calculate(String ticker, DomainContext repository) throws CalculationException {

        log.append("Calculating value of all stocks index.");
        BigDecimal idxPv = calculate(repository.getStocks(), repository);
        log.append("Calculated value of all stocks index: %s.", new Object[]{idxPv});
        return idxPv;
    }
}