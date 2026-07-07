package ch.kris.model;

public class StoreOrder {
    private Long orderId;
    private Long uid;
    private Long packageId;
    private String status;
    private int priceChfCents;
    private int creditedAmount;
    private int bonusAmount;
    private boolean firstTimePurchaseBonus;

    public StoreOrder() {
    }

    public StoreOrder(Long orderId, Long uid, Long packageId, String status, int priceChfCents, int creditedAmount, int bonusAmount, boolean firstTimePurchaseBonus) {
        this.orderId = orderId;
        this.uid = uid;
        this.packageId = packageId;
        this.status = status;
        this.priceChfCents = priceChfCents;
        this.creditedAmount = creditedAmount;
        this.bonusAmount = bonusAmount;
        this.firstTimePurchaseBonus = firstTimePurchaseBonus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriceChfCents() {
        return priceChfCents;
    }

    public void setPriceChfCents(int priceChfCents) {
        this.priceChfCents = priceChfCents;
    }

    public int getCreditedAmount() {
        return creditedAmount;
    }

    public void setCreditedAmount(int creditedAmount) {
        this.creditedAmount = creditedAmount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(int bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public boolean isFirstTimePurchaseBonus() {
        return firstTimePurchaseBonus;
    }

    public void setFirstTimePurchaseBonus(boolean firstTimePurchaseBonus) {
        this.firstTimePurchaseBonus = firstTimePurchaseBonus;
    }
}
