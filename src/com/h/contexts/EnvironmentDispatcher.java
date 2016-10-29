package com.h.contexts;

import com.h.contexts.eventProcessors.EqTradeBookController;
import com.h.logging.LogFactory;
import com.h.logging.Logger;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by halex on 10/29/16.
 */
public class EnvironmentDispatcher implements DomainEventDispatcher {

    private final static ReentrantLock instanceLock = new ReentrantLock();
    private static DomainEventDispatcher instance;
    public static DomainEventDispatcher getInstance(){

        instanceLock.lock();
        try{
            if (instance == null){

                EventObserver[] observers = new EventObserver[]{

                        new EqTradeBookController(LogFactory.makeLogger())
                };

                instance = new EnvironmentDispatcher(LogFactory.makeLogger(), observers);
            }
            return instance;
        }
        finally {
            instanceLock.unlock();
        }
    }

    private final Logger log;
    private final EventObserver[] observers;

    private EnvironmentDispatcher(Logger log, EventObserver[] observers){
        this.log = log;
        this.observers = observers;
    }

    @Override
    public <U> void dispatch(U... args) {

        try {
            String argType = args[0].getClass().getName();
            for (EventObserver o : observers) {
                EventObserver observer = o;
                new Thread(() -> {
                    try {

                        if (!observer.isObserving(argType)) {
                            return;
                        }

                        ((DomainEventObserver<U>) observer).notifyAsync(args);

                    } catch (Exception e) {

                        log.append("Error while processing domain event: %s.", new Object[]{e});
                        e.printStackTrace();
                    }
                })
                .start();
            }
        }
        catch (Exception e){

            log.append("Error while dispatching domain event: %s.", new Object[]{e});
            e.printStackTrace();
        }
    }
}