package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public class GoogleStockPurchase extends AbstractStockPurchase {
    public static final String STOCK_TICKER = "GOOG";
    public GoogleStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        super(STOCK_TICKER, quantity, purchaseTimestamp, purchasePricePerUnit);
    }
}
