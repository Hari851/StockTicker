package com.example.stockticker.service;

import com.example.stockticker.model.Stock;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public Stock getStockData(String symbol) {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable("StockData");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("symbol", symbol).withConsistentRead(true);
        Item item = table.getItem(spec);

        if (item == null) {
            throw new RuntimeException("Stock not found for symbol: " + symbol);
        }

        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setPrice(item.getDouble("price"));
        stock.setChange(item.getDouble("change"));
        stock.setVolume(item.getLong("volume"));

        return stock;
    }
}
