package com.h.contracts;

/**
 * Created by halex on 10/28/16.
 */
public class CalculationException extends Exception {

    //ToDo create specific exception types for all ops.

    public CalculationException(String reason){
        this.reason = reason;
    }

    public String reason;
}
