package com.h.logging;

/**
 * Created by halex on 10/22/16.
 */
public class ConsoleLogger implements Logger {

    public void append(String message){
        append(message, null);
    }
    public void append(String message, Object[] args){
        //ToDo add coloring based on levels
        //http://stackoverflow.com/a/24415186
        //System.out.println((char)27 + "[34;43m" + String.format(message, args));
        System.out.println(String.format(message, args));
    }
}