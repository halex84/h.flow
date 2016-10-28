package com.h.mkt.calc;

import com.h.contracts.CalculationException;
import com.h.logging.Logger;
import com.h.mkt.data.Stock;
import com.h.repositories.SimpleStockTradingRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by halex on 10/23/16.
 */
abstract class IndexValueCalculator implements SimpleStockCalculator {

    final Logger log;
    private final SimpleStockCalculator stockPvCalculator;

    IndexValueCalculator(Logger logger, SimpleStockCalculator stockPvCalc){

        log = logger;
        stockPvCalculator = stockPvCalc;
    }

    BigDecimal calculate(List<Stock> components, SimpleStockTradingRepository repository) throws CalculationException {

        if (components == null || components.size() == 0){
            return BigDecimal.ZERO;
        }
        //Calc'ing the geometric mean by multiplying the n-th sqrt of each element.
        BigDecimal exp = BigDecimal.valueOf(1d / components.size());
        BigDecimal idxPv = BigDecimal.ONE;
        BigDecimal[] cPvsWtd = new BigDecimal[components.size()];
        for (int i = 0; i < components.size(); i++){
            //The components will update their own pv if needed.
            BigDecimal uPv = components.get(i).getCurrentPv(repository, stockPvCalculator);
            cPvsWtd[i] = BigDecimal.valueOf(Math.pow(uPv.doubleValue(), exp.doubleValue()));
            idxPv = BigDecimal.valueOf(idxPv.doubleValue() * cPvsWtd[i].doubleValue());
        }

        return idxPv;
    }
}