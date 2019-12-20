package it.unipi.giar.Controller;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class InfoGameController {
	
	private Game game;
	private User user;
	private Document gameDoc;

    @FXML
    private Text name;

    @FXML
    private Text description;

    @FXML
    private JFXComboBox<String> yourRating;

    @FXML
    private Text rating;

    @FXML
    private JFXButton addToWishlistButton;

    @FXML
    private JFXButton addToMyGamesButton;

    @FXML
    private Text released;

    @FXML
    private Text metacritic;

    @FXML
    private ListView<?> platforms;

    @FXML
    private ListView<?> genres;

    @FXML
    private ListView<?> developers;

    @FXML
    void addToMyGames(ActionEvent event) {
    	addToMyGamesButton.setDisable(true);
    	addToWishlistButton.setDisable(false);
    	
    	//TO DO
    	
    	
    	user.addGameToList(gameDoc, "myGames");
    	user.addToMongoList(gameDoc, "myGames");
    	if(!user.checkListNull("myGames"))
    		if(user.checkDuplicate(gameDoc, "wishlist")) {
    		user.removeGameFromList(gameDoc, "wishlist");	
    		user.removeFromMongoList(gameDoc, "wishlist");
    	}
    	
    	
    	
    	
    }

    @FXML
    void addToWishlist(ActionEvent event) {
    	addToWishlistButton.setDisable(true);
    	addToMyGamesButton.setDisable(false);
    	
    	//TO DO
    	
    	user.addGameToList(gameDoc, "wishlist");
    	user.addToMongoList(gameDoc, "wishlist");
    			
    	if(!user.checkListNull("myGames")) {
    		
    		if(user.checkDuplicate(gameDoc, "myGames")==true) {
    			
    		user.removeGameFromList(gameDoc, "myGames");	
    		user.removeFromMongoList(gameDoc, "myGames");
    		}
    	}
    		
    	user.createGame(game.getName());
    	
    	
    	
    }
    
    public void initialize(User user, Game game) {
    	this.user=user;
    	this.game = game;
    	
    	gameDoc = new Document();
    	gameDoc.append("name", game.getName());
    	gameDoc.append("rating", game.getRating());
    	
    	name.setText(game.getName()); 
    	description.setText(game.getDescription()); 
    	rating.setText(String.valueOf(game.getRating())); 
    	
    	Date date = game.getReleased();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
    	
    	
    	//released.setText(dateFormat.format(date)); 
    	metacritic.setText(String.valueOf(game.getMetacritic())); 
    	 
		List<String> list = new ArrayList<String>();
    	ObservableList<String> rats = FXCollections.observableArrayList(list);
    	rats.add("1");
    	rats.add("2");
    	rats.add("3");
    	rats.add("4");
    	rats.add("5");
		yourRating.setItems((ObservableList<String>) rats);
		
		//TO DO 
		//IF THE USER ALREADY VOTED THE GAME, IN THE COMBOBOX APPEARS THE RATE OF THAT USER BY DEFAULT
		//String userRate = String.valueOf(user.getGameRate(game.getId()));
		//yourRating.setValue(userRate);
		
		if(user.isInMyGames(game)) {
			addToWishlistButton.setDisable(false);
			addToMyGamesButton.setDisable(true);
		}
		
		else if(user.isInWishlist(game)) {
			addToWishlistButton.setDisable(true);
			addToMyGamesButton.setDisable(false);
		}
		
		else {
			addToWishlistButton.setDisable(false);
    		addToMyGamesButton.setDisable(false);
		}
		
    }
    
}