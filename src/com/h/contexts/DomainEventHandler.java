package com.h.contexts;

/**
 * Created by halex on 10/29/16.
 */
public interface DomainEventHandler<T,U> {
    T executeService(U... args);
}
