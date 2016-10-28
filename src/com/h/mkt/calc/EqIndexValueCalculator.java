package com.h.mkt.calc;

import com.h.contracts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.data.EqIndex;
import com.h.repositories.SimpleStockTradingRepository;
import java.math.BigDecimal;

/**
 * Created by halex on 10/22/16.
 */
public class EqIndexValueCalculator extends IndexValueCalculator {

    public EqIndexValueCalculator(Logger logger, SimpleStockCalculator stockPvCalc){
        super(logger, stockPvCalc);
    }

    @Override
    public BigDecimal calculate(String ticker, SimpleStockTradingRepository repository) throws CalculationException {

        log.append("Calculating value of index %s.", new Object[]{ticker});

        EqIndex eqIndex = repository.getOrAddEquityIndexByTicker(ticker);
        BigDecimal idxPv = calculate(eqIndex.getComponents(), repository);

        log.append("Calculated value of index %s: %s.", new Object[]{ticker, idxPv});

        return idxPv;
    }
}