package com.h.mkt.calc;

import com.h.logging.Logger;
import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;
import java.math.BigDecimal;

/**
 * Created by halex on 10/23/16.
 */
public class AllStocksIndexValueCalculator extends IndexValueCalculator {

    public AllStocksIndexValueCalculator(Logger logger, SimpleStockCalculator stockPvCalc){
        super(logger, stockPvCalc);
    }

    @Override
    public BigDecimal calculate(String ticker, SimpleStockTradingRepository repository) throws InvalidArgumentException {

        log.append("Calculating value of all stocks index.");
        BigDecimal idxPv = calculate(repository.getStocks(), repository);
        log.append("Calculated value of all stocks index: %s.", new Object[]{idxPv});
        return idxPv;
    }
}