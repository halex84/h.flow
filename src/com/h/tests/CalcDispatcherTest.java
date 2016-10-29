package com.h.tests;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainContext;
import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.contracts.TradeContract;
import com.h.mkt.data.BuySell;
import com.h.contexts.EnvironmentContext;
import com.h.services.CalcDispatcher;
import com.h.services.DataDispatcher;
import com.h.services.TradeDispatcher;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by halex on 10/23/16.
 */
public class CalcDispatcherTest {

    @Test(expected=CalculationException.class)
    public void pvCalcMustFailForUnknownTickers() throws Exception {

        calcDispatcher.pvCalc("ABCD");
    }

    @Test(expected=CalculationException.class)
    public void peCalcMustFailForUnknownTickers() throws Exception {

        calcDispatcher.peCalc("ABCD");
    }

    @Test(expected=CalculationException.class)
    public void dividendYieldCalcMustFailForUnknownTickers() throws Exception {

        calcDispatcher.dividendYieldCalc("ABCD");
    }

    @Test(expected=CalculationException.class)
    public void calcIndexMustFailForUnknownIndexTickers() throws Exception {

        calcDispatcher.calcIndex("I");
    }

    @Test
    public void pvCalcMustReturnZeroForTickerWithNoTrades() throws Exception {

        addIndexAndComponents();

        assertEquals(0, calcDispatcher.pvCalc("ABCD").doubleValue(), 0);
        assertEquals(0, calcDispatcher.pvCalc("BCDE").doubleValue(), 0);
        assertEquals(0, calcDispatcher.pvCalc("CDEF").doubleValue(), 0);
        assertEquals(0, calcDispatcher.pvCalc("VWXY").doubleValue(), 0);
        assertEquals(0, calcDispatcher.pvCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void peCalcMustReturnZeroForTickerWithNoTrades() throws Exception {

        addIndexAndComponents();

        assertEquals(0, calcDispatcher.peCalc("ABCD").doubleValue(), 0);
        assertEquals(0, calcDispatcher.peCalc("BCDE").doubleValue(), 0);
        assertEquals(0, calcDispatcher.peCalc("CDEF").doubleValue(), 0);
        assertEquals(0, calcDispatcher.peCalc("VWXY").doubleValue(), 0);
        assertEquals(0, calcDispatcher.peCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void dividendYieldCalcMustReturnZeroForTickerWithNoTrades() throws Exception {

        addIndexAndComponents();

        assertEquals(0, calcDispatcher.dividendYieldCalc("ABCD").doubleValue(), 0);
        assertEquals(0, calcDispatcher.dividendYieldCalc("BCDE").doubleValue(), 0);
        assertEquals(0, calcDispatcher.dividendYieldCalc("CDEF").doubleValue(), 0);
        assertEquals(0, calcDispatcher.dividendYieldCalc("VWXY").doubleValue(), 0);
        assertEquals(0, calcDispatcher.dividendYieldCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void calcIndexMustReturnZeroForIndexTickerWithNoComponents() throws Exception {

        addIndex();
        assertEquals(0, calcDispatcher.calcIndex("I").doubleValue(), 0);
    }

    @Test
    public void calcIndexMustReturnZeroForIndexTickerWithNoTrades() throws Exception {

        addIndexAndComponents();
        assertEquals(0, calcDispatcher.calcIndex("I").doubleValue(), 0);
    }

    @Test
    public void calcIndexAllStocksMustReturnZeroWhenThereAreNoTrades() throws Exception {

        addIndexAndComponents();
        assertEquals(0, calcDispatcher.calcIndexAllStocks().doubleValue(), 0);
    }

    @Test
    public void pvCalc() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        assertEquals(31.145454545454548, calcDispatcher.pvCalc("ABCD").doubleValue(), 0);
        assertEquals(15, calcDispatcher.pvCalc("BCDE").doubleValue(), 0);
        assertEquals(112.5, calcDispatcher.pvCalc("CDEF").doubleValue(), 0);
        assertEquals(30, calcDispatcher.pvCalc("VWXY").doubleValue(), 0);
        assertEquals(3000, calcDispatcher.pvCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void peCalc() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        assertEquals(155.7272727272727430, calcDispatcher.peCalc("ABCD").doubleValue(), 0);//31.14÷0.2
        assertEquals(150, calcDispatcher.peCalc("BCDE").doubleValue(), 0);//15÷0.1
        assertEquals(0, calcDispatcher.peCalc("CDEF").doubleValue(), 0);//112.5÷0
        assertEquals(600, calcDispatcher.peCalc("VWXY").doubleValue(), 0);//30÷0.05
        assertEquals(600, calcDispatcher.peCalc("WXYZ").doubleValue(), 0);//3000÷5
    }

    @Test
    public void dividendYieldCalc() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        assertEquals(0.0064214827787507, calcDispatcher.dividendYieldCalc("ABCD").doubleValue(), 0);//(31.14÷0.2)^-1
        assertEquals(0.0066666666666667, calcDispatcher.dividendYieldCalc("BCDE").doubleValue(), 0);//(15÷0.1)^-1
        assertEquals(0, calcDispatcher.dividendYieldCalc("CDEF").doubleValue(), 0);//(112.5÷0)^-1
        assertEquals(0.0016666666666667, calcDispatcher.dividendYieldCalc("VWXY").doubleValue(), 0);//(30÷0.05)^-1
        assertEquals(0.0016666666666667, calcDispatcher.dividendYieldCalc("WXYZ").doubleValue(), 0);//(3000÷5)^-1
    }

    @Test
    public void calcIndex() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        //(31.145454545454548*15*30*3000)^(1/4)
        assertEquals(80.52525531774512, calcDispatcher.calcIndex("I").doubleValue(), 0);
    }

    @Test
    public void calcIndexAllStocks() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        //(31.145454545454548*15*30*3000*112.5)^(1/5)
        assertEquals(86.09465555973358, calcDispatcher.calcIndexAllStocks().doubleValue(), 0);
    }

    private void addIndex() throws CalculationException {

        EqIndexContract eqIdx = new EqIndexContract("I", new String[]{});
        dataDispatcher.addOrUpdateEquityIndex(eqIdx);
    }

    private void addIndexAndComponents() throws CalculationException {

        dataDispatcher.addOrUpdateTicker(new EqStockContract("ABCD", 20));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("BCDE", 10));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("CDEF", 0));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("VWXY", 1, 5));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("WXYZ", 100, 5));

        EqIndexContract eqIdx = new EqIndexContract("I", new String[]{ "ABCD", "BCDE", "VWXY", "WXYZ" });
        dataDispatcher.addOrUpdateEquityIndex(eqIdx);
    }

    private void addIndexComponentTrades() throws CalculationException {

        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 2000, BigDecimal.valueOf(20)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 100, BigDecimal.valueOf(19)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 200, BigDecimal.valueOf(22)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 200, BigDecimal.valueOf(25)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "ABCD", 3000, BigDecimal.valueOf(40)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "BCDE", 2000, BigDecimal.valueOf(10)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "BCDE", 2000, BigDecimal.valueOf(20)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "CDEF", 2000, BigDecimal.valueOf(75)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "CDEF", 2000, BigDecimal.valueOf(150)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "VWXY", 2000, BigDecimal.valueOf(20)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "VWXY", 2000, BigDecimal.valueOf(40)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "WXYZ", 20, BigDecimal.valueOf(1999)));
        tradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "WXYZ", 20, BigDecimal.valueOf(4001)));
    }

    private TradeDispatcher tradeDispatcher;
    private DataDispatcher dataDispatcher;
    private CalcDispatcher calcDispatcher;
    
    public CalcDispatcherTest(){

        DomainContext environment = EnvironmentContext.getInstance();
        tradeDispatcher = new TradeDispatcher(environment);
        dataDispatcher = new DataDispatcher(environment);
        calcDispatcher = new CalcDispatcher(environment);
    }
    
    @After
    public void afterTest(){
        
        DomainContext environment = EnvironmentContext.getInstance();
        
        environment.housekeep();
        
        tradeDispatcher = new TradeDispatcher(environment);
        dataDispatcher = new DataDispatcher(environment);
        calcDispatcher = new CalcDispatcher(environment);
    }
}