package it.unipi.giar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.java_cup.internal.runtime.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUpController  {
	ObservableList<String> countries = FXCollections.observableArrayList("Puppa");
	
    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXTextField signUpNickname;

    @FXML
    private JFXPasswordField signUpPassword;

    @FXML
    private JFXTextField signUpEmail;

    @FXML
    private JFXPasswordField signUpConfirmPassword;

    @FXML
    private JFXComboBox<String> signUpCountry;
    
    public void initialize() {
        ObservableList<String> elements = FXCollections.observableArrayList(
            new String("Element 1"),
            new String( "Element 2")
        );

        signUpCountry.setItems(elements);
    }

    @FXML
    void SignUp(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
    
        Stage stage = new Stage();
        stage.setTitle("GIAR");
        stage.setScene(new Scene(root));  
        stage.show();
        stage.setResizable(false);
        Stage stage1 = (Stage) signUpButton.getScene().getWindow();
        stage1.close();
    }
    

	


}

