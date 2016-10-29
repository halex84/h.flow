package com.h.events;

import com.h.mkt.data.EqIndex;
import com.h.mkt.data.EqTrade;
import com.h.mkt.data.Stock;

import java.util.List;

/**
 * Created by halex on 10/29/16.
 */
public class EqTradeBooked {

    public EqTradeBooked(EqTrade bookedTrade, Stock tradedStock, List<EqIndex> tradedStockOverlayingIndexes) {
        this.bookedTrade = bookedTrade;
        this.tradedStock = tradedStock;
        this.tradedStockOverlayingIndexes = tradedStockOverlayingIndexes;
    }

    public final EqTrade bookedTrade;
    public final Stock tradedStock;
    public final List<EqIndex> tradedStockOverlayingIndexes;
}
