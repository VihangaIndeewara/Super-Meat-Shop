package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class DashBoardFormController {
    public AnchorPane AllDashBoard;
    public Label lblTime;
    public AnchorPane DashBoardContext;
    public Label lblDate;

    public  void initialize(){
        loadDateAndTime();
    }

    private void loadDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Timeline clock=new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime=LocalTime.now();
            lblTime.setText(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void btnDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardContextForm");
    }

    public void btnClientOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ClientForm");
    }

    public void btnMeatOnAction(ActionEvent actionEvent) throws IOException {
        setUi("MeatForm");
    }

    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("OrderForm");
    }

    public void btnOrderDetailsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("OrderDetailForm");
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("EmployeeForm");
    }

    public void btnPurchaseShopOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PurchaseShopForm");
    }

    public void btnPurchaseOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PurchaseOrderForm");
    }

    public void btnPurchaseOrderDetailsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PurchaseOrderDetailForm");
    }

    public void btnProfitOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProfitForm");
    }

    private void setUi(String location) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../views/"+location+".fxml"));
        DashBoardContext.getChildren().add(parent);

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../views/LoginForm.fxml"));
        AllDashBoard.getChildren().add(parent);
    }
}
