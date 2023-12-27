package bg.sofia.uni.fmi.mjt.trading.price;

import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.util.MathHelper;

public class PriceChart implements PriceChartAPI {
    private double microsoftStockPrice;
    private double googleStockPrice;
    private double amazonStockPrice;

    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice) {
        this.microsoftStockPrice = microsoftStockPrice;
        this.googleStockPrice = googleStockPrice;
        this.amazonStockPrice = amazonStockPrice;
    }

    /**
     * Gets the current price of the stock identified by the provided stock ticker rounded to two decimal places
     *
     * @param stockTicker the stock ticker
     * @return current price of stock. If the stock with the provided ticker is not traded on the platform
     * or the ticker is null, return 0.0
     */
    @Override
    public double getCurrentPrice(String stockTicker) {
        if (stockTicker == null) {
            return 0d;
        }
        return switch (stockTicker) {
            case AmazonStockPurchase.STOCK_TICKER -> MathHelper.round(amazonStockPrice, 2);
            case GoogleStockPurchase.STOCK_TICKER -> MathHelper.round(googleStockPrice, 2);
            case MicrosoftStockPurchase.STOCK_TICKER -> MathHelper.round(microsoftStockPrice, 2);
            default -> 0d;
        };
    }

    /**
     * Changes the current price of the stock identified by the provided stock ticker by the provided percentage.
     * As we are creating a thriving trading platform, the percentage can only be a positive number
     *
     * @param stockTicker   the ticker of the stock for which the price is changing
     * @param percentChange positive number denoting the percentage increase of stock price
     * @return true, if the price was increased successfully. If the stock with the provided ticker is not traded
     * on the platform or the ticker is null, return false. If the provided percentChange is not a positive
     * number, return false.
     */
    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange) {
        if (stockTicker == null || percentChange < 0) {
            return false;
        }
        return switch (stockTicker) {
            case AmazonStockPurchase.STOCK_TICKER -> {
                amazonStockPrice *= 1d + percentChange / 100d;
                yield true;
            }
            case GoogleStockPurchase.STOCK_TICKER -> {
                googleStockPrice *= 1d + percentChange / 100d;
                yield true;
            }
            case MicrosoftStockPurchase.STOCK_TICKER -> {
                microsoftStockPrice *= 1d + percentChange / 100d;
                yield true;
            }
            default -> false;
        };
    }
}
