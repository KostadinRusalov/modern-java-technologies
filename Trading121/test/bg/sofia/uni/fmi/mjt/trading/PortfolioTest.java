package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PortfolioTest {
    private PriceChartAPI priceChart;
    private PortfolioAPI portfolio;

    @BeforeEach
    void setUp() {
        priceChart = new PriceChart(5, 5, 5);
    }

    @Test
    void testGetNetWorthWithNoStocks() {
        portfolio = new Portfolio("Koce", priceChart, 100, 0);
        assertEquals(0d, portfolio.getNetWorth());
    }

    @Test
    void testBuyStockWhenMaxSizeIsZero() {
        portfolio = new Portfolio("Koce", priceChart, 100, 0);
        assertNull(portfolio.buyStock("AMZ", 5));
    }

    @Test
    void testGetAllPurchasesWithOutsideTimeFrame() {
        StockPurchase[] stockPurchases = new StockPurchase[]{
                new AmazonStockPurchase(5, LocalDateTime.of(2023, 10, 4, 10, 0), 10),
                new AmazonStockPurchase(5, LocalDateTime.of(2023, 11, 14, 10, 0), 10),
                new AmazonStockPurchase(5, LocalDateTime.of(2022, 4, 24, 10, 0), 10),
                new AmazonStockPurchase(5, LocalDateTime.of(2022, 12, 24, 10, 0), 10),
        };
        portfolio = new Portfolio("Koce", priceChart, stockPurchases, 100, 10);

        var purchases = portfolio.getAllPurchases(LocalDateTime.of(2010, 1,1,1,1),
                LocalDateTime.of(2012,1,1,1,1));
        assertEquals(0, purchases.length);
    }
    @Test
    void testGetAllPurchasesWithOutsideTimeFrameA() {
        StockPurchase[] stockPurchases = new StockPurchase[]{
                new AmazonStockPurchase(5, LocalDateTime.of(2023, 10, 4, 10, 0), 10),
                new AmazonStockPurchase(5, LocalDateTime.of(2023, 11, 14, 10, 0), 10),
                new AmazonStockPurchase(5, LocalDateTime.of(2022, 4, 24, 10, 0), 10),
                new AmazonStockPurchase(5, LocalDateTime.of(2022, 12, 24, 10, 0), 10),
        };
        portfolio = new Portfolio("Koce", priceChart, stockPurchases, 100, 10);

        var purchases = portfolio.getAllPurchases(LocalDateTime.of(2010, 1,1,1,1),
                LocalDateTime.of(2012,1,1,1,1));
        assertEquals(0, purchases.length);
    }

    @Test
    void testGetAllPurchasesWithNoPurchases() {
        portfolio = new Portfolio("koce", priceChart, 100,0);
        assertEquals(0, portfolio.getAllPurchases().length);
    }
}
