package it.unipi.giar.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;


import it.unipi.giar.GiarSession;
import it.unipi.giar.Data.Developer;
import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.Genre;
import it.unipi.giar.Data.Platform;
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
    private ListView<String> platforms;

    @FXML
    private ListView<String> genres;

    @FXML

    private ListView<String> developers;
    
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
      
    	metacritic.setText(Integer.toString(game.getMetacritic())); 
    	 
    	ObservableList<String> ratingValues = FXCollections.observableArrayList();
    	ratingValues.addAll("1", "2", "3", "4", "5");
		  yourRating.setItems(ratingValues);
		  
      released.setText(dateFormat.format(date));
		
    	metacritic.setText(String.valueOf(game.getMetacritic())); 

		  List<String> listDeveloper = new ArrayList<String>();
		  for (Developer dev : game.getDevelopers()) {
			  listDeveloper.add(dev.getName());
		  }
    	ObservableList<String> obsDeveloper = FXCollections.observableArrayList(listDeveloper);
		  developers.setItems((ObservableList<String>) obsDeveloper);

		  List<String> listGenres = new ArrayList<String>();
		  for (Genre genre : game.getGenres()) {
			  listGenres.add(genre.getName());
		  }
    	ObservableList<String> obsGenre = FXCollections.observableArrayList(listGenres);
		  genres.setItems((ObservableList<String>) obsGenre);

		  List<String> listPlatform = new ArrayList<String>();
		  for (Platform plat : game.getPlatforms()) {
			  listPlatform.add(plat.getName());
		  }
    	ObservableList<String> obsPlatform = FXCollections.observableArrayList(listPlatform);
		  platforms.setItems((ObservableList<String>) obsPlatform);
		  
		session = GiarSession.getInstance();
		user = session.getLoggedUser();
		
		if(user.alreadyVoted(name.getText())) {
			yourRating.setValue(user.getPreviousVote(name.getText()));
    	}
		
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
    @FXML
    void rateGame(ActionEvent event) {
    	GiarSession session;
    	User user;
    	
    	session = GiarSession.getInstance();
    	user = session.getLoggedUser();
    	
    	String value = yourRating.getValue();

    	user.rateGame(value, name.getText());
    	
    	
    	
    }
    
    
}