package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginContext;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;

    public void btnLogInOnAction(ActionEvent actionEvent) throws IOException {
        if (txtUserName.getText().equals("Vihanga") & txtPassword.getText().equals("1234")) {
            setUi("DashBoardForm");
        } else {
            new Alert(Alert.AlertType.ERROR, "Wrong Password!!!").show();
        }
    }

    private void setUi(String location) throws IOException {
        Stage stage=(Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/"+location+".fxml"))));
        stage.centerOnScreen();
    }
}
