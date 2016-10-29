package com.h.runtime;

import com.h.contexts.CalculationException;
import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.contracts.TradeContract;
import com.h.mkt.data.BuySell;
import com.h.mkt.data.StockType;

import java.math.BigDecimal;

/**
 * Created by halex on 10/23/16.
 */
class ContractsMapper {

    /**
     * Builds a trade contract from the command line arguments.
     * @param specifics deskId, (buy|sell), TICKER, qty, price
     * @return trade contract
     * @throws CalculationException if arguments are invalid.
     */
    public static TradeContract Trade(String[] specifics) throws CalculationException {

        if (!validateTradeArguments(specifics)){
            throw new CalculationException("invalid arguments");
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
     * @throws CalculationException if arguments are invalid.
     */
    public static EqStockContract Stock(String[] specifics) throws CalculationException {

        if (!validateStockArguments(specifics)){
            throw new CalculationException("invalid arguments");
        }

        StockType type = StockType.valueOf(specifics[1]);

        switch (type){
            case common:
                return new EqStockContract(specifics[0], Double.parseDouble(specifics[2]));
            case preferred:
                return new EqStockContract(specifics[0], Double.parseDouble(specifics[2]), Double.parseDouble(specifics[3]));
        }

        throw new CalculationException("stock.type unknown");
    }

    /**
     * Builds an index contract from the command line arguments.
     * @param specifics: TICKER, ST1,ST2,..,STn (2nd arg: csv without spaces of component tickers)
     * @throws CalculationException if arguments are invalid.
     */
    public static EqIndexContract Index(String[] specifics) throws CalculationException {

        if (!validateIndexArguments(specifics)){
            throw new CalculationException("invalid arguments");
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