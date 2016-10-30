package com.h.events;

import com.h.contexts.DomainContext;
import com.h.mkt.data.EqIndex;
import com.h.mkt.data.EqTrade;
import com.h.mkt.data.Stock;

import java.util.List;

/**
 * Created by halex on 10/29/16.
 */
public class EqTradeBooked {

    public EqTradeBooked(DomainContext srcInfo, EqTrade bookedTrade) {
        this.src = srcInfo;
        this.bookedTrade = bookedTrade;
    }

    private final DomainContext src;
    private final EqTrade bookedTrade;

    public EqTrade getBookedTrade(){

        return bookedTrade;
    }

    public Stock getTradedStock() {

        return src.getStockByTicker(bookedTrade.getTicker());
    }

    public List<EqIndex> getTradedStockOverlayingIndexes(){

        return src.getEquityIndexesByComponent(getTradedStock());
    }
}
