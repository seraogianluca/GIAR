package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.unipi.giar.Data.Developer;
import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.Genre;
import it.unipi.giar.Data.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;


public class AdminInfoGameController {

	Game game;

	@FXML
	private Text name;

	@FXML
	private Text description;

	@FXML
	private Text rating;

	@FXML
	private JFXButton updateButton;

	@FXML
	private JFXButton deleteButton;

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
		Date date;
		DateFormat dateFormat;

		game = Game.findGame(gameName);

		name.setText(game.getName());
		description.setText(game.getDescription());
		rating.setText(Double.toString(game.getRating()));

		date = game.getReleased();
		dateFormat = new SimpleDateFormat("yyyy-mm-dd");

		metacritic.setText(Integer.toString(game.getMetacritic()));

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
	}

	@FXML
	void delete(ActionEvent event) {    	
		try {
			game.deleteGame(game.getName());

			AlertBoxController.display();
			
			FXMLLoader loader;
			Scene scene;
			AnchorPane pane;
			AnchorPane newPane;

			scene = name.getScene();
			pane = (AnchorPane) scene.lookup("#anchorPaneRight");

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AdminHomepage.fxml"));
			newPane = loader.load();

			pane.getChildren().setAll(newPane);
		} catch (Exception e) {
			e.printStackTrace();
		}    	
	}

	@FXML
	void update(ActionEvent event) {
		try {
			FXMLLoader loader;
			AdminUpdateGameController controller;
			Scene scene;
			AnchorPane pane;
			AnchorPane newPane;

			scene = name.getScene();
			pane = (AnchorPane)scene.lookup("#anchorPaneRight");

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AdminUpdateGame.fxml"));
			newPane = loader.load();
			
			controller = loader.getController();
			controller.initialize(game.getName());

			pane.getChildren().setAll(newPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}