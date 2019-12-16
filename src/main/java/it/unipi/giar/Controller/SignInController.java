package it.unipi.giar.Controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import it.unipi.giar.Data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignInController {

    @FXML
    private AnchorPane anchorRoot;
    
    @FXML
    private JFXButton signUpNowButton;
    
    @FXML
    private JFXButton signInButton;

    @FXML
    private JFXTextField signInNickname;

    @FXML
    private JFXPasswordField signInPassword;
    
    @FXML
    private Text errorMessage;

    @FXML
    void SignIn(ActionEvent event) throws IOException {
    	
    	String username = null;
    	String password = null;
    	try {
    		
    		username = signInNickname.getText();
    		password = signInPassword.getText();
    
    	   }
    	    
    	catch( Exception e){
    		e.printStackTrace(); 		
    	}
    	
    	
    	   	
    	if((!User.checkNickname(username)) || (!User.checkPassword(password))) {
    		errorMessage.setText("Wrong username or password.");
    		errorMessage.setVisible(true);
    		return;
    	}
    	
    	Parent root;
    	
    	boolean isAdmin= false;
    	isAdmin = User.isAdmin(username);
    	if (isAdmin) {

    		root = FXMLLoader.load(getClass().getResource("/fxml/AdminHomepage.fxml"));
    	}
    	else { 	
    		
	    	root = FXMLLoader.load(getClass().getResource("/fxml/UserHomepage.fxml"));
	    	
    	}
	        Stage stage = new Stage();
	        stage.setTitle("GIAR");
	        stage.setScene(new Scene(root));  
	        stage.show();
	        stage.setResizable(false);
	        Stage stage1 = (Stage) signInButton.getScene().getWindow();
	        stage1.close();
    	
    }

    @FXML
    void showSignUpPage(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
        Stage stage = new Stage();
        stage.setTitle("GIAR");
        stage.setScene(new Scene(root));  
        stage.show();
        stage.setResizable(false);
        String css = this.getClass().getResource("/css/signUp.css").toExternalForm(); 
        root.getStylesheets().add(css);
        Stage stage1 = (Stage) signUpNowButton.getScene().getWindow();
        stage1.close();
    }
    
}
