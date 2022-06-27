package model;

public class OrderDetail {
    private String orderId;
    private String meatId;
    private int qty;
    private double price;
    private double total;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String meatId, int qty, double price, double total) {
        this.orderId = orderId;
        this.meatId = meatId;
        this.qty = qty;
        this.price = price;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                ", meatId='" + meatId + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
