package com.h.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h.contexts.CalculationException;
import com.h.contracts.EqIndexContract;
import com.h.contracts.EqStockContract;
import com.h.contracts.TradeContract;
import com.h.mkt.data.BuySell;
import com.h.mkt.data.StockType;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by halex on 10/29/16.
 * *
 * Requirements:
 1. Provide working source code that will :-
 a. For a given stock,
 i. calculate the dividend yield
 ii. calculate the P/E Ratio
 iii. record a trade, with timestamp, quantity of shares, buy or sell indicator and price
 iv. Calculate Stock Price based on trades recorded in past 15 minutes
 b. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
 *
 */
public class TerminalArgs {

    public enum Operation {
        calc,
        load
    }

    public enum Calc {
        pe,
        pv,
        dy,
        index,
        indexAll
    }

    public enum Load {
        trade,
        tradeFile,
        stock,
        stockFile,
        eqIndex,
        eqIndexFile
    }

    public Operation operation;
    public Load loadArgument = null;
    public Calc calcArgument = null;
    public String[] specifics;

    private final ContractsMapper contractsMapper;

    /**
     * Creates an instance of parameters from valid arguments.
     * @param arguments operation argument specific
     * e.g.
     * calc pe TICKER
     * calc pv TICKER
     * load stock TICKER, common|preferred, { common: lastDividend }, { preferred: parValue, parValueDividendPct }
     * load stockFile pathToFile
     * load trade deskId, (buy|sell), TICKER, qty, price
     * load tradeFile pathToFile
     * ->Files should contain an array of JSON serialized contract instances.
     */
    public TerminalArgs(String[] arguments) {

        contractsMapper = new ContractsMapper();
        operation = Operation.valueOf(arguments[0]);
        specifics = Arrays.copyOfRange(arguments, 2, arguments.length);
        if (operation.equals(Operation.calc)){
            calcArgument = Calc.valueOf(arguments[1]);
        }
        else if (operation.equals(Operation.load)){
            loadArgument = Load.valueOf(arguments[1]);
        }
    }

    public EqStockContract getStock() throws CalculationException {

        return contractsMapper.Stock(specifics);
    }

    public EqStockContract[] getStocksFromFile() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        EqStockContract[] stocks = mapper.readValue(new File(specifics[0]), EqStockContract[].class);

        return stocks;
    }

    public EqIndexContract getIndex() throws CalculationException {

        return contractsMapper.Index(specifics);
    }

    public EqIndexContract[] getIndexesFromFile() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        EqIndexContract[] indexes = mapper.readValue(new File(specifics[0]), EqIndexContract[].class);

        return indexes;
    }

    public TradeContract getTrade() throws CalculationException {

        return contractsMapper.Trade(specifics);
    }

    public TradeContract[] getTradesFromFile() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        TradeContract[] trades = mapper.readValue(new File(specifics[0]), TradeContract[].class);

        return trades;
    }

    private class ContractsMapper {

        /**
         * Builds a trade contract from the command line arguments.
         * @param specifics (buy|sell), TICKER, qty, price, [ deskId ]
         * @return trade contract
         */
        public TradeContract Trade(String[] specifics)  {

            BuySell buySell = BuySell.valueOf(specifics[0]);
            String ticker = specifics[1];
            int qty = Integer.parseInt(specifics[2]);
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(specifics[3]));
            if (specifics.length >= 5) {

                return new TradeContract(specifics[4], buySell, ticker, qty, price);
            } else {

                return new TradeContract(null, buySell, ticker, qty, price);
            }
        }

        /**
         * Builds a stock contract from the command line arguments.
         * @param specifics: TICKER, common|preferred, { common: lastDividend }, { preferred: parValue, parValueDividendPct }
         * @throws CalculationException if arguments are invalid.
         */
        public EqStockContract Stock(String[] specifics) throws CalculationException {

            StockType type = StockType.valueOf(specifics[1]);

            switch (type) {
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
         */
        public EqIndexContract Index(String[] specifics) {

            return new EqIndexContract(specifics[0], specifics[1].split(","));
        }
    }
}