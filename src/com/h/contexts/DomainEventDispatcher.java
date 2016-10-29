package com.h.contexts;

import com.h.events.EqTradeBooked;

/**
 * Created by halex on 10/29/16.
 */
public interface DomainEventDispatcher {

    <U> void dispatch(U... args);
}