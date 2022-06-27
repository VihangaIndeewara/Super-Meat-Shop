package model;

public class Orders {
    private String  orderId;
    private String date;
    private String time;
    private String clientId;

    public Orders() {
    }

    public Orders(String orderId, String date, String time, String clientId) {
        this.orderId = orderId;
        this.date = date;
        this.time = time;
        this.clientId = clientId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
