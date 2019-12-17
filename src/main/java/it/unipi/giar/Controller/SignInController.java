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
    		
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/UserMenu.fxml"));
            root = loader.load();
           
            //access the controller and call a method
            UserMenuController controller = loader.getController();
            controller.initData(new User(username));
    		
    	}
	        
    	Stage stage = (Stage)signInButton.getScene().getWindow();
	    stage.setScene(new Scene(root));  
	    stage.show();
    }

    @FXML
    void showSignUpPage(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
    	String css = this.getClass().getResource("/css/signUp.css").toExternalForm(); 
        Stage stage = (Stage) signUpNowButton.getScene().getWindow();
        
        root.getStylesheets().add(css);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
    
}
