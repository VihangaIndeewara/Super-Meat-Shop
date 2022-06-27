package views.TM;

import javafx.scene.control.Button;

public class CartTM {
    private String meatId;
    private String description;
    private int qty;
    private Double price;
    private Double amount;
    private Button btn;

    public CartTM() {
    }

    public CartTM(String meatId, String description, int qty, Double price, Double amount, Button btn) {
        this.meatId = meatId;
        this.description = description;
        this.qty = qty;
        this.price = price;
        this.amount = amount;
        this.btn = btn;
    }

    public String getMeatId() {
        return meatId;
    }

    public void setMeatId(String meatId) {
        this.meatId = meatId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "OrderCartTM{" +
                "meatId='" + meatId + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", amount=" + amount +
                ", btn=" + btn +
                '}';
    }
}
