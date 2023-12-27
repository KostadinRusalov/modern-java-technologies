package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public class AmazonStockPurchase extends AbstractStockPurchase {
    public static final String STOCK_TICKER = "AMZ";
    public AmazonStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        super(STOCK_TICKER, quantity, purchaseTimestamp, purchasePricePerUnit);
    }
}
