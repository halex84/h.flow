package com.h.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h.contexts.CalculationException;
import com.h.contracts.EqStockContract;
import com.h.contracts.TradeContract;
import com.h.mkt.data.BuySell;
import com.h.mkt.data.EqTrade;
import com.h.contexts.EnvironmentContext;
import com.h.contexts.DomainContext;
import com.h.services.DataDispatcher;
import com.h.services.TradeDispatcher;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by halex on 10/23/16.
 */
public class TradeDispatcherTest {

    @Test(expected=CalculationException.class)
    public void bookTradeMustFailOnMissingTickers() throws Exception {

        DomainContext context = EnvironmentContext.getInstance();
        TradeDispatcher tradeDispatcher = new TradeDispatcher(context);
        //prepare.
        TradeContract[] trades = new TradeContract[]{

                new TradeContract("eq", BuySell.buy, "DB", 5000, BigDecimal.valueOf(13.33)),
                new TradeContract("eq", BuySell.sell, "DB", 5000, BigDecimal.valueOf(15)),
                new TradeContract("eq", BuySell.sell, "AAPL", 2000, BigDecimal.valueOf(117.05)),
                new TradeContract("eq", BuySell.buy, "BCS", 5000, BigDecimal.valueOf(8)),
                new TradeContract("eq", BuySell.sell, "BCS", 5000, BigDecimal.valueOf(9.45)),
                new TradeContract("eq", BuySell.buy, "JPM", 3000, BigDecimal.valueOf(69))
        };

        //act.
        for (TradeContract trade : trades) {
            tradeDispatcher.bookTrade(trade);
        }
    }

    @Test
    public void bookTrade() throws Exception {

        DomainContext context = EnvironmentContext.getInstance();
        DataDispatcher dataDispatcher = new DataDispatcher(context);
        TradeDispatcher tradeDispatcher = new TradeDispatcher(context);

        //prepare.
        dataDispatcher.addOrUpdateTicker(new EqStockContract("DB", 0));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("AAPL", 57));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("BCS", 5.2968));
        dataDispatcher.addOrUpdateTicker(new EqStockContract("JPM", 48));
        TradeContract[] trades = new TradeContract[]{

                new TradeContract("eq", BuySell.buy, "DB", 5000, BigDecimal.valueOf(13.33)),
                new TradeContract("eq", BuySell.sell, "DB", 5000, BigDecimal.valueOf(15)),
                new TradeContract("eq", BuySell.sell, "AAPL", 2000, BigDecimal.valueOf(117.05)),
                new TradeContract("eq", BuySell.buy, "BCS", 5000, BigDecimal.valueOf(8)),
                new TradeContract("eq", BuySell.sell, "BCS", 5000, BigDecimal.valueOf(9.45)),
                new TradeContract("eq", BuySell.buy, "JPM", 3000, BigDecimal.valueOf(69))
        };

        //act.
        for (TradeContract trade : trades) {
            tradeDispatcher.bookTrade(trade);
        }

        //verify.
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -15);
        Date timestamp = cal.getTime();

        List<EqTrade> bcsTrades = context.getEquityTradesByTickerAfter("BCS", timestamp);
        List<EqTrade> jpmTrades = context.getEquityTradesByTickerAfter("JPM", timestamp);
        List<EqTrade> aaplTrades = context.getEquityTradesByTickerAfter("AAPL", timestamp);
        List<EqTrade> dbTrades = context.getEquityTradesByTickerAfter("DB", timestamp);

        assertNotNull(bcsTrades);
        assertEquals(2, bcsTrades.size());

        assertNotNull(jpmTrades);
        assertEquals(1, jpmTrades.size());

        assertNotNull(aaplTrades);
        assertEquals(1, aaplTrades.size());

        assertNotNull(dbTrades);
        assertEquals(2, dbTrades.size());

        //validate two trades.
        EqTrade jpmTrade = jpmTrades.get(0);

        assertEquals("eq", jpmTrade.getDeskId());
        assertEquals(BuySell.buy, jpmTrade.getBuySell());
        assertEquals("JPM", jpmTrade.getTicker());
        assertEquals(BigDecimal.valueOf(69), jpmTrade.getPrice());
        assertEquals(3000, jpmTrade.getQty());

        EqTrade bcsTrade = bcsTrades.get(1);

        assertEquals("eq", bcsTrade.getDeskId());
        assertEquals(BuySell.sell, bcsTrade.getBuySell());
        assertEquals("BCS", bcsTrade.getTicker());
        assertEquals(BigDecimal.valueOf(9.45), bcsTrade.getPrice());
        assertEquals(5000, bcsTrade.getQty());
    }

    @After
    public void afterTest(){
        EnvironmentContext.getInstance().housekeep();
    }
}