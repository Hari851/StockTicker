package com.example.stockticker.controller;

import com.example.stockticker.model.Stock;
import com.example.stockticker.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{symbol}")
    public Stock getStockData(@PathVariable String symbol) {
        return stockService.getStockData(symbol);
    }
}
