package com.h.runtime;

import com.h.contexts.EnvironmentContext;
import com.h.logging.LogFactory;
import com.h.services.CalcDispatcher;
import com.h.services.DataDispatcher;
import com.h.services.TradeDispatcher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by halex on 10/22/16.
 */
class Terminal {

    public static void main(String[] args) throws IOException {

        //ToDo networking.
        System.out.print("ServerProcessImpl pending. Running from the command line.");

        //read-only accessors of current values have inner domain use;
        //however, here we emphasize the calculation of values.

        while(process(getCommandArgsFromConsole()) > 0) { }

        System.out.println();
        System.out.print("Have a nice day.");
    }

    private static int process(String[] args) {

        try {

            if (shouldExit(args)){
                return -1;
            }

            if (shouldPrintHelp(args)
                    || !OperationContext.validateArguments(args)){

                OperationContext.printUsage();
                return 1;
            }

            new Thread(() -> {
                OperationContext op =
                    OperationContext.makeRunnable(
                        args,
                        LogFactory.makeLogger(),
                        EnvironmentContext.getInstance(),
                        new CalcDispatcher(EnvironmentContext.getInstance()),
                        new DataDispatcher(EnvironmentContext.getInstance()),
                        new TradeDispatcher(EnvironmentContext.getInstance()));

                op.run();
            })
            .start();
        } catch (Exception ex) {

            System.out.println("*");
            System.out.println("Processing failed to start.");
            ex.printStackTrace();
            return 0;
        }

        return 1;
    }

    private static boolean shouldExit(String[] args) {

        return args.length == 1 && args[0].equals("t");
    }

    private static boolean shouldPrintHelp(String[] args) {

        return args.length == 1 && args[0].equals("h");
    }

    private static String[] getCommandArgsFromConsole() throws IOException {

        System.out.println();
        System.out.print("Command ['t' to exit, 'h' for help]#>");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        String command = inputReader.readLine();
        String[] commandArgs = command.split("\\s+");/*
        System.out.println();
        System.out.print("Running. Args...");
        for (int i = 0; i < commandArgs.length; i++){
            System.out.format("{%s: %s}", i+1, commandArgs[i]);
        }
        System.out.println();*/
        return commandArgs;
    }
}