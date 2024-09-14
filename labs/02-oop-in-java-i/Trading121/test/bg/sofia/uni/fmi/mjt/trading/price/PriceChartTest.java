package bg.sofia.uni.fmi.mjt.trading.price;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriceChartTest {
    private PriceChartAPI priceChart;

    @BeforeEach
    void setUp() {
        priceChart = new PriceChart(4.694, 4.195, 3.813);
    }

    @Test
    void testGetCurrentPriceWithNullTicker() {
        assertEquals(0d, priceChart.getCurrentPrice(null));
    }

    @Test
    void testGetCurrentPriceWithAmazon() {
        assertEquals(3.81, priceChart.getCurrentPrice("AMZ"));
    }

    @Test
    void testGetCurrentPriceWithGoogle() {
        assertEquals(4.20, priceChart.getCurrentPrice("GOOG"));
    }

    @Test
    void testGetCurrentPriceOfMicrosoft() {
        assertEquals(4.69, priceChart.getCurrentPrice("MSFT"));
    }

    @Test
    void testGetCurrentPriceOfUnsupportedTicker() {
        assertEquals(0d, priceChart.getCurrentPrice("KOCE"));
    }

    @Test
    void testChangeStockPriceWithNegativePercent() {
        assertFalse(priceChart.changeStockPrice("KOCE", -11));
    }

    @Test
    void testChangeStockPriceOfUnsupportedTicker() {
        assertFalse(priceChart.changeStockPrice("KOCE", 11));
    }

    @Test
    void testChangeStockPriceOfAmazon() {
        assertTrue(priceChart.changeStockPrice("AMZ", 5));
        assertEquals(4d, priceChart.getCurrentPrice("AMZ"));
    }

    @Test
    void testChangeStockPriceOfGoogle() {
        assertTrue(priceChart.changeStockPrice("GOOG", 6));
        assertEquals(4.45, priceChart.getCurrentPrice("GOOG"));
    }

    @Test
    void testChangeStockPriceOfMicrosoft() {
        assertTrue(priceChart.changeStockPrice("MSFT", 12));
        assertEquals(5.26, priceChart.getCurrentPrice("MSFT"));
    }
}
