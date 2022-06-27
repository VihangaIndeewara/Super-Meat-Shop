package model;

public class PurchaseOrderDetail {
    private String purchaseOrderId;
    private String meatId;
    private int qty;
    private double price;
    private double total;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(String purchaseOrderId, String meatId, int qty, double price, double total) {
        this.purchaseOrderId = purchaseOrderId;
        this.meatId = meatId;
        this.qty = qty;
        this.price = price;
        this.total = total;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getMeatId() {
        return meatId;
    }

    public void setMeatId(String meatId) {
        this.meatId = meatId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetail{" +
                "purchaseOrderId='" + purchaseOrderId + '\'' +
                ", meatId='" + meatId + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
