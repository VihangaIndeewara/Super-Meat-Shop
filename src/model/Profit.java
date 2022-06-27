package model;

public class Profit {
    private String  profitId;
    private String  orderId;
    private double purchasePrice;
    private double sellingPrice;
    private Double amount;

    public Profit() {
    }

    public Profit(String profitId, String orderId, double purchasePrice, double sellingPrice, Double amount) {
        this.profitId = profitId;
        this.orderId = orderId;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.amount = amount;
    }

    public String getProfitId() {
        return profitId;
    }

    public void setProfitId(String profitId) {
        this.profitId = profitId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Profit{" +
                "profitId='" + profitId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", sellingPrice=" + sellingPrice +
                ", amount=" + amount +
                '}';
    }
}
