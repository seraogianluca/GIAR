package it.unipi.giar.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import it.unipi.giar.GiarSession;
import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class InfoGameController {

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
    
    public void initialize(String gameName) {
    	GiarSession session;
    	User user;
    	Game game;
    	Date date;
    	DateFormat dateFormat;
    	
    	game = Game.findGame(gameName);
    	
    	name.setText(game.getName());
    	description.setText(game.getDescription()); 
    	rating.setText(Double.toString(game.getRating())); 
    	
    	date = game.getReleased();
    	dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 	
    	
    	//released.setText(dateFormat.format(date)); 
    	metacritic.setText(Integer.toString(game.getMetacritic())); 
    	 
    	ObservableList<String> ratingValues = FXCollections.observableArrayList();
    	ratingValues.addAll("1", "2", "3", "4", "5");
		yourRating.setItems(ratingValues);
		
		//TODO: 
		//IF THE USER ALREADY VOTED THE GAME, IN THE COMBOBOX APPEARS THE RATE OF THAT USER BY DEFAULT
		//String userRate = String.valueOf(user.getGameRate(game.getId()));
		//yourRating.setValue(userRate);
		
		session = GiarSession.getInstance();
		user = session.getLoggedUser();
		
		if(user.isInMyGames(game.getName())) {
			addToWishlistButton.setDisable(false);
			addToMyGamesButton.setDisable(true);
		} else if(user.isInWishlist(game.getName())) {
			addToWishlistButton.setDisable(true);
			addToMyGamesButton.setDisable(false);
		} else {
			addToWishlistButton.setDisable(false);
    		addToMyGamesButton.setDisable(false);
		}
		
    }

    @FXML
    void addToMyGames(ActionEvent event) {
    	GiarSession session;
    	User user;
    	
    	session = GiarSession.getInstance();
    	user = session.getLoggedUser();

    	user.addGameToList(name.getText(), "myGames");  
    	
    	addToMyGamesButton.setDisable(true);
    	addToWishlistButton.setDisable(false);
    }

    @FXML
    void addToWishlist(ActionEvent event) {
      	GiarSession session;
    	User user;
    	
    	session = GiarSession.getInstance();
    	user = session.getLoggedUser();
    	
    	user.addGameToList(name.getText(), "wishlist");
    		
    	addToWishlistButton.setDisable(true);
    	addToMyGamesButton.setDisable(false);
    } 
    
}