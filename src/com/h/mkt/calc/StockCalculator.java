package com.h.mkt.calc;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainContext;

import java.math.BigDecimal;

/**
 * Created by halex on 10/22/16.
 */
public interface StockCalculator {

    BigDecimal calculate(String ticker, DomainContext context) throws CalculationException;
}