package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import views.TM.DetailCartTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailFormController {
    public TableView tblOrderDetail;
    public TableColumn colOrderId;
    public TableColumn colClientId;
    public TableColumn colMeatId;
    public TableColumn colQty;
    public TableColumn colPrice;
    public TableColumn colDate;
    public TableColumn colTime;

    public  void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colClientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        colMeatId.setCellValueFactory(new PropertyValueFactory<>("meatId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        loadAllOrderDetail();

    }

    private void loadAllOrderDetail() {
        try {
            String sql="SELECT o.oId,o.clientId,od.meatId,od.qty,od.price,o.date,o.time  FROM Orders o INNER JOIN OrderDetail od ON o.oId = od.orderId";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();

            ObservableList<DetailCartTM> detailList= FXCollections.observableArrayList();

            while (resultSet.next()){
                detailList.add(new DetailCartTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getDouble(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                ));
            }
            tblOrderDetail.setItems(detailList);


        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
