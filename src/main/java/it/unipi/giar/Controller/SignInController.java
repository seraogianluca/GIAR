package it.unipi.giar.Controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
    void SignIn(ActionEvent event) throws IOException {
    	/*//da controllare
    	//Connection connection = //connectionstring
    	//try {
    		String username = signInNickname.getText();
    		String password = signInPassword.getText();
    		
    		
    		//Statement statement = connection.create statements..event
    	    //int status = ritorno di mongo
    		//isadmin? ritorna il tipo di utente
    	    //if (status > 0) {
    	    	//loggato
    	//    }
    	    
    	//}catch( MongoDBException e){
    	//	e.printStacktrace();    		
    	//}
    	*/
    	Parent root;
    	boolean isAdmin= false;
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
