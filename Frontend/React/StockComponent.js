import React, { useEffect, useState } from "react";
import axios from "axios";

const StockComponent = () => {
  const [stockData, setStockData] = useState(null);
  const [symbol, setSymbol] = useState("AAPL"); // Default stock symbol

  useEffect(() => {
    // Fetch stock data when the component mounts
    fetchStockData(symbol);
  }, [symbol]);

  const fetchStockData = async (symbol) => {
    try {
      const response = await axios.get(
        `https://<api-id>.execute-api.<region>.amazonaws.com/prod/api/stock/${symbol}`
      );
      setStockData(response.data);
    } catch (error) {
      console.error("Error fetching stock data", error);
    }
  };

  return (
    <div>
      <h1>Stock Data for {symbol}</h1>
      <div>
        <label>Stock Symbol:</label>
        <input
          type="text"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value.toUpperCase())}
        />
      </div>

      {stockData ? (
        <div>
          <p>Price: ${stockData.price}</p>
          <p>Change: ${stockData.change}</p>
          <p>Volume: {stockData.volume}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default StockComponent;
