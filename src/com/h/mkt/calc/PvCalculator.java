package com.h.mkt.calc;

import com.h.logging.Logger;
import com.h.mkt.data.EqTrade;
import com.h.repositories.SimpleStockTradingRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by halex on 10/22/16.
 */
public class PvCalculator implements SimpleStockCalculator {

    private final Logger log;

    public PvCalculator(Logger logger){
        log = logger;
    }

    @Override
    public BigDecimal calculate(String ticker, SimpleStockTradingRepository repository) {

        log.append("Calculating PV of %s.", new Object[]{ticker});

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -15);
        Date time = cal.getTime();

        List<EqTrade> trades = repository.getEquityTradesByTickerAfter(ticker, time);

        if (trades == null || trades.size() == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal pv = BigDecimal.ZERO;
        BigDecimal totalQty = BigDecimal.valueOf(trades.stream().mapToInt(EqTrade::getQty).sum());

        for(EqTrade trade : trades){
            BigDecimal qty = BigDecimal.valueOf(trade.getQty());
            BigDecimal wt = qty.divide(totalQty, 16, RoundingMode.HALF_UP);
            pv = pv.add(wt.multiply(trade.getPrice()));
        }

        log.append("Calculated PV of %s: %s.", new Object[]{ticker, pv});

        return pv;
    }
}