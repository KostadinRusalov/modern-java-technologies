package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public class MicrosoftStockPurchase extends AbstractStockPurchase {
    public static final String STOCK_TICKER = "MSFT";
    public MicrosoftStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        super(STOCK_TICKER, quantity, purchaseTimestamp, purchasePricePerUnit);
    }
}
