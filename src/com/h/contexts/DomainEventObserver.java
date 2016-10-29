package com.h.contexts;

/**
 * Created by halex on 10/29/16.
 */
public interface DomainEventObserver<U> extends EventObserver {
    
    void notifyAsync(U... args) throws CalculationException;
}