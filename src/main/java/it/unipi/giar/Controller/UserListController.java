package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXTreeTableView;

import it.unipi.giar.Controller.UserHomepageController.GameTable;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class UserListController {

    @FXML
    private Text listType;
    
    public void initialize(String type) {
    	listType.setText(type);
    	
//    	if type=="Wishlist"
//    			//LOAD IN THE TABLE THE WISHLIST GAMES 
//    	else if type=="MyGames"
//    			//LOAD IN THE TABLE THE MYGAMES GAMES 
//    	
    
    }
}

