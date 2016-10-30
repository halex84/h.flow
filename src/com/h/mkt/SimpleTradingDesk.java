package com.h.mkt;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainContext;
import java.math.BigDecimal;

/**
 * Books one trade at a time.
 * Created by halex on 10/22/16.
 */
public interface SimpleTradingDesk {

    void buy(DomainContext context, String ticker, int qty, BigDecimal price) throws CalculationException;
    void sell(DomainContext context, String ticker, int qty, BigDecimal price) throws CalculationException;
}