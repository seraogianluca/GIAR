package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXButton;

import it.unipi.giar.GiarSession;
import it.unipi.giar.Data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserProfileController {
	
	@FXML
	private Text profileTitle;

	@FXML
	private Text nicknameLabel;

	@FXML
	private Text userNickname;

	@FXML
	private Text emailLabel;
	
	@FXML
	private Text userEmail;

	@FXML
	private Text countryLabel;
	
	@FXML
	private Text userCountry;

	@FXML
	private JFXButton deleteProfile;
	
    public void initialize() {
    	User user;
    	
    	GiarSession session = GiarSession.getInstance();
    	user = session.getLoggedUser();
    	
    	userNickname.setText(user.getNickname());
    	userEmail.setText(user.getEmail());
    	userCountry.setText(user.getCountry());
    }

    @FXML
    void deleteUserProfile(ActionEvent event) {
    	try {
    		Parent root;
    		Stage stage;
    		
			User.delete(userNickname.getText());
			
			root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
			
			stage = (Stage)profileTitle.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
