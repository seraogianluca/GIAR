package it.unipi.giar.Controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import it.unipi.giar.GiarSession;
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
    
    public void initialize() {
    	GiarSession session = GiarSession.getInstance();
    	
    	if (session.getDeleted()) {
    		errorMessage.setText("Account successfully deleted.");
        	errorMessage.setVisible(true);
    	} else if (session.getRegistered() != null) {
    		signInNickname.setText(session.getRegistered());
    	}
	}

    @FXML
    void SignIn(ActionEvent event) throws IOException {
    	GiarSession session;
    	String username = null;
    	String password = null;
    	Parent root;
    	Stage stage;
    	
    	username = signInNickname.getText();
    	password = signInPassword.getText();
    	
    	if(username == null || password == null) {
			errorMessage.setText("Please insert username and password.");
			errorMessage.setVisible(true);
			return;
		} else if ((!User.checkNickname(username)) || (!User.checkPassword(password))) {
    		errorMessage.setText("Wrong username or password.");
    		errorMessage.setVisible(true);
    		return;
    	}
    	
    	session = GiarSession.getInstance();
    	session.setLoggedUser(username);
    	
    	if (User.isAdmin(username)) {
    		root = FXMLLoader.load(getClass().getResource("/fxml/AdminHomepage.fxml"));
    	} else { 	
            root = FXMLLoader.load(getClass().getResource("/fxml/UserMenu.fxml"));	
    	}
	        
    	stage = (Stage)signInButton.getScene().getWindow();
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
	
	@FXML
	void signInPassword(ActionEvent event) throws IOException {
		SignIn(event);
	}
    
}
