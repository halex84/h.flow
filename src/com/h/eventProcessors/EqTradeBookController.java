package com.h.eventProcessors;

import com.h.contexts.CalculationException;
import com.h.contexts.DomainEventObserver;
import com.h.events.EqTradeBooked;
import com.h.logging.Logger;
import com.h.mkt.data.EqIndex;

/**
 * Created by halex on 10/29/16.
 */
public class EqTradeBookController implements DomainEventObserver<EqTradeBooked> {

    private final Logger log;

    public EqTradeBookController(Logger log){
        this.log = log;
    }

    @Override
    public boolean isObserving(String eventType) {
        return eventType.equals(EqTradeBooked.class.getName());
    }

    @Override
    public void notifyAsync(EqTradeBooked... args) throws CalculationException {

        log.append("Invalidating relevant index and ticker calculations.");

        args[0].getTradedStock().invalidateCalculationResults();
        args[0].getTradedStockOverlayingIndexes().forEach(EqIndex::invalidateCalculationResults);

        log.append("Invalidated relevant index and ticker calculations.");
    }
}