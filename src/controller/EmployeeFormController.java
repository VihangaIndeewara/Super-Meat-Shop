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
import model.Client;
import model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class EmployeeFormController {
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colJoinDate;
    public TableColumn colResigningDate;
    public TableColumn colSalary;
    public TableColumn colContactNo;
    public JFXTextField txtEmployeeId;
    public JFXTextField txtEmployeeName;
    public JFXTextField txtEmployeeAddress;
    public JFXTextField txtEmployeeContactNo;
    public JFXTextField txtEmployeeSalary;
    public JFXTextField txtUpdateId;
    public JFXTextField txtUpdateName;
    public JFXTextField txtUpdateAddress;

    public JFXTextField txtUpdateJoinDate;
    public JFXTextField txtUpdateSalary;
    public JFXTextField txtUpdateContactNo;
    public JFXTextField txtEmployeeResigningDate;
    public JFXTextField txtEmployeeJoinDate;
    public TableView<Employee> tblEmployeeForm;
    public JFXTextField txtUpdateResigningDate;
    public Button btnSave;

    LinkedHashMap<JFXTextField, Pattern> hashMap=new LinkedHashMap<>();

    public  void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colJoinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        colResigningDate.setCellValueFactory(new PropertyValueFactory<>("resigningDate"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        btnSave.setDisable(true);

        Pattern idPattern = Pattern.compile("^E[0-9]{3}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 / , ]{4,50}$");
        Pattern joinDatePattern = Pattern.compile("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$");
        Pattern salaryPattern = Pattern.compile("^[1-9]{1}[0-9]*(.00)?$");
        Pattern contactNoPattern = Pattern.compile("^[+940-9]{12}$");

        hashMap.put(txtEmployeeId,idPattern);
        hashMap.put(txtEmployeeName,namePattern);
        hashMap.put(txtEmployeeAddress,addressPattern);
        hashMap.put(txtEmployeeJoinDate,joinDatePattern);
        hashMap.put(txtEmployeeSalary,salaryPattern);
        hashMap.put(txtEmployeeContactNo,contactNoPattern);

        btnSave.setDisable(true);

        loadAllEmployee();

        tblEmployeeForm.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });

    }

    private void setData(Employee newValue) {
        if(newValue != null) {
            txtUpdateId.setText(newValue.getEmpId());
            txtUpdateName.setText(newValue.getName());
            txtUpdateAddress.setText(newValue.getAddress());
            txtUpdateJoinDate.setText(newValue.getJoinDate());
            txtUpdateResigningDate.setText(newValue.getResigningDate());
            txtUpdateSalary.setText(String.valueOf(newValue.getSalary()));
            txtUpdateContactNo.setText(newValue.getContactNo());
        }
    }

    private void loadAllEmployee() {
        try {
            String sql="SELECT*FROM Employee";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();

            ObservableList<Employee> obList = FXCollections.observableArrayList();

            while (result.next()){
                obList.add(new Employee(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getDouble(6),
                        result.getString(7)
                )) ;
            }

            tblEmployeeForm.setItems(obList);
            tblEmployeeForm.refresh();


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
                saveEmployee();
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


    private void saveEmployee() {
        Employee e=new Employee(txtEmployeeId.getText(),txtEmployeeName.getText(),txtEmployeeAddress.getText(),txtEmployeeJoinDate.getText(),txtEmployeeResigningDate.getText(),Double.parseDouble(txtEmployeeSalary.getText()),txtEmployeeContactNo.getText());

        try {
            String sql="INSERT INTO Employee VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,e.getEmpId());
            stm.setObject(2,e.getName());
            stm.setObject(3,e.getAddress());
            stm.setObject(4,e.getJoinDate());
            stm.setObject(5,e.getResigningDate());
            stm.setObject(6,e.getSalary());
            stm.setObject(7,e.getContactNo());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        btnSave.setDisable(true);
        clearText();
        loadAllEmployee();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        saveEmployee();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            String sql="UPDATE Employee SET name=?,address=?,joinDate=?,resigningDate=?,salary=?,contactNo=? WHERE eid=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdateName.getText());
            stm.setObject(2,txtUpdateAddress.getText());
            stm.setObject(3,txtUpdateJoinDate.getText());
            stm.setObject(4,txtUpdateResigningDate.getText());
            stm.setObject(5,txtUpdateSalary.getText());
            stm.setObject(6,txtUpdateContactNo.getText());
            stm.setObject(7,txtUpdateId.getText());
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        loadAllEmployee();
        clearText();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            String sql = "DELETE FROM Employee WHERE eid=?";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setObject(1,txtUpdateId.getText());
            stm.executeUpdate();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadAllEmployee();
        clearText();
    }

    private void clearText() {
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtEmployeeAddress.clear();
        txtEmployeeJoinDate.clear();
        txtEmployeeResigningDate.clear();
        txtEmployeeSalary.clear();
        txtEmployeeContactNo.clear();
        txtUpdateId.clear();
        txtUpdateName.clear();
        txtUpdateAddress.clear();
        txtUpdateJoinDate.clear();
        txtUpdateResigningDate.clear();
        txtUpdateSalary.clear();
        txtUpdateContactNo.clear();

        txtEmployeeId.requestFocus();

        txtEmployeeId.setFocusColor(Paint.valueOf("#4059a9"));
        txtEmployeeName.setFocusColor(Paint.valueOf("#4059a9"));
        txtEmployeeAddress.setFocusColor(Paint.valueOf("#4059a9"));
        txtEmployeeJoinDate.setFocusColor(Paint.valueOf("#4059a9"));
        txtEmployeeResigningDate.setFocusColor(Paint.valueOf("#4059a9"));
        txtEmployeeSalary.setFocusColor(Paint.valueOf("#4059a9"));
        txtEmployeeContactNo.setFocusColor(Paint.valueOf("#4059a9"));


    }


}
