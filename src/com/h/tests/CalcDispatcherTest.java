package com.h.tests;

import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.contracts.TradeContract;
import com.h.mkt.data.BuySell;
import com.h.repositories.InMemoryTradeRepository;
import com.h.services.CalcDispatcher;
import com.h.services.DataDispatcher;
import com.h.services.TradeDispatcher;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by halex on 10/23/16.
 */
public class CalcDispatcherTest {

    @Test(expected=InvalidArgumentException.class)
    public void pvCalcMustFailForUnknownTickers() throws Exception {

        CalcDispatcher.pvCalc("ABCD");
    }

    @Test(expected=InvalidArgumentException.class)
    public void peCalcMustFailForUnknownTickers() throws Exception {

        CalcDispatcher.peCalc("ABCD");
    }

    @Test(expected=InvalidArgumentException.class)
    public void dividendYieldCalcMustFailForUnknownTickers() throws Exception {

        CalcDispatcher.dividendYieldCalc("ABCD");
    }

    @Test(expected=InvalidArgumentException.class)
    public void calcIndexMustFailForUnknownIndexTickers() throws Exception {

        CalcDispatcher.calcIndex("I");
    }

    @Test
    public void pvCalcMustReturnZeroForTickerWithNoTrades() throws Exception {

        addIndexAndComponents();

        assertEquals(0, CalcDispatcher.pvCalc("ABCD").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.pvCalc("BCDE").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.pvCalc("CDEF").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.pvCalc("VWXY").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.pvCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void peCalcMustReturnZeroForTickerWithNoTrades() throws Exception {

        addIndexAndComponents();

        assertEquals(0, CalcDispatcher.peCalc("ABCD").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.peCalc("BCDE").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.peCalc("CDEF").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.peCalc("VWXY").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.peCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void dividendYieldCalcMustReturnZeroForTickerWithNoTrades() throws Exception {

        addIndexAndComponents();

        assertEquals(0, CalcDispatcher.dividendYieldCalc("ABCD").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.dividendYieldCalc("BCDE").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.dividendYieldCalc("CDEF").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.dividendYieldCalc("VWXY").doubleValue(), 0);
        assertEquals(0, CalcDispatcher.dividendYieldCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void calcIndexMustReturnZeroForIndexTickerWithNoComponents() throws Exception {

        addIndex();
        assertEquals(0, CalcDispatcher.calcIndex("I").doubleValue(), 0);
    }

    @Test
    public void calcIndexMustReturnZeroForIndexTickerWithNoTrades() throws Exception {

        addIndexAndComponents();
        assertEquals(0, CalcDispatcher.calcIndex("I").doubleValue(), 0);
    }

    @Test
    public void calcIndexAllStocksMustReturnZeroWhenThereAreNoTrades() throws Exception {

        addIndexAndComponents();
        assertEquals(0, CalcDispatcher.calcIndexAllStocks().doubleValue(), 0);
    }

    @Test
    public void pvCalc() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        assertEquals(31.145454545454548, CalcDispatcher.pvCalc("ABCD").doubleValue(), 0);
        assertEquals(15, CalcDispatcher.pvCalc("BCDE").doubleValue(), 0);
        assertEquals(112.5, CalcDispatcher.pvCalc("CDEF").doubleValue(), 0);
        assertEquals(30, CalcDispatcher.pvCalc("VWXY").doubleValue(), 0);
        assertEquals(3000, CalcDispatcher.pvCalc("WXYZ").doubleValue(), 0);
    }

    @Test
    public void peCalc() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        assertEquals(155.7272727272727430, CalcDispatcher.peCalc("ABCD").doubleValue(), 0);//31.14÷0.2
        assertEquals(150, CalcDispatcher.peCalc("BCDE").doubleValue(), 0);//15÷0.1
        assertEquals(0, CalcDispatcher.peCalc("CDEF").doubleValue(), 0);//112.5÷0
        assertEquals(600, CalcDispatcher.peCalc("VWXY").doubleValue(), 0);//30÷0.05
        assertEquals(600, CalcDispatcher.peCalc("WXYZ").doubleValue(), 0);//3000÷5
    }

    @Test
    public void dividendYieldCalc() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        assertEquals(0.0064214827787507, CalcDispatcher.dividendYieldCalc("ABCD").doubleValue(), 0);//(31.14÷0.2)^-1
        assertEquals(0.0066666666666667, CalcDispatcher.dividendYieldCalc("BCDE").doubleValue(), 0);//(15÷0.1)^-1
        assertEquals(0, CalcDispatcher.dividendYieldCalc("CDEF").doubleValue(), 0);//(112.5÷0)^-1
        assertEquals(0.0016666666666667, CalcDispatcher.dividendYieldCalc("VWXY").doubleValue(), 0);//(30÷0.05)^-1
        assertEquals(0.0016666666666667, CalcDispatcher.dividendYieldCalc("WXYZ").doubleValue(), 0);//(3000÷5)^-1
    }

    @Test
    public void calcIndex() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        //(31.145454545454548*15*30*3000)^(1/4)
        assertEquals(80.52525531774512, CalcDispatcher.calcIndex("I").doubleValue(), 0);
    }

    @Test
    public void calcIndexAllStocks() throws Exception {

        addIndexAndComponents();
        addIndexComponentTrades();

        //(31.145454545454548*15*30*3000*112.5)^(1/5)
        assertEquals(86.09465555973358, CalcDispatcher.calcIndexAllStocks().doubleValue(), 0);
    }

    private void addIndex() throws InvalidArgumentException {

        EqIndexContract eqIdx = new EqIndexContract("I", new String[]{});
        DataDispatcher.addOrUpdateEquityIndex(eqIdx);
    }

    private void addIndexAndComponents() throws InvalidArgumentException {

        DataDispatcher.addOrUpdateTicker(new EqStockContract("ABCD", 20));
        DataDispatcher.addOrUpdateTicker(new EqStockContract("BCDE", 10));
        DataDispatcher.addOrUpdateTicker(new EqStockContract("CDEF", 0));
        DataDispatcher.addOrUpdateTicker(new EqStockContract("VWXY", 1, 5));
        DataDispatcher.addOrUpdateTicker(new EqStockContract("WXYZ", 100, 5));

        EqIndexContract eqIdx = new EqIndexContract("I", new String[]{ "ABCD", "BCDE", "VWXY", "WXYZ" });
        DataDispatcher.addOrUpdateEquityIndex(eqIdx);
    }

    private void addIndexComponentTrades() throws InvalidArgumentException {

        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 2000, BigDecimal.valueOf(20)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 100, BigDecimal.valueOf(19)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 200, BigDecimal.valueOf(22)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "ABCD", 200, BigDecimal.valueOf(25)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "ABCD", 3000, BigDecimal.valueOf(40)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "BCDE", 2000, BigDecimal.valueOf(10)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "BCDE", 2000, BigDecimal.valueOf(20)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "CDEF", 2000, BigDecimal.valueOf(75)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "CDEF", 2000, BigDecimal.valueOf(150)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "VWXY", 2000, BigDecimal.valueOf(20)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "VWXY", 2000, BigDecimal.valueOf(40)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.buy, "WXYZ", 20, BigDecimal.valueOf(1999)));
        TradeDispatcher.bookTrade(new TradeContract("eq", BuySell.sell, "WXYZ", 20, BigDecimal.valueOf(4001)));
    }

    @After
    public void afterTest(){
        InMemoryTradeRepository.getInstance().housekeep();
    }
}