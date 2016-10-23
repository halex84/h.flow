package com.h.visualisations;

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

    public static void main(String[] args) {

        //ToDo think about this.
        while(Environment.process(args)) {
            System.out.println("~");
            args = System.console().readLine().split("\\s+");
        }
    }
}