package com.h.mkt.data;

import java.math.BigDecimal;

/**
 * Created by halex on 10/30/16.
 */
public class PortfolioMargin {

    private final String portfolioId;
    private final BigDecimal eqMargin;

    public PortfolioMargin(String portfolioId, BigDecimal eqMargin){
        this.portfolioId = portfolioId;
        this.eqMargin = eqMargin;
    }

    public String getPortfolioId(){
        return portfolioId;
    }

    public BigDecimal getEquityMargin(){
        return eqMargin;
    }

    public BigDecimal getTotalMargin(){
        return getEquityMargin();
    }
}