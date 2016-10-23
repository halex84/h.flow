package com.h.services;

import com.h.contracts.TradeContract;
import com.h.mkt.data.Desk;
import com.h.repositories.InMemoryTradeRepository;
import com.h.repositories.SimpleStockTradingRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * TradeDispatcher API
 * Created by halex on 10/22/16.
 */
public class TradeDispatcher {

    /**
     * Books a trade.
     */
    public static void bookTrade(TradeContract trade) throws InvalidArgumentException {

        SimpleStockTradingRepository repository = InMemoryTradeRepository.getInstance();
        Desk tradeDesk = repository.getOrAddDeskById(trade.deskId);
        switch (trade.buySell){
            case buy:
                tradeDesk.buy(repository, trade.ticker, trade.qty, trade.price);
                break;
            case sell:
                tradeDesk.sell(repository, trade.ticker, trade.qty, trade.price);
                break;
        }
    }
}