package com.h.runtime;

import com.h.contexts.CalculationException;
import com.h.logging.LogFactory;
import com.h.logging.Logger;
import com.h.services.CalcDispatcher;
import com.h.services.DataDispatcher;
import com.h.services.TradeDispatcher;
import java.util.Arrays;

public class Environment {

    private enum Operation {
        t,
        a,
        b,
        s
    }

    public static boolean process(String[] arguments){

        Logger log = LogFactory.makeLogger();

        try{
            Environment env = new Environment(arguments);

            if (env.isExitRequested()){
                return false;
            }

            env.run();
        }
        catch (Exception ex){
            ex.printStackTrace();
            //ToDo display random funny messages.
            //Your calculation experienced a hick-up.
            //God knows what happened. Restart everything.
            //Don't worry - the cavalry is coming!
            //Fire! Fire! Call the dev department!
            log.append("Keep calm and carry on.");
        }

        return true;
    }

    private Environment(String[] arguments) throws CalculationException {

        if (!validateArguments(arguments)){
            throw new CalculationException("invalid arguments");
        }

        argument0 = Operation.valueOf(arguments[0]);

        int offset = 2;
        if (argument0.equals(Operation.b)){
            offset = 1; //this is my favorite line, of course.
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

    private Operation argument0;
    private int argument1;
    private String[] specifics;

    private void run() throws CalculationException {

        switch (argument0){
            case a: {
                switch (argument1){
                    case 1:
                        CalcDispatcher.dividendYieldCalc(specifics[0]);
                        break;
                    case 2:
                        CalcDispatcher.peCalc(specifics[0]);
                        break;
                    case 3:
                        TradeDispatcher.bookTrade(ServiceContractsMapper.Trade(specifics));
                        break;
                    case 4:
                        CalcDispatcher.pvCalc(specifics[0]);
                        break;
                }
            }
            break;
            case b:{
                CalcDispatcher.calcIndexAllStocks();
            }
            break;
            case s: {
                switch (argument1){
                    case 1:
                        DataDispatcher.addOrUpdateTicker(ServiceContractsMapper.Stock(specifics));
                        break;
                    case 2:
                        DataDispatcher.addOrUpdateEquityIndex(ServiceContractsMapper.Index(specifics));
                        break;
                    case 3:
                        CalcDispatcher.calcIndex(specifics[0]);
                        break;
                }
            }
            break;
        }
    }

    private boolean isExitRequested(){
        return argument0.equals(Operation.t);
    }

    private static boolean validateArguments(String[] arguments){
        //TODO wv off
        return true;
    }
}