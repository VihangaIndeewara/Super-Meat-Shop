package views.TM;

public class DetailCartTM {
    private String orderId;
    private String clientId;
    private String meatId;
    private int qty;
    private double price;
    private String date;
    private String time;

    public DetailCartTM() {
    }

    public DetailCartTM(String orderId, String clientId, String meatId, int qty, double price, String date, String time) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.meatId = meatId;
        this.qty = qty;
        this.price = price;
        this.date = date;
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DetailCartTM{" +
                "orderId='" + orderId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", meatId='" + meatId + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
