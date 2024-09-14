package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;
import bg.sofia.uni.fmi.mjt.trading.util.MathHelper;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Portfolio implements PortfolioAPI {
    private final String owner;
    private final PriceChartAPI priceChart;
    private final StockPurchase[] stockPurchases;
    private double budget;
    private final int maxSize;
    private int currentSize;

    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this(owner, priceChart, new StockPurchase[maxSize], budget, maxSize);
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.stockPurchases = Arrays.copyOf(stockPurchases, maxSize);
        this.budget = budget;
        this.maxSize = maxSize;
        this.currentSize = stockPurchases.length;
    }

    /**
     * Purchases the provided quantity of stocks with the provided ticker. The budget in the portfolio should
     * decrease by the corresponding amount. If a stock is on-demand then naturally its price increases.
     * Every stock purchase should result in a 5% price increase of the purchased stock
     *
     * @param stockTicker the stock ticker
     * @param quantity    the quantity of stock that should be purchased
     * @return the stock purchase if it was successfully purchased. If the stock with the provided ticker is
     * not traded on the platform or the ticker is null, return null. If the budget is not enough to make the
     * purchase, return null. If quantity is not a positive number, return null. If the portfolio is already
     * at max size, return null.
     */
    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (stockTicker == null || quantity < 0 || currentSize >= maxSize) {
            return null;
        }

        double stockPrice = priceChart.getCurrentPrice(stockTicker);
        double price = quantity * stockPrice;
        if (budget < price) {
            return null;
        }

        budget -= price;

        StockPurchase stockPurchase = switch (stockTicker) {
            case AmazonStockPurchase.STOCK_TICKER -> new AmazonStockPurchase(quantity, LocalDateTime.now(), stockPrice);
            case GoogleStockPurchase.STOCK_TICKER -> new GoogleStockPurchase(quantity, LocalDateTime.now(), stockPrice);
            case MicrosoftStockPurchase.STOCK_TICKER ->
                    new MicrosoftStockPurchase(quantity, LocalDateTime.now(), stockPrice);
            default -> null;
        };

        if (stockPurchase == null) {
            return null;
        }

        priceChart.changeStockPrice(stockTicker, 5);
        stockPurchases[currentSize++] = stockPurchase;

        return stockPurchase;
    }

    /**
     * @return all stock purchases made so far.
     */
    @Override
    public StockPurchase[] getAllPurchases() {
        return Arrays.copyOf(stockPurchases, currentSize);
    }

    /**
     * Retrieves purchases made in the provided inclusive time interval
     *
     * @param startTimestamp the start timestamp of the interval
     * @param endTimestamp   the end timestamp of the interval
     * @return all stock purchases made so far in the provided time interval
     */
    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        StockPurchase[] stockPurchasesBetween = new StockPurchase[currentSize];
        int count = 0;
        for (int i = 0; i < currentSize; ++i) {
            if (!stockPurchases[i].getPurchaseTimestamp().isBefore(startTimestamp)
                    && !stockPurchases[i].getPurchaseTimestamp().isAfter(endTimestamp)) {
                stockPurchasesBetween[count++] = stockPurchases[i];
            }
        }
        return Arrays.copyOf(stockPurchasesBetween, count);
    }

    /**
     * @return the current total net worth of the portfolio: the sum of each purchases' quantity multiplied by
     * the current price of the stock identified by that purchase rounded to two decimal places
     */
    @Override
    public double getNetWorth() {
        double sum = 0;
        for (int i = 0; i < currentSize; ++i) {
            sum += stockPurchases[i].getQuantity() * priceChart.getCurrentPrice(stockPurchases[i].getStockTicker());
        }
        return MathHelper.round(sum, 2);
    }

    /**
     * @return the remaining budget in the portfolio rounded to two decimal places
     */
    @Override
    public double getRemainingBudget() {
        return MathHelper.round(budget, 2);
    }

    /**
     * @return the owner of the portfolio
     */
    @Override
    public String getOwner() {
        return owner;
    }
}
