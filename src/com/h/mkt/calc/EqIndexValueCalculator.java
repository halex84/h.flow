package com.h.mkt.calc;

import com.h.contexts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.data.EqIndex;
import com.h.contexts.DomainContext;
import java.math.BigDecimal;

/**
 * Created by halex on 10/22/16.
 */
public class EqIndexValueCalculator extends IndexValueCalculator {

    public EqIndexValueCalculator(Logger logger, StockCalculator stockPvCalc){
        super(logger, stockPvCalc);
    }

    @Override
    public BigDecimal calculate(String ticker, DomainContext context) throws CalculationException {

        log.append("Calculating value of index %s.", new Object[]{ticker});

        EqIndex eqIndex = context.getOrAddEquityIndexByTicker(ticker);
        BigDecimal idxPv = calculate(eqIndex.getComponents(), context);

        log.append("Calculated value of index %s: %s.", new Object[]{ticker, idxPv});

        return idxPv;
    }
}