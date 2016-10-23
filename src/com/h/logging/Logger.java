package com.h.logging;

/**
 * Created by halex on 10/22/16.
 */
public interface Logger {

    //ToDo add levels

    void append(String message);
    void append(String message, Object[] args);
}
