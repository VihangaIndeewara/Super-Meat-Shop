package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import views.TM.CartTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class PurchaseOrderFormController {
    public TableView<CartTM> tblPurchaseForm;
    public TableColumn colMeatId;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colPrice;
    public TableColumn colAmount;
    public TableColumn colRemove;
    public JFXComboBox<String> cmbPurchaseShopId;
    public JFXTextField txtName;
    public JFXTextField txtFarmName;
    public JFXTextField txtMeatType;
    public JFXComboBox<String> cmbMeatId;
    public JFXTextField txtDescription;
    public JFXTextField txtPurchasePrice;
    public JFXTextField txtQty;
    public JFXTextField txtOrId;
    public Label lblTotal;
    public Button btnAddToCart;
    public Button btnPlaceOrder;

    LinkedHashMap<JFXTextField, Pattern> map=new LinkedHashMap<>();

    public void initialize(){
        setOrderId();

        setPurchaseShopId();
        setMeatId();

        btnPlaceOrder.setDisable(true);

        cmbPurchaseShopId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbPurchaseShopDetail(newValue);
        });
        cmbMeatId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setMeatDetail(newValue);
        });


        Pattern pricePattern=Pattern.compile("^[1-9]{1}[0-9]*(.00)?$");
        Pattern qtyPattern=Pattern.compile("^[1-9]{1}[0-9]*$");
        map.put(txtPurchasePrice,pricePattern);
        map.put(txtQty,qtyPattern);


        colMeatId.setCellValueFactory(new PropertyValueFactory<>("meatId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    private void setMeatDetail(String selectedId) {
        try {
            String sql = "SELECT description,qtyOnHand,sellingPrice FROM Meat WHERE mId=?";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,selectedId);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                txtDescription.setText(resultSet.getString(1));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cmbPurchaseShopDetail(String selectedId) {
        try {
            String sql = "SELECT name,farmName,meatType FROM PurchaseShop WHERE phsId=?";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,selectedId);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                txtName.setText(resultSet.getString(1));
                txtFarmName.setText(resultSet.getString(2));
                txtMeatType.setText(resultSet.getString(3));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setMeatId() {
        try {
            String sql = "SELECT mId FROM Meat ";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();

            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                obList.add(
                        resultSet.getString(1)
                );
            }
            cmbMeatId.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void setPurchaseShopId() {
        try {
            String sql = "SELECT phsId FROM PurchaseShop ";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();

            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                obList.add(
                        resultSet.getString(1)
                );
            }
            cmbPurchaseShopId.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setOrderId() {
        try {
            String sql="SELECT phoId FROM purchaseOrder ORDER BY phoId DESC LIMIT 1";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
            String newId="PHO001";

            while (result.next()) {
                String id = result.getString(1);
                int num = Integer.parseInt(id.substring(3));
                num++;

                if (num < 10) {
                    newId = "PHO00"+num;
                } else if (num >= 10 && num <= 99) {
                    newId = "PHO0"+num;
                } else if (num > 99) {
                    newId = "PHO"+num;
                }
            }
            txtOrId.setText(newId);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtKeyReleased(KeyEvent keyEvent) {
        validation();

        if (keyEvent.getCode()== KeyCode.ENTER){
           Object responce= validation();

           if (responce instanceof  JFXTextField){
              JFXTextField textField=(JFXTextField) responce;
              textField.requestFocus();
           }else  if (responce instanceof Boolean){
               addCart();
           }
        }
    }

    public Object validation(){
        for (JFXTextField t: map.keySet()){
            Pattern pattern = map.get(t);

            if (pattern.matcher(t.getText()).matches()){
                accept(t);
            }else {
                error(t);
                return t;
            }
        }
        return true;
    }

    private void error(JFXTextField textField) {
        if(textField.getText().length()>0){
            textField.setFocusColor(Paint.valueOf("red"));
        }
        btnAddToCart.setDisable(true);
    }

    private void accept(JFXTextField textField) {
        textField.setFocusColor(Paint.valueOf("#08b506"));
        btnAddToCart.setDisable(false);
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        addCart();
    }

    ObservableList<CartTM> obList=FXCollections.observableArrayList();

    public void addCart(){
        if (cmbPurchaseShopId.getValue()==null||cmbMeatId.getValue()==null||txtPurchasePrice.getText().length()<=0||(txtQty.getText().length()<=0)){
            if (cmbPurchaseShopId.getValue()==null){
                new Alert(Alert.AlertType.WARNING,"Not Selected Purchase Shop Id").show();
            }else if(cmbMeatId.getValue()==null){
                new Alert(Alert.AlertType.WARNING,"Not Selected Meat Id").show();
            }else if(txtPurchasePrice.getText().length()<=0){
                new Alert(Alert.AlertType.WARNING,"Not Entered Purchase Price").show();
            }else if (txtQty.getText().length()<=0){
                new Alert(Alert.AlertType.WARNING,"Not Entered Qty").show();
            }
        }else {
            btnPlaceOrder.setDisable(false);

            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtPurchasePrice.getText());
            double tot = qty * price;

            Button btn = new Button("remove");
            btn.setStyle("-fx-background-color: red");

            CartTM isExist = isExists(cmbMeatId.getValue());

            if (isExist != null) {
                for (CartTM t : obList) {
                    if (t.equals(isExist)) {
                        t.setQty(t.getQty() + qty);
                        t.setAmount(t.getAmount() + tot);
                    }
                }
            } else {

                CartTM tm = new CartTM(
                        cmbMeatId.getValue(),
                        txtDescription.getText(),
                        qty,
                        price,
                        tot,
                        btn
                );


                btn.setOnAction(event -> {
                    obList.removeIf(cartTM -> cartTM.getMeatId().equals(tm.getMeatId()));
                });

                obList.add(tm);
                tblPurchaseForm.setItems(obList);
            }
            btnAddToCart.setDisable(true);
            tblPurchaseForm.refresh();
            calculateTotal();
            clearTexts();
        }
    }



    private CartTM isExists(String meatId) {
        for (CartTM tm:obList){
            if (tm.getMeatId().equals(meatId)){
                return tm;
            }
        }
        return null;

    }

    private void calculateTotal() {
        double total=0;

        for (CartTM t :obList){
            total+=t.getAmount();
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void clearTexts() {
        cmbMeatId.getSelectionModel().clearSelection();
        txtDescription.clear();
        txtPurchasePrice.clear();
        txtQty.clear();

        cmbMeatId.requestFocus();

        txtPurchasePrice.setFocusColor(Paint.valueOf("#4059a9"));
        txtQty.setFocusColor(Paint.valueOf("#4059a9"));

    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        PurchaseOrder order=new PurchaseOrder(txtOrId.getText(),String.valueOf(LocalDate.now()),String.valueOf(LocalTime.now()),cmbPurchaseShopId.getValue());

        try {
            String sql = "INSERT INTO PurchaseOrder VALUES (?,?,?,?)";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,order.getPhoId());
            stm.setObject(2,order.getDate());
            stm.setObject(3,order.getTime() );
            stm.setObject(4,order.getPurchaseShopId() );
            if(stm.executeUpdate()>0){
                setOrderDetails();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setOrderDetails() {
        ArrayList<PurchaseOrderDetail> details=new ArrayList<>();

        for (CartTM tm:obList){
            details.add(new PurchaseOrderDetail(
                    txtOrId.getText(),
                    tm.getMeatId(),
                    tm.getQty(),
                    tm.getPrice(),
                    tm.getAmount()
            ));
        }
        for (PurchaseOrderDetail t:details) {
            try {
                String sql = "INSERT INTO PurchaseOrderDetail VALUES (?,?,?,?,?)";
                PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
                stm.setObject(1, t.getPurchaseOrderId());
                stm.setObject(2, t.getMeatId());
                stm.setObject(3, t.getQty());
                stm.setObject(4, t.getPrice());
                stm.setObject(5, t.getTotal());

                if(stm.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Purchase Order Saved!!!").show();
                    clearPurchaseShopTexts();
                    setOrderId();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        btnPlaceOrder.setDisable(true);
    }

    private void clearPurchaseShopTexts() {
        cmbPurchaseShopId.getSelectionModel().clearSelection();
        txtName.clear();
        txtFarmName.clear();
        txtMeatType.clear();
        obList.clear();
        lblTotal.setText(String.valueOf(0.00));

    }


}
