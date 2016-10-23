package com.h.visualisations;

import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.contracts.TradeContract;
import com.h.mkt.data.BuySell;
import com.h.mkt.data.StockType;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.math.BigDecimal;

/**
 * Created by halex on 10/23/16.
 */
class Contracts {

    /**
     * Builds a trade contract from the command line arguments.
     * @param specifics deskId, (buy|sell), TICKER, qty, price
     * @return trade contract
     * @throws InvalidArgumentException if arguments are invalid.
     */
    public static TradeContract Trade(String[] specifics) throws InvalidArgumentException {

        if (!validateTradeArguments(specifics)){
            throw new InvalidArgumentException(specifics);
        }

        BuySell buySell = BuySell.valueOf(specifics[0]);
        String ticker = specifics[1];
        int qty = Integer.parseInt(specifics[2]);
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(specifics[3]));

        return new TradeContract(buySell, ticker, qty, price);
    }

    /**
     * Builds a stock contract from the command line arguments.
     * @param specifics: TICKER, common|preferred, { common: lastDividend }, { preferred: parValue, parValueDividendPct }
     * @throws InvalidArgumentException if arguments are invalid.
     */
    public static EqStockContract Stock(String[] specifics) throws InvalidArgumentException {

        if (!validateStockArguments(specifics)){
            throw new InvalidArgumentException(specifics);
        }

        StockType type = StockType.valueOf(specifics[1]);

        switch (type){
            case common:
                return new EqStockContract(specifics[0], Double.parseDouble(specifics[2]));
            case preferred:
                return new EqStockContract(specifics[0], Double.parseDouble(specifics[2]), Double.parseDouble(specifics[3]));
        }

        throw new InvalidArgumentException(new String[]{"stock.type"});
    }

    /**
     * Builds an index contract from the command line arguments.
     * @param specifics: TICKER, ST1,ST2,..,STn (2nd arg: csv without spaces of component tickers)
     * @throws InvalidArgumentException if arguments are invalid.
     */
    public static EqIndexContract Index(String[] specifics) throws InvalidArgumentException {

        if (!validateIndexArguments(specifics)){
            throw new InvalidArgumentException(specifics);
        }

        return new EqIndexContract(specifics[0], specifics[1].split(","));
    }

    //ToDo fix Wolfsburg below (with log output).

    private static boolean validateTradeArguments(String[] arguments){
        return true;
    }

    private static boolean validateStockArguments(String[] specifics) {
        return  true;
    }

    private static boolean validateIndexArguments(String[] specifics) {
        return true;
    }
}