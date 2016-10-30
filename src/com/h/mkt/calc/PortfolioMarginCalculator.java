package com.h.mkt.calc;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainContext;
import com.h.logging.Logger;
import com.h.mkt.data.Portfolio;
import com.h.mkt.data.PortfolioMargin;
import com.h.mkt.data.Stock;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by halex on 10/30/16.
 */
public class PortfolioMarginCalculator implements MarginCalculator {

    private final Logger log;
    private final StockCalculator stockPvCalculator;

    public PortfolioMarginCalculator(Logger log, StockCalculator stockPvCalculator){
        this.log = log;
        this.stockPvCalculator = stockPvCalculator;
    }

    //ToDo replace with expression based calculation.

    @Override
    public PortfolioMargin calculate(Portfolio portfolio, DomainContext context) throws CalculationException {

        log.append("Calculating Portfolio margin of %s.", new Object[]{portfolio.getPortfolioId()});

        BigDecimal eqMargin = calculateEqMargin(portfolio.getNetEquityPositions(), context);
        PortfolioMargin margin = new PortfolioMargin(portfolio.getPortfolioId(), eqMargin);

        log.append("Calculated Portfolio margin of %s: %s.", new Object[]{margin.getPortfolioId(), margin.getTotalMargin()});

        return margin;
    }

    private BigDecimal calculateEqMargin(Map<String, Integer> netEqPositions, DomainContext context) throws CalculationException {

        //return a haircut of the total net position.
        BigDecimal margin = BigDecimal.ZERO;
        for (String ticker : netEqPositions.keySet()){

            Stock stock = context.getStockByTicker(ticker);
            if (stock == null){
                throw new CalculationException("unknown ticker");
            }
            int scale = netEqPositions.get(ticker);
            BigDecimal pV = stock.getCurrentPv(context, stockPvCalculator);
            margin = margin.add(pV.multiply(BigDecimal.valueOf(0.1)).multiply(BigDecimal.valueOf(scale)));
        }
        return margin.abs();
    }
}