package it.unipi.giar.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import it.unipi.giar.GiarSession;
import it.unipi.giar.TwitterConnector;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class InfoGameController {

	Game game;

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

	@FXML
	private JFXButton back;
	
	@FXML
	private Text positive;

	@FXML
	private Text negative;

	public void initialize(String gameName) {
		GiarSession session;
		User user;
		Date date;
		DateFormat dateFormat;

		game = Game.findGame(gameName);

		name.setText(game.getName());
		description.setText(game.getDescription());
		rating.setText(Double.toString(game.getRating()));

		date = game.getReleased();
		dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		released.setText(dateFormat.format(date));

		metacritic.setText(Integer.toString(game.getMetacritic()));

		ObservableList<String> ratingValues = FXCollections.observableArrayList();
		ratingValues.addAll("1", "2", "3", "4", "5");
		yourRating.setItems(ratingValues);

		List<String> listDeveloper = new ArrayList<String>();
		for (Developer dev : game.getDevelopers()) {
			if(!listDeveloper.contains(dev.getName()))
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

		if (user.rated(name.getText())) {
			yourRating.setValue(user.getRate(name.getText()));
		}

		if (user.isInMyGames(game.getName())) {
			addToWishlistButton.setDisable(false);
			addToMyGamesButton.setDisable(true);
		} else if (user.isInWishlist(game.getName())) {
			addToWishlistButton.setDisable(true);
			addToMyGamesButton.setDisable(false);
		} else {
			addToWishlistButton.setDisable(false);
			addToMyGamesButton.setDisable(false);
		}
		
		//Search for opinions on twitter
		final Thread sentimentAnalysis = new Thread() {
			public void run() {
				ArrayList<Integer> opinions = TwitterConnector.sentimentAnalysis(gameName);
				if(opinions.size() == 0) {
					positive.setText(Integer.toString(0));
					negative.setText(Integer.toString(0));
				} else {
					positive.setText(opinions.get(0).toString());
					negative.setText(opinions.get(1).toString());
				}
			}
		};
		
		sentimentAnalysis.start();
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

		game.rate(yourRating.getValue(), user.getRate(name.getText()));
		user.rate(name.getText(), yourRating.getValue());
		rating.setText(Double.toString(game.getRating()));
	}

	@FXML
	void back(ActionEvent event) {
		try {
			FXMLLoader loader;
			Scene scene;
			AnchorPane pane;
			AnchorPane newPane;

			scene = name.getScene();
			pane = (AnchorPane) scene.lookup("#anchorPaneRight");

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/UserHomepage.fxml"));
			
			newPane = loader.load();

			pane.getChildren().setAll(newPane);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}