package com.h.runtime;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by halex on 10/22/16.
 *
 * Requirements
 1. Provide working source code that will :-
 a. For a given stock,
 i. calculate the dividend yield
 ii. calculate the P/E Ratio
 iii. record a trade, with timestamp, quantity of shares, buy or sell indicator and price
 iv. Calculate Stock Price based on trades recorded in past 15 minutes
 b. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
 *
 */
class Main {

    public static void main(String[] args) throws IOException {

        while(Environment.process(getCommandArgsFromConsole())) { }
    }

    private static String[] getCommandArgsFromConsole() throws IOException {

        System.out.print("Command ['t' to exit]#>");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        String command = inputReader.readLine();
        String[] commandArgs = command.split("\\s+");
        System.out.print("Running. Args...");
        for (int i = 0; i < commandArgs.length; i++){
            System.out.format("%s: %s", i+1, commandArgs[i]);
        }
        return commandArgs;
    }
}