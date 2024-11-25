package com.example.stockticker.scheduler;

import com.example.stockticker.service.StockDataFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockDataScheduler {

    @Autowired
    private StockDataFetcherService stockDataFetcherService;

    // Fetch stock data every 5 minutes
    @Scheduled(fixedRate = 300000)  // 5 minutes in milliseconds
    public void fetchStockData() {
        stockDataFetcherService.fetchAndPersistStockData("AAPL");  // Example: Fetch Apple stock (AAPL)
    }
}
