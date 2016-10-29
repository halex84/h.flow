package com.h.contexts;

/**
 * Created by halex on 10/29/16.
 */
public class EnvironmentMonitor implements DomainEventHandler<String, Integer> {

    @Override
    public String executeService(Integer... args) {
        // do stuff
        return null;
    }
}