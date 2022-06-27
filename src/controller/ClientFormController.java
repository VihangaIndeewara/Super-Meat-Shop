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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ClientFormController {
    public AnchorPane clientContext;
    public TableView<Client> tblClientForm;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colType;
    public TableColumn colMobileNo;
    public TableColumn colLandNo;
    public JFXTextField txtClientId;
    public JFXTextField txtClientName;
    public JFXTextField txtClientAddress;
    public JFXTextField txtMobileNo;
    public JFXTextField txtLandNo;
    public JFXTextField txtUpdateId;
    public JFXTextField txtUpdateName;
    public JFXTextField txtUpdateAddress;
    public JFXTextField txtUpdateType;
    public JFXTextField txtUpdateMobileNo;
    public JFXTextField txtUpdateLandNo;
    public JFXTextField txtClientType;
    public Button btnSave;

    LinkedHashMap<JFXTextField, Pattern> hashMap=new LinkedHashMap<>();

    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMobileNo.setCellValueFactory(new PropertyValueFactory<>("mobileNo"));
        colLandNo.setCellValueFactory(new PropertyValueFactory<>("landNo"));


        Pattern idPattern = Pattern.compile("^C[0-9]{3}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 / , ]{4,50}$");
        Pattern typePattern = Pattern.compile("^[A-z ]{3,10}$");
        Pattern mobileNoPattern = Pattern.compile("^[+940-9]{12}$");
        Pattern landNoPattern = Pattern.compile("^[0-9]{3}[-][0-9]{7}$");

        hashMap.put(txtClientId,idPattern);
        hashMap.put(txtClientName,namePattern);
        hashMap.put(txtClientAddress,addressPattern);
        hashMap.put(txtClientType,typePattern);
        hashMap.put(txtMobileNo,mobileNoPattern);
        hashMap.put(txtLandNo,landNoPattern);

        btnSave.setDisable(true);

        loadAllClients();

        tblClientForm.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });
    }


    private void setData(Client newValue) {
        if(newValue != null) {
            txtUpdateId.setText(newValue.getCusId());
            txtUpdateName.setText(newValue.getName());
            txtUpdateAddress.setText(newValue.getAddress());
            txtUpdateType.setText(newValue.getType());
            txtUpdateMobileNo.setText(newValue.getMobileNo());
            txtUpdateLandNo.setText(newValue.getLandNo());
        }

    }


    private void loadAllClients() {
        try {
            String sql="SELECT*FROM Client";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();

            ObservableList<Client> obList = FXCollections.observableArrayList();

            while (result.next()){
                  obList.add(new Client(
                          result.getString(1),
                          result.getString(2),
                          result.getString(3),
                          result.getString(4),
                          result.getString(5),
                          result.getString(6)

                  )) ;
            }

            tblClientForm.setItems(obList);

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
                saveClient();
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

    private void saveClient() {
        Client c=new Client(txtClientId.getText(),txtClientName.getText(),txtClientAddress.getText(),txtClientType.getText(),txtMobileNo.getText(),txtLandNo.getText());

        try {
            String sql="INSERT INTO Client VALUES (?,?,?,?,?,?)";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,c.getCusId());
            stm.setObject(2,c.getName());
            stm.setObject(3,c.getAddress());
            stm.setObject(4,c.getType());
            stm.setObject(5,c.getMobileNo());
            stm.setObject(6,c.getLandNo());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnSave.setDisable(true);
        clearText();
        loadAllClients();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        saveClient();
    }

    private void clearText() {
        txtClientId.clear();
        txtClientName.clear();
        txtClientAddress.clear();
        txtClientType.clear();
        txtMobileNo.clear();
        txtLandNo.clear();
        txtUpdateId.clear();
        txtUpdateName.clear();
        txtUpdateAddress.clear();
        txtUpdateType.clear();
        txtUpdateMobileNo.clear();
        txtUpdateLandNo.clear();

        txtClientId.requestFocus();

        txtClientId.setFocusColor(Paint.valueOf("#4059a9"));
        txtClientId.setFocusColor(Paint.valueOf("#4059a9"));
        txtClientName.setFocusColor(Paint.valueOf("#4059a9"));
        txtClientAddress.setFocusColor(Paint.valueOf("#4059a9"));
        txtClientType.setFocusColor(Paint.valueOf("#4059a9"));
        txtMobileNo.setFocusColor(Paint.valueOf("#4059a9"));
        txtLandNo.setFocusColor(Paint.valueOf("#4059a9"));
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

            try {
                String sql = "UPDATE Client SET name=?,address=?,type=?,mobileNo=?,landNo=? WHERE cid=?";
                PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(sql);
                stm.setObject(1, txtUpdateName.getText());
                stm.setObject(2, txtUpdateAddress.getText());
                stm.setObject(3, txtUpdateType.getText());
                stm.setObject(4, txtUpdateMobileNo.getText());
                stm.setObject(5, txtUpdateLandNo.getText());
                stm.setObject(6, txtUpdateId.getText());
                if (stm.executeUpdate() > 0) {
                    loadAllClients();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            clearText();
        }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            String sql = "DELETE FROM Client WHERE cid=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdateId.getText());
            if(stm.executeUpdate()>0){
                loadAllClients();
            }
            clearText();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }


    }



}
