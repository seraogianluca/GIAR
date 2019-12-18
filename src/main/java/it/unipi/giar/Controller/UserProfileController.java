package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXButton;

import it.unipi.giar.Data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class UserProfileController {
	User user;
	
    @FXML
    private Text Username;

    @FXML
    private JFXButton deleteProfile;

    @FXML
    void deleteUserProfile(ActionEvent event) {
    	//TO DO 
    }
    
    public void initialize() {
    	this.user = UserMenuController.user;
    	Username.setText(user.getNickname());
    	
    	
    }
    

}
