package it.unipi.giar;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignInController {

    @FXML
    private JFXButton signUpNowButton;

    @FXML
    private JFXButton signInButton;

    @FXML
    private JFXTextField signInNickname;

    @FXML
    private JFXPasswordField signInPassword;

    @FXML
    void SignIn(ActionEvent event) {

    }

    @FXML
    void showSignUpPage(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
   
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Sign Up");
        stage.setScene(new Scene(root));  
        stage.show();
    }

}
