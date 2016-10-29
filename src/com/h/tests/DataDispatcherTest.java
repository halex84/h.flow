package com.h.tests;

import com.h.contexts.CalculationException;
import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.Stock;
import com.h.mkt.data.StockType;
import com.h.contexts.EnvironmentContext;
import com.h.services.DataDispatcher;
import com.h.contexts.DomainContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by halex on 10/23/16.
 */
public class DataDispatcherTest {

    @Test
    public void addOrUpdateTickers() throws Exception {

        //prepare.
        EqStockContract[] data = new EqStockContract[]{

                new EqStockContract("JPM", 48),
                new EqStockContract("JPM-Y", 1, 6.05),
                new EqStockContract("C", 16),
                new EqStockContract("WFC", 38),
                new EqStockContract("BAC", 7.5),
                new EqStockContract("MS", 2),
                new EqStockContract("GS", 65),
                new EqStockContract("DB", 83.47),
                new EqStockContract("DB", 0),
                new EqStockContract("BCS", 6.3536),
                new EqStockContract("BCS", 5.2968)
        };
        DomainContext context = EnvironmentContext.getInstance();
        DataDispatcher dataDispatcher = new DataDispatcher(context);
        //act.
        dataDispatcher.addOrUpdateTickers(data);

        //verify.
        //all tickers must be accessible via the context, and have expected state.
        List<Stock> stocks = context.getStocks();

        assertNotNull(stocks);
        assertEquals(9, stocks.size());

        Stock jpm = context.getStockByTicker("JPM");
        assertEquals("JPM", jpm.getTicker());
        assertEquals(StockType.common, jpm.getType());
        assertEquals(48, jpm.getLastDividend(), 0);
        assertEquals(0, jpm.getParValue(), 0);
        assertEquals(0, jpm.getParValueDividendPct(), 0);

        Stock jpmy = context.getStockByTicker("JPM-Y");
        assertEquals("JPM-Y", jpmy.getTicker());
        assertEquals(StockType.preferred, jpmy.getType());
        assertEquals(0, jpmy.getLastDividend(), 0);
        assertEquals(1, jpmy.getParValue(), 0);
        assertEquals(6.05, jpmy.getParValueDividendPct(), 0);

        Stock db = context.getStockByTicker("DB");
        assertEquals("DB", db.getTicker());
        assertEquals(StockType.common, db.getType());
        assertEquals(0, db.getLastDividend(), 0);
        assertEquals(0, db.getParValue(), 0);
        assertEquals(0, db.getParValueDividendPct(), 0);

        Stock bcs = context.getStockByTicker("BCS");
        assertEquals("BCS", bcs.getTicker());
        assertEquals(StockType.common, bcs.getType());
        assertEquals(5.2968, bcs.getLastDividend(), 0);
        assertEquals(0, bcs.getParValue(), 0);
        assertEquals(0, bcs.getParValueDividendPct(), 0);
    }

    @Test(expected=CalculationException.class)
    public void addOrUpdateEquityIndexMustThrowOnMissingComponents() throws Exception {

        DomainContext context = EnvironmentContext.getInstance();
        DataDispatcher dataDispatcher = new DataDispatcher(context);
        //set-up: create some tickers but not all.
        dataDispatcher.addOrUpdateTicker(new EqStockContract("JPM", 48));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("C", 16));
        EqIndexContract eqIdx = new EqIndexContract("DJUSBK", new String[]{ "JPM", "C", "WFC", "BAC", "GS" });
        //act.
        dataDispatcher.addOrUpdateEquityIndex(eqIdx);
    }

    @Test
    public void addOrUpdateEquityIndex() throws Exception {

        DomainContext context = EnvironmentContext.getInstance();
        DataDispatcher dataDispatcher = new DataDispatcher(context);
        //set-up: create all tickers to be included in the index.
        dataDispatcher.addOrUpdateTicker(new EqStockContract("JPM", 48));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("C", 16));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("WFC", 38));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("BAC", 7.5));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("GS", 65));
        EqIndexContract eqIdx = new EqIndexContract("DJUSBK", new String[]{ "JPM", "C", "WFC", "BAC", "GS" });
        //act.
        dataDispatcher.addOrUpdateEquityIndex(eqIdx);
        //verify.
        //all tickers must be in the context, and have expected state.
        Stock jpm = context.getStockByTicker("JPM");
        Stock c = context.getStockByTicker("C");
        //index must be in the context and contain the assigned components.
        //it must be found both by component and ticker.
        EqIndex byComponentJpm = context.getEquityIndexesByComponent(jpm).get(0);
        EqIndex byComponentC = context.getEquityIndexesByComponent(c).get(0);
        EqIndex byTicker = context.getEquityIndexByTicker("DJUSBK");
        //they all must be the same instance.
        Assert.assertTrue(byComponentJpm == byComponentC && byComponentC == byTicker);
        //for convenience.
        EqIndex index = byTicker;
        //assert all components are here.
        Assert.assertTrue(index.getComponents().contains(jpm));
        Assert.assertTrue(index.getComponents().contains(c));
        Assert.assertTrue(index.getComponents().contains(context.getStockByTicker("GS")));
        Assert.assertTrue(index.getComponents().contains(context.getStockByTicker("WFC")));
        Assert.assertTrue(index.getComponents().contains(context.getStockByTicker("BAC")));
    }

    @After
    public void afterTest(){
        EnvironmentContext.getInstance().housekeep();
    }
}