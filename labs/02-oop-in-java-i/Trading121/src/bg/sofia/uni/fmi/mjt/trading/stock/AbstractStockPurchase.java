package bg.sofia.uni.fmi.mjt.trading.stock;

import bg.sofia.uni.fmi.mjt.trading.util.MathHelper;

import java.time.LocalDateTime;

public class AbstractStockPurchase implements StockPurchase {
    private final String ticker;
    private final int quantity;
    private final LocalDateTime purchaseTimestamp;
    private final double purchasePricePerUnit;

    public AbstractStockPurchase(String ticker, int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit() {
        return MathHelper.round(purchasePricePerUnit, 2);
    }

    @Override
    public double getTotalPurchasePrice() {
        return MathHelper.round(quantity * purchasePricePerUnit, 2);
    }

    @Override
    public String getStockTicker() {
        return ticker;
    }
}
