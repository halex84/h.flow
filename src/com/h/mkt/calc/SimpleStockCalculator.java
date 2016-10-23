package com.h.mkt.calc;

import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.math.BigDecimal;

/**
 * Created by halex on 10/22/16.
 */
public interface SimpleStockCalculator {

    BigDecimal calculate(String ticker, SimpleStockTradingRepository repository) throws InvalidArgumentException;
}