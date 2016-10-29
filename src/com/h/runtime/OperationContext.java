package com.h.runtime;

import com.h.contexts.DomainContext;
import com.h.logging.Logger;
import com.h.services.CalcDispatcher;
import com.h.services.DataDispatcher;
import com.h.services.TradeDispatcher;

/**
 * Created by halex on 10/29/16.
 */
public class OperationContext {

    private final DomainContext context;
    private final CalcDispatcher calcDispatcher;
    private final DataDispatcher dataDispatcher;
    private final TradeDispatcher tradeDispatcher;
    private final Logger log;

    private OperationContext(Logger log, DomainContext context, CalcDispatcher calcDispatcher, DataDispatcher dataDispatcher, TradeDispatcher tradeDispatcher) {
        this.context = context;
        this.calcDispatcher = calcDispatcher;
        this.dataDispatcher = dataDispatcher;
        this.tradeDispatcher = tradeDispatcher;
        this.log = log;
    }

    private OperationParameters args;
    private boolean amRunnable = false;

    public OperationContext run() {

        //ToDo networking.
        //request->ServerProcess->request started.
        //out: calculation events. and messaging. etc.
        if (!amRunnable){
            System.out.println("No new runnable operation registered.");
            return this;
        }

        try {

            switch (args.argument0) {
                case a: {
                    switch (args.argument1) {
                        case 1:
                            calcDispatcher.dividendYieldCalc(args.specifics[0]);
                            break;
                        case 2:
                            calcDispatcher.peCalc(args.specifics[0]);
                            break;
                        case 3:
                            tradeDispatcher.bookTrade(ContractsMapper.Trade(args.specifics));
                            break;
                        case 4:
                            calcDispatcher.pvCalc(args.specifics[0]);
                            break;
                    }
                }
                break;
                case b: {
                    calcDispatcher.calcIndexAllStocks();
                }
                break;
                case s: {
                    switch (args.argument1) {
                        case 1:
                            dataDispatcher.addOrUpdateTicker(ContractsMapper.Stock(args.specifics));
                            break;
                        case 2:
                            dataDispatcher.addOrUpdateEquityIndex(ContractsMapper.Index(args.specifics));
                            break;
                        case 3:
                            calcDispatcher.calcIndex(args.specifics[0]);
                            break;
                    }
                }
                break;
            }
        }
        catch (Exception e){

            System.out.println("*"); //remember to validate.
            System.out.println("Processing failed.");
            e.printStackTrace();
        }

        return this;
    }

    public static void printUsage() {
        //ToDo print usage to the system console.
    }

    public static boolean validateArguments(String[] arguments){
        //ToDo print validation messages to the system console.
        return  true;
    }

    public static OperationContext makeRunnable(String[] args, Logger log, DomainContext context, CalcDispatcher calcDispatcher, DataDispatcher dataDispatcher, TradeDispatcher tradeDispatcher) {

        OperationContext oc = new OperationContext(log, context, calcDispatcher, dataDispatcher, tradeDispatcher);

        try{
            //remember to print usage and validation for new functionality.
            oc.args = new OperationParameters(args);
            oc.amRunnable = true;
        }
        catch (Exception e){ e.printStackTrace(); }

        return oc;
    }
}