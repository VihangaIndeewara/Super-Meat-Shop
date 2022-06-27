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
import model.OrderDetail;
import model.Orders;
import model.Profit;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import views.TM.CartTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class OrderFormController {
    public TableView<CartTM> tblOrderForm;
    public TableColumn colMeatId;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colPrice;
    public TableColumn colAmount;
    public TableColumn colRemove;
    public JFXComboBox<String> cmbClientId;
    public JFXTextField txtClientName;
    public JFXTextField txtClientAddress;
    public JFXTextField txtClientType;
    public JFXComboBox<String> cmbMeatId;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtSellingPrice;
    public JFXTextField txtQty;
    public JFXTextField txtOrId;
    public Label lblTotal;
    public Button btnPlaceOrder;
    public Button btnAddToCart;
    public Button btnPrintBill;

    String clientMobileNo;
    String profitId;
    String orderId;
    Double sellingPrice = 0.00;
    Double purchasePrice=0.00;

    LinkedHashMap<JFXTextField, Pattern> map=new LinkedHashMap<>();


    public void initialize() {
        setOrderId();

        setClientId();
        setMeatId();

        btnPlaceOrder.setDisable(true);
        btnPrintBill.setDisable(true);

        Pattern qtyPattern =Pattern.compile("^[1-9]{1}[0-9]*$");
        map.put(txtQty,qtyPattern);


        cmbClientId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setClientDetail(newValue);
        });
        cmbMeatId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setMeatDetail(newValue);
        });


        colMeatId.setCellValueFactory(new PropertyValueFactory<>("meatId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    private void setOrderId() {
        try {
            String sql = "SELECT oId FROM Orders ORDER BY oId DESC LIMIT 1";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
            String newId = "OR001";

            while (result.next()) {
                String id = result.getString(1);
                int num = Integer.parseInt(id.substring(2));
                num++;

                if (num <= 9) {
                    newId = "OR00" + num;
                } else if (num > 9 && num < 100) {
                    newId = "OR0" + num;
                } else if (num >= 100) {
                    newId = "OR" + num;
                }
            }
            txtOrId.setText(newId);


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setMeatDetail(String selectedId) {
        try {
            String sql = "SELECT description,qtyOnHand,sellingPrice FROM Meat WHERE mId=?";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1, selectedId);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                txtDescription.setText(resultSet.getString(1));
                txtQtyOnHand.setText(resultSet.getString(2));
                txtSellingPrice.setText(resultSet.getString(3));
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

    private void setClientDetail(String selectedId) {
        try {
            String sql = "SELECT name,address,type,mobileNo FROM Client WHERE cId=?";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1, selectedId);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                txtClientName.setText(resultSet.getString(1));
                txtClientAddress.setText(resultSet.getString(2));
                txtClientType.setText(resultSet.getString(3));
                clientMobileNo=resultSet.getString(4);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setClientId() {
        try {
            String sql = "SELECT cId FROM CLIENT ";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();

            ObservableList<String> obList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                obList.add(
                        resultSet.getString(1)
                );
            }
            cmbClientId.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void txtKeyReleased(KeyEvent keyEvent) throws Exception {
        validation();

        if (keyEvent.getCode()== KeyCode.ENTER){
           Object responce = validation();

           if (responce instanceof JFXTextField){
              JFXTextField t= (JFXTextField) responce;
              t.requestFocus();
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
        return null;
    }

    private void error(JFXTextField textField) {
        if(textField.getText().length()>0){
            textField.setFocusColor(Paint.valueOf("red"));
        }
        btnAddToCart.setDisable(true);
    }

    private void accept(JFXTextField textField) {
        textField.setFocusColor(Paint.valueOf("#08b506"));

        txtQty.getText();

        if(Integer.parseInt(txtQty.getText()) >Integer.parseInt(txtQtyOnHand.getText())){
            btnAddToCart.setDisable(true);
            textField.setFocusColor(Paint.valueOf("red"));
        }else {
            btnAddToCart.setDisable(false);
            textField.setFocusColor(Paint.valueOf("#08b506"));
        }
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) throws Exception {
        addCart();
    }

    ObservableList<CartTM> obList = FXCollections.observableArrayList();

    public void addCart() throws Exception {
        if (cmbClientId.getValue()==null||cmbMeatId.getValue()==null || txtQty.getText().length()<=0){
            if (cmbClientId.getValue()==null){
                new Alert(Alert.AlertType.WARNING,"Not Selected Client Id").show();
            }else if (cmbMeatId.getValue()==null){
                new Alert(Alert.AlertType.WARNING,"Not Selected Meat ID").show();
            }else if(txtQty.getText().length()<=0){
                new Alert(Alert.AlertType.WARNING,"Not Entered Qty").show();
            }
        }else {
            btnPlaceOrder.setDisable(false);

            int qty = Integer.parseInt(txtQty.getText());
            double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
            double tot = qty * sellingPrice;

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
                        sellingPrice,
                        tot,
                        btn
                );


                btn.setOnAction((event) -> {
                    obList.removeIf(cartTM -> cartTM.getMeatId().equals(tm.getMeatId()));
                });

                obList.add(tm);
                tblOrderForm.setItems(obList);
            }
            calculateTotal();
            tblOrderForm.refresh();
            orderId = txtOrId.getText();
            clearTexts();
        }
    }





    private CartTM isExists(String meatId) {
        for (CartTM tm : obList) {
            if (tm.getMeatId().equals(meatId)) {
                return tm;
            }
        }
        return null;
    }


    private void calculateTotal() {
        double total = 0;

        for (CartTM t : obList) {
            total += t.getAmount();
        }
        lblTotal.setText(String.valueOf(total));
    }


    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        Orders o = new Orders(txtOrId.getText(), String.valueOf(LocalDate.now()), String.valueOf(LocalTime.now()), cmbClientId.getValue());


        try {
            String sql = "INSERT INTO Orders VALUES (?,?,?,?)";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1, o.getOrderId());
            stm.setObject(2, o.getDate());
            stm.setObject(3, o.getTime());
            stm.setObject(4, o.getClientId());
            if (stm.executeUpdate() > 0) {
                setOrderDetails();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void setOrderDetails() {
        ArrayList<OrderDetail> details = new ArrayList<>();

        for (CartTM tm : obList) {
            details.add(new OrderDetail(
                    txtOrId.getText(),
                    tm.getMeatId(),
                    tm.getQty(),
                    tm.getPrice(),
                    tm.getAmount()
            ));
        }
        for (OrderDetail t : details) {
            try {
                String sql = "INSERT INTO OrderDetail VALUES (?,?,?,?,?)";
                PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
                stm.setObject(1, t.getOrderId());
                stm.setObject(2, t.getMeatId());
                stm.setObject(3, t.getQty());
                stm.setObject(4, t.getPrice());
                stm.setObject(5, t.getTotal());

                if (stm.executeUpdate() > 0) {
                    updateQty(t.getQty(), t.getMeatId());
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Saved!!!").show();
                    btnPlaceOrder.setDisable(true);
                    btnPrintBill.setDisable(false);

                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        setOrderId();
        saveProfit();
    }

    private void updateQty(int qty, String meatId) {
        try {
            String sql = "UPDATE meat SET qtyOnHand=qtyOnHand-? WHERE mId=?";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1, qty);
            stm.setObject(2, meatId);
            stm.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void clearClientTexts() {
        cmbClientId.getSelectionModel().clearSelection();
        txtClientName.clear();
        txtClientAddress.clear();
        txtClientType.clear();
        obList.clear();
        lblTotal.setText(String.valueOf(0.00));

        purchasePrice=0.00;
    }

    private void clearTexts() {
        cmbMeatId.getSelectionModel().clearSelection();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        txtSellingPrice.clear();

        cmbMeatId.requestFocus();

        txtQty.setFocusColor(Paint.valueOf("#4059a9"));
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) {
        LinkedHashMap map = new LinkedHashMap();

        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/reports/OrderReport.jasper"));

            map.put("imageUrl", "asserts/badge.png");
            map.put("id", orderId);
            map.put("date", String.valueOf(LocalDate.now()));
            map.put("time", String.valueOf(LocalTime.now()));
            map.put("name", txtClientName.getText());
            map.put("address", txtClientAddress.getText());
            map.put("contactNo", clientMobileNo);
            map.put("total", Double.parseDouble(lblTotal.getText()));

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, new JRBeanCollectionDataSource(obList));

            JasperViewer.viewReport(jasperPrint, false);


        } catch (JRException e) {
            e.printStackTrace();
        }

        btnPlaceOrder.setDisable(true);
        btnPrintBill.setDisable(true);
        clearClientTexts();


    }


    public void saveProfit() {
        setProfitId();
        totalOrderSellingPrice();
        totalOrderPurchasePrice();

        Double amountPrice = sellingPrice - purchasePrice;


        Profit p = new Profit(profitId, orderId, purchasePrice, sellingPrice, amountPrice);
        try {
            String sql = "INSERT INTO Profit VALUES (?,?,?,?,?);";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1, p.getProfitId());
            stm.setObject(2, p.getOrderId());
            stm.setObject(3, p.getPurchasePrice());
            stm.setObject(4, p.getSellingPrice());
            stm.setObject(5, p.getAmount());
            stm.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void setProfitId() {
        try {
            String sql = "SELECT pId FROM Profit ORDER BY pId DESC LIMIT 1";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
            String newId = "P001";

            while (result.next()) {
                String id = result.getString(1);
                int num = Integer.parseInt(id.substring(1));
                num++;

                if (num <= 9) {
                    newId = "P00" + num;
                } else if (num > 9 && num < 100) {
                    newId = "P0" + num;
                } else if (num >= 100) {
                    newId = "P" + num;
                }
            }
            profitId = newId;


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void totalOrderSellingPrice() {
        try {
            String sql = "SELECT sum(total) FROM  OrderDetail WHERE orderId=?";
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1, orderId);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                sellingPrice = result.getDouble(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

//    public void totalOrderPurchasePrice() {
//        try {
//            String sql = "SELECT  qty,meatId FROM  OrderDetail WHERE orderId=?";
//            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//            stm.setObject(1, orderId);
//            ResultSet set = stm.executeQuery();
//
//            while (set.next()) {
//                int qty = set.getInt(1);
//
//                try {
//                    String sql2 = "SELECT purchasePrice FROM  Meat WHERE mId=?";
//                    PreparedStatement stm2 = DBConnection.getInstance().getConnection().prepareStatement(sql2);
//                    stm2.setObject(1, set.getString(2));
//                    ResultSet resultSet = stm2.executeQuery();
//
//                    while (resultSet.next()) {
//                        double price = resultSet.getDouble(1);
//                        purchasePrice =+ qty * price;
//                    }
//                } catch (SQLException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


    public  void  totalOrderPurchasePrice(){
        try {
            String sql="SELECT qty,meatId FROM OrderDetail WHERE orderId=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,orderId);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                try {
                    String sql2 ="SELECT  purchasePrice FROM Meat WHERE  mId=?";
                    PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql2);
                    statement.setObject(1,resultSet.getString(2));
                    ResultSet set = statement.executeQuery();

                    while (set.next()){
//                       double p=  set.getDouble(1);
//                       int q=resultSet.getInt(1);
                        purchasePrice += set.getDouble(1)*resultSet.getInt(1);
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
