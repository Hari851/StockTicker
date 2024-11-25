package com.example.stockticker.service;

import com.example.stockticker.model.Stock;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StockDataFetcherService {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${alphavantage.api.key}")
    private String apiKey;  // Inject Alpha Vantage API key

    public void fetchAndPersistStockData(String symbol) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.alphavantage.co/query")
                .queryParam("function", "TIME_SERIES_INTRADAY")
                .queryParam("symbol", symbol)
                .queryParam("interval", "5min")
                .queryParam("apikey", apiKey)
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);

        JsonNode rootNode;
        try {
            rootNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
            JsonNode timeSeriesNode = rootNode.path("Time Series (5min)");

            if (timeSeriesNode.isObject()) {
                String latestTime = timeSeriesNode.fieldNames().next();
                JsonNode latestData = timeSeriesNode.get(latestTime);

                double price = latestData.path("4. close").asDouble();
                double change = latestData.path("4. close").asDouble() - latestData.path("1. open").asDouble();
                long volume = latestData.path("5. volume").asLong();

                persistStockData(symbol, price, change, volume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void persistStockData(String symbol, double price, double change, long volume) {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable("StockData");

        Item item = new Item()
                .withPrimaryKey("symbol", symbol)
                .withDouble("price", price)
                .withDouble("change", change)
                .withLong("volume", volume);

        table.putItem(item);
    }
}
