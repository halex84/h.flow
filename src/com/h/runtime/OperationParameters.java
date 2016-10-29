package com.h.runtime;

import java.util.Arrays;

/**
 * Created by halex on 10/29/16.
 */
public class OperationParameters {

    public enum Operation {
        a,
        b,
        s
    }

    public Operation argument0;
    public int argument1;
    public String[] specifics;

    public OperationParameters(String[] arguments) {

        argument0 = Operation.valueOf(arguments[0]);

        int offset = 2;
        if (argument0.equals(Operation.b)){
            offset = 1;
        }

        if (offset >= arguments.length){
            specifics = new String[0];
        }
        else {
            specifics = Arrays.copyOfRange(arguments, offset, arguments.length);
        }

        if (arguments.length >= 2) {

            switch (arguments[1]) {
                case "i":
                    argument1 = 1;
                    break;
                case "ii":
                    argument1 = 2;
                    break;
                case "iii":
                    argument1 = 3;
                    break;
                case "iv":
                    argument1 = 4;
                    break;
            }
        }
    }
}
