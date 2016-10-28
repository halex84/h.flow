package com.h.mkt;

import com.h.contracts.CalculationException;
import com.h.repositories.SimpleStockTradingRepository;
import java.math.BigDecimal;

/**
 * Books one trade at a time.
 * Created by halex on 10/22/16.
 */
public interface SimpleStockTradingDesk {

    void buy(SimpleStockTradingRepository repository, String ticker, int qty, BigDecimal price) throws CalculationException;
    void sell(SimpleStockTradingRepository repository, String ticker, int qty, BigDecimal price) throws CalculationException;
}