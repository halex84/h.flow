package com.h.contexts;

/**
 * Created by halex on 10/29/16.
 */
public interface EventObserver {

    /**
     * Is the observer to handle this event type?
     * @param eventType to check.
     * @return true if yes.
     */
    boolean isObserving(String eventType);
}