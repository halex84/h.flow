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
        this.log = log;
        this.context = context;
        this.calcDispatcher = calcDispatcher;
        this.dataDispatcher = dataDispatcher;
        this.tradeDispatcher = tradeDispatcher;
    }

    private TerminalArgs args;
    private boolean iAmRunnable = false;

    public OperationContext run() {

        //ToDo networking.
        //request->ServerProcess->request started.
        //out: calculation events. and messaging. etc.
        //have separate Server|CommandLine|Network Contexts?
        //with the CL&N processes dispatching requests to the server?
        //with separate configs, and injecting the dispatchers?
        //maybe work with a distributed in-memory context?
        if (!iAmRunnable){
            System.out.println("No new runnable operation registered.");
            return this;
        }

        try {

            switch (args.operation) {
                case calc: {
                    switch (args.calcArgument) {
                        case dy:
                            calcDispatcher.dividendYieldCalc(args.specifics[0]);
                            break;
                        case pe:
                            calcDispatcher.peCalc(args.specifics[0]);
                            break;
                        case pv:
                            calcDispatcher.pvCalc(args.specifics[0]);
                            break;
                        case index:
                            calcDispatcher.calcIndex(args.specifics[0]);
                            break;
                        case indexAll:
                            calcDispatcher.calcIndexAllStocks();
                            break;
                    }
                }
                break;
                case load: {
                    switch (args.loadArgument) {
                        case stock:
                            dataDispatcher.addOrUpdateTicker(args.getStock());
                            break;
                        case stockFile:
                            dataDispatcher.addOrUpdateTickers(args.getStocksFromFile());
                            break;
                        case eqIndex:
                            dataDispatcher.addOrUpdateEquityIndex(args.getIndex());
                            break;
                        case eqIndexFile:
                            dataDispatcher.addOrUpdateEquityIndexes(args.getIndexesFromFile());
                            break;
                        case trade:
                            tradeDispatcher.bookTrade(args.getTrade());
                            break;
                        case tradeFile:
                            tradeDispatcher.bookTrades(args.getTradesFromFile());
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
            oc.args = new TerminalArgs(args);
            oc.iAmRunnable = true;
        }
        catch (Exception e){ e.printStackTrace(); }

        return oc;
    }
}