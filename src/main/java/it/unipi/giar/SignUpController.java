package it.unipi.giar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUpController  {
	
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
    	try {
			List<String> countries1 = Files.readAllLines(new File("src/main/resources/countries.txt").toPath(), Charset.defaultCharset());
			ObservableList<String> countries = FXCollections.observableArrayList(countries1);
			signUpCountry.setItems(countries);
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    }

    @FXML
    void SignUp(ActionEvent event) throws IOException {
    	/*//da controllare
    	String username = signUpNickname.getText();
		String password = signUpPassword.getText();
		String confirmPasswrod = signUpConfirmPassword.getText();
		String email = signUpEmail.getText();
		String country = signUpCountry.getValue();
		
		System.out.println(country);
		//*/
		
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

