package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import model.Employee;
import model.Meat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class MeatFormController {
    public TableView<Meat> tblMeatForm;
    public TableColumn colId;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colPurchasePrice;
    public TableColumn colSellingPrice;
    public JFXTextField txtMeatId;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtPurchasePrice;
    public JFXTextField txtSellingPrice;
    public JFXTextField txtUpdateId;
    public JFXTextField txtUpdateDescription;
    public JFXTextField txtUpdateQtyOnHand;
    public JFXTextField txtUpdatePurchasePrice;
    public JFXTextField txtUpdateSellingPrice;
    public Button btnSave;

    LinkedHashMap<JFXTextField, Pattern> hashMap=new LinkedHashMap<>();

    public  void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("meatId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        btnSave.setDisable(true);

        Pattern idPattern = Pattern.compile("^M[0-9]{3}$");
        Pattern descriptionPattern = Pattern.compile("^[A-z () /]{3,50}$");
        Pattern qtyOnHandPattern = Pattern.compile("^[1-9]{1}[0-9]*$");
        Pattern purchasePricePattern = Pattern.compile("^[1-9]{1}[0-9]*(.00)?$");
        Pattern sellingPricePattern = Pattern.compile("^[1-9]{1}[0-9]*(.00)?$");


        hashMap.put(txtMeatId,idPattern);
        hashMap.put(txtDescription,descriptionPattern);
        hashMap.put(txtQtyOnHand,qtyOnHandPattern);
        hashMap.put(txtPurchasePrice,purchasePricePattern);
        hashMap.put(txtSellingPrice,sellingPricePattern);

        loadAllMeat();

        tblMeatForm.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });

    }

    private void setData(Meat newValue) {
        if(newValue != null) {
            txtUpdateId.setText(newValue.getMeatId());
            txtUpdateDescription.setText(newValue.getDescription());
            txtUpdateQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
            txtUpdatePurchasePrice.setText(String.valueOf(newValue.getPurchasePrice()));
            txtUpdateSellingPrice.setText(String.valueOf(newValue.getSellingPrice()));
        }
    }

    private void loadAllMeat() {
        try {
            String sql="SELECT*FROM Meat";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();

            ObservableList<Meat> obList = FXCollections.observableArrayList();

            while (result.next()){
                obList.add(new Meat(
                        result.getString(1),
                        result.getString(2),
                        result.getInt(3),
                        result.getDouble(4),
                        result.getDouble(5)
                )) ;
            }

            tblMeatForm.setItems(obList);
            tblMeatForm.refresh();


        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtKeyReleased(KeyEvent keyEvent) {
        validation();

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object respone =validation();

            if (respone instanceof JFXTextField){
                JFXTextField t= (JFXTextField) respone;
                t.requestFocus();
            }else  if(respone instanceof Boolean){
                saveMeat();
            }
        }
    }

    public Object validation(){
        for (JFXTextField textField: hashMap.keySet()){
            Pattern pattern = hashMap.get(textField);
            if(pattern.matcher(textField.getText()).matches()){
                accept(textField);
            }else{
                error(textField);
                return textField;
            }
        }
        return true;
    }

    private void error(JFXTextField textField) {
        if(textField.getText().length()>0){
            textField.setFocusColor(Paint.valueOf("red"));
        }
        btnSave.setDisable(true);
    }

    private void accept(JFXTextField textField) {
        textField.setFocusColor(Paint.valueOf("#08b506"));
        btnSave.setDisable(false);
    }

    public  void  saveMeat(){
        Meat m=new Meat(txtMeatId.getText(),txtDescription.getText(),Integer.valueOf(txtQtyOnHand.getText()),Double.valueOf(txtPurchasePrice.getText()),Double.parseDouble(txtSellingPrice.getText()));

        try {
            String sql="INSERT INTO Meat VALUES (?,?,?,?,?)";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,m.getMeatId());
            stm.setObject(2,m.getDescription());
            stm.setObject(3,m.getQtyOnHand());
            stm.setObject(4,m.getSellingPrice());
            stm.setObject(5,m.getSellingPrice());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        btnSave.setDisable(true);
        clearText();
        loadAllMeat();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        saveMeat();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            String sql="UPDATE Meat SET description=?,qtyOnHand=?,purchasePrice=?,sellingPrice=? WHERE mid=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdateDescription.getText());
            stm.setObject(2,txtUpdateQtyOnHand.getText());
            stm.setObject(3,txtUpdatePurchasePrice.getText());
            stm.setObject(4,txtUpdateSellingPrice.getText());
            stm.setObject(5,txtUpdateId.getText());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        loadAllMeat();
        clearText();
    }


    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            String sql = "DELETE FROM Meat WHERE mid=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdateId.getText());
            stm.executeUpdate();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadAllMeat();
        clearText();
    }


    private void clearText() {
        txtMeatId.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtPurchasePrice.clear();
        txtSellingPrice.clear();
        txtUpdateId.clear();
        txtUpdateDescription.clear();
        txtUpdateQtyOnHand.clear();
        txtUpdatePurchasePrice.clear();
        txtUpdateSellingPrice.clear();

        txtMeatId.requestFocus();

        txtMeatId.setFocusColor(Paint.valueOf("#4059a9"));
        txtDescription.setFocusColor(Paint.valueOf("#4059a9"));
        txtQtyOnHand.setFocusColor(Paint.valueOf("#4059a9"));
        txtPurchasePrice.setFocusColor(Paint.valueOf("#4059a9"));
        txtSellingPrice.setFocusColor(Paint.valueOf("#4059a9"));

    }

}
