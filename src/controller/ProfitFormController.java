package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import model.Profit;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ProfitFormController {
    public TableView<Profit> tblProfitForm;
    public TableColumn colProfitId;
    public TableColumn colOrderId;
    public TableColumn colPurchasePrice;
    public TableColumn colSellingPrice;
    public TableColumn colAmount;
    public Label lblTotalProfit;
    public TextField txtStartDate;
    public TextField txtEndDate;
    public JFXButton btnOk;

    LinkedHashMap<TextField, Pattern> map=new LinkedHashMap<>();


    public  void  initialize(){
        colProfitId.setCellValueFactory(new PropertyValueFactory<>("profitId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        Pattern startDatePattern =Pattern.compile("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$");
        Pattern endDatePattern =Pattern.compile("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$");
        map.put(txtStartDate,startDatePattern);
        map.put(txtEndDate,endDatePattern);

        btnOk.setDisable(true);

        loadAllProfit();
    }


    private void loadAllProfit() {
        try {
            String sql="SELECT * FROM Profit";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();

            ObservableList<Profit> obList= FXCollections.observableArrayList();

            while (resultSet.next()){
                obList.add(new Profit(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5)
                ));
            }
            tblProfitForm.setItems(obList);


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void btnBarChatOnAction(ActionEvent actionEvent) {
        try {
            JasperReport compiledReport= (JasperReport) JRLoader.loadObject(getClass().getResource("/reports/ProfitBarChart.jasper"));
            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException |SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void btnPieChartOnAction(ActionEvent actionEvent) {
        try {
            JasperReport compiledReport= (JasperReport) JRLoader.loadObject(getClass().getResource("/reports/ProfitPieChart.jasper"));
            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException |SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void txtKeyReleased(KeyEvent keyEvent) throws Exception {
        validation();

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object responce = validation();

            if (responce instanceof TextField){
                TextField t= (TextField) responce;
                t.requestFocus();
            }
        }
    }

    public Object validation(){
        for (TextField t: map.keySet()){
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

    private void error(TextField textField) {
        if(textField.getText().length()>0){
            textField.setStyle("-fx-border-color: red");
        }
        btnOk.setDisable(true);
    }

    private void accept(TextField textField) {
        textField.setStyle("-fx-border-color: #08b506");
        btnOk.setDisable(false);

    }

    double totProfit;

    public void btnOkOnAction(ActionEvent actionEvent) {
        String start=txtStartDate.getText();
        String end=txtEndDate.getText();

        try {
            String sql="SELECT oId FROM orders WHERE date BETWEEN '"+start+"' and '"+end+"'";
            PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement(sql);

            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                try {
                    String sql2 = "SELECT amount FROM Profit WHERE orderId=?";
                    PreparedStatement stm2 = DBConnection.getInstance().getConnection().prepareStatement(sql2);
                    stm2.setObject(1, resultSet.getString(1));
                    ResultSet set = stm2.executeQuery();

                    while (set.next()) {
                        totProfit += Double.parseDouble(set.getString(1));
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        lblTotalProfit.setText(String.valueOf(totProfit));
    }



    public void btnClearOnAction(ActionEvent actionEvent) {
        txtStartDate.clear();
        txtEndDate.clear();
        lblTotalProfit.setText("0.00");
        totProfit=0.00;

        txtStartDate.requestFocus();

        txtStartDate.setStyle("-fx-border-color:  #9a9494");
        txtEndDate.setStyle("-fx-border-color:  #9a9494");
    }
}
