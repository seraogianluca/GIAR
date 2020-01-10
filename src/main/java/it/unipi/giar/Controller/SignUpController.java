package it.unipi.giar.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import it.unipi.giar.GiarSession;
import it.unipi.giar.Data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignUpController  {
	
	private boolean errorFlag = false;
	
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
	
	@FXML
	private JFXButton signInNowButton;
	
    @FXML
    private Text errorMessage;
    
    public void initialize() {
    	try {
			List<String> countries1 = Files.readAllLines(
					new File("src/main/resources/countries.txt").toPath(), 
					Charset.defaultCharset());
			ObservableList<String> countries = FXCollections.observableArrayList(countries1);
			signUpCountry.setItems(countries);
    	} catch (Exception e) {
			e.printStackTrace();
    	}	
    }
    
    private void setError(boolean flag, String msg) {
    	errorFlag = flag;
    	
    	if(flag) {
    		errorMessage.setText(msg);
    		errorMessage.setVisible(true);
    	} else {
    		errorMessage.setVisible(false);
    	}
    }
    
    @FXML
    private void checkNickname(KeyEvent event) {
    	if(!User.checkNickname(signUpNickname.getText())) {
    		setError(true, "Nickname already exists.");
    	} else {
    		setError(false, null);
    	} 
    }

    @FXML
    private void checkEmail(KeyEvent event) {
    	if(!User.checkEmail(signUpEmail.getText())) {
    		setError(true, "Email already exists.");
    	} else {
    		setError(false, null);
    	}
    }

    @FXML
    private void checkPassword(KeyEvent event) {
		String password = signUpPassword.getText();
		String confirmPassword = signUpConfirmPassword.getText();

		if(!password.equals(confirmPassword)) {
			setError(true, "Password doesn't match.");
		} else {
			setError(false, null);
		}
    }

    @FXML
    private void SignUp(ActionEvent event) {
    	GiarSession session;
    	
    	MessageDigest md;
    	byte[] digest;
    	
    	String password;
    	String nickname;
    	String email;
    	String country;
    	
		Parent root;
		Stage stage;
		
	    if (!errorFlag) {
	    	errorMessage.setVisible(false);
			
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(signUpPassword.getText().getBytes());
				digest = md.digest();
				password = DatatypeConverter.printHexBinary(digest).toUpperCase();
				
				nickname = signUpNickname.getText();
				email = signUpEmail.getText();
				country = signUpCountry.getValue();
				
				if(password == null || nickname == null || email == null || country == null) {
					setError(true, "Please fill all text field.");
					return;
				}
				
				User.register(nickname, email, password, country);
				session = GiarSession.getInstance();
				session.setRegistered(nickname);

	            root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
				
		        stage = (Stage)signUpButton.getScene().getWindow();
		        stage.setScene(new Scene(root));
		        stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}		
		} else {
			setError(true, "Unable to register.");
		}
	}
	
	@FXML
	void showSignInPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
		Stage stage = (Stage) signInNowButton.getScene().getWindow();

		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}

}

