package com.h.mkt.calc;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainContext;
import com.h.mkt.data.Portfolio;
import com.h.mkt.data.PortfolioMargin;

/**
 * Created by halex on 10/30/16.
 */
public interface MarginCalculator {

    /**
     * Calculates the margin for the given portfolio.
     * @param portfolio to calculate margin for
     * @param context to operate in
     * @return new PortfolioMargin margin instance
     */
    PortfolioMargin calculate(Portfolio portfolio, DomainContext context) throws CalculationException;
}