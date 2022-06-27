package model;

public class PurchaseOrder {
    private String phoId;
    private String date;
    private String time;
    private String purchaseShopId;

    public PurchaseOrder() {
    }

    public PurchaseOrder(String phoId, String date, String time, String purchaseShopId) {
        this.phoId = phoId;
        this.date = date;
        this.time = time;
        this.purchaseShopId = purchaseShopId;
    }

    public String getPhoId() {
        return phoId;
    }

    public void setPhoId(String phoId) {
        this.phoId = phoId;
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

    public String getPurchaseShopId() {
        return purchaseShopId;
    }

    public void setPurchaseShopId(String purchaseShopId) {
        this.purchaseShopId = purchaseShopId;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "phoId='" + phoId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", purchaseShopId='" + purchaseShopId + '\'' +
                '}';
    }
}
