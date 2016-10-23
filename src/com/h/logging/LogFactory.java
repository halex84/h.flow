package com.h.logging;

/**
 * Created by halex on 10/22/16.
 */
public final class LogFactory {

    private static final Logger _consoleLogger = new ConsoleLogger();

    //ToDo make one with external logging.
    //ToDo invoke all registered loggers.

    /**
     * Enables dependency resolution by the factory.
     * */
    public static Logger makeLogger(){
        return _consoleLogger;
    }
}