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
import model.PurchaseShop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class PurchaseShopFormController {
    public TableView<PurchaseShop> tblPurchaseShopFrom;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colFarmName;
    public TableColumn colAddress;
    public TableColumn colMeatType;
    public TableColumn colContactNo;
    public JFXTextField txtPurchaseId;
    public JFXTextField txtPurchaseName;
    public JFXTextField txtPurchaseFarmName;
    public JFXTextField txtPurchaseAddress;
    public JFXTextField txtPurchaseMeatType;
    public JFXTextField txtPurchaseContactNo;
    public JFXTextField txtUpdatePurchaseId;
    public JFXTextField txtUpdatePurchaseName;
    public JFXTextField txtUpdatePurchaseFarmName;
    public JFXTextField txtUpdatePurchaseAddress;
    public JFXTextField txtUpdatePurchaseMeatType;
    public JFXTextField txtUpdatePurchaseContactNo;
    public Button btnSave;

    LinkedHashMap<JFXTextField, Pattern> hashMap=new LinkedHashMap<>();

    public  void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("phsId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFarmName.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMeatType.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));


        Pattern idPattern = Pattern.compile("^PHS[0-9]{3}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
        Pattern farmNamePattern = Pattern.compile("^[A-z ]{3,20}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 / , ]{4,50}$");
        Pattern typePattern = Pattern.compile("^[A-z ]{3,10}$");
        Pattern mobileNoPattern = Pattern.compile("^[+940-9]{12}$");


        hashMap.put(txtPurchaseId,idPattern);
        hashMap.put(txtPurchaseName,namePattern);
        hashMap.put(txtPurchaseFarmName,farmNamePattern);
        hashMap.put(txtPurchaseAddress,addressPattern);
        hashMap.put(txtPurchaseMeatType,typePattern);
        hashMap.put(txtPurchaseContactNo,mobileNoPattern);

        btnSave.setDisable(true);

        loadAllPurchaseShop();

        tblPurchaseShopFrom.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });

    }

    private void setData(PurchaseShop newValue) {
        if(newValue != null) {
            txtUpdatePurchaseId.setText(newValue.getPhsId());
            txtUpdatePurchaseName.setText(newValue.getName());
            txtUpdatePurchaseFarmName.setText(newValue.getFarmName());
            txtUpdatePurchaseAddress.setText(newValue.getAddress());
            txtUpdatePurchaseMeatType.setText(newValue.getMeatType());
            txtUpdatePurchaseContactNo.setText(String.valueOf(newValue.getContactNo()));
        }
    }

    private void loadAllPurchaseShop() {
        try {
            String sql="SELECT*FROM PurchaseShop";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();

            ObservableList<PurchaseShop> obList = FXCollections.observableArrayList();

            while (result.next()){
                obList.add(new PurchaseShop(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6)

                )) ;
            }

            tblPurchaseShopFrom.setItems(obList);
            tblPurchaseShopFrom.refresh();


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
                savePurchaseShop();
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

    private void savePurchaseShop() {
        PurchaseShop s=new PurchaseShop(txtPurchaseId.getText(),txtPurchaseName.getText(),txtPurchaseFarmName.getText(),txtPurchaseAddress.getText(),txtPurchaseMeatType.getText(),txtPurchaseContactNo.getText());


        try {
            String sql="INSERT INTO PurchaseShop VALUES (?,?,?,?,?,?)";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,s.getPhsId());
            stm.setObject(2,s.getName());
            stm.setObject(3,s.getFarmName());
            stm.setObject(4,s.getAddress());
            stm.setObject(5,s.getMeatType());
            stm.setObject(6,s.getContactNo());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        btnSave.setDisable(true);
        clearText();
        loadAllPurchaseShop();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        savePurchaseShop();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            String sql = "DELETE FROM PurchaseShop WHERE phsId=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdatePurchaseId.getText());
            stm.executeUpdate();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadAllPurchaseShop();
        clearText();
    }


    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            String sql="UPDATE PurchaseShop SET name=?,farmName=?,address=?,meatType=?,contactNo=? WHERE phsId=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdatePurchaseName.getText());
            stm.setObject(2,txtUpdatePurchaseFarmName.getText());
            stm.setObject(3,txtUpdatePurchaseAddress.getText());
            stm.setObject(4,txtUpdatePurchaseMeatType.getText());
            stm.setObject(5,txtUpdatePurchaseContactNo.getText());
            stm.setObject(6,txtUpdatePurchaseId.getText());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        loadAllPurchaseShop();
        clearText();
    }

    public  void  clearText(){
        txtPurchaseId.clear();
        txtPurchaseName.clear();
        txtPurchaseFarmName.clear();
        txtPurchaseAddress.clear();
        txtPurchaseMeatType.clear();
        txtPurchaseContactNo.clear();
        txtUpdatePurchaseId.clear();
        txtUpdatePurchaseName.clear();
        txtUpdatePurchaseFarmName.clear();
        txtUpdatePurchaseAddress.clear();
        txtUpdatePurchaseMeatType.clear();
        txtUpdatePurchaseContactNo.clear();

        txtPurchaseId.requestFocus();

        txtPurchaseId.setFocusColor(Paint.valueOf("#4059a9"));
        txtPurchaseName.setFocusColor(Paint.valueOf("#4059a9"));
        txtPurchaseFarmName.setFocusColor(Paint.valueOf("#4059a9"));
        txtPurchaseAddress.setFocusColor(Paint.valueOf("#4059a9"));
        txtPurchaseMeatType.setFocusColor(Paint.valueOf("#4059a9"));
        txtPurchaseContactNo.setFocusColor(Paint.valueOf("#4059a9"));
    }


}
