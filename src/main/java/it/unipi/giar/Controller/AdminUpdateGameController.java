package it.unipi.giar.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import it.unipi.giar.Data.Developer;
import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.Genre;
import it.unipi.giar.Data.Platform;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AdminUpdateGameController {

	private Game game;

	@FXML
	private Label gameTitle;

	@FXML
	private JFXTextField date;

	@FXML
	private JFXTextArea description;

	@FXML
	private JFXButton updateButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private Text message;

	@FXML
	private Pane patformsPane;

	@FXML
	private Label platformLabel;

	@FXML
	private JFXListView<String> platformList;
	private ObservableList<String> platList;

	@FXML
	private JFXComboBox<String> platformCombo;

	@FXML
	private JFXButton platAddButton;

	@FXML
	private JFXButton platRemoveButton;

	@FXML
	private Pane genresPane;

	@FXML
	private Label genresLabel;

	@FXML
	private JFXListView<String> genresList;
	private ObservableList<String> genList;

	@FXML
	private JFXComboBox<String> genresCombo;

	@FXML
	private JFXButton genAddButton;

	@FXML
	private JFXButton genRemoveButton;

	@FXML
	private Pane developersPane;

	@FXML
	private Label developersLabel;

	@FXML
	private JFXListView<String> developersList;
	private ObservableList<String> devList;

	@FXML
	private JFXTextField developersCombo;

	@FXML
	private JFXButton devAddButton;

	@FXML
	private JFXButton devRemoveButton;

	public void initialize(final String gameName) {
		DateFormat dateForm;
		dateForm = new SimpleDateFormat("dd/mm/yyyy");

		game = Game.findGame(gameName);
		gameTitle.setText(gameName);
		date.setText(dateForm.format(game.getReleased()));
		description.setText(game.getDescription());

		final Runnable chargePlatforms = new Runnable() {
			public void run() {
				javafx.application.Platform.runLater(() -> {
					ObservableList<String> platforms;
					platforms = FXCollections.observableArrayList(Game.getAllPlatform());
					platformCombo.setItems(platforms);
					platList = FXCollections.observableArrayList();

					for (final Platform plat : game.getPlatforms()) {
						platList.add(plat.getName());
					}
					platformList.setItems(platList);
				});
			}
		};

		final Runnable chargeGenres = new Runnable() {
			public void run() {
				javafx.application.Platform.runLater(() -> {
					ObservableList<String> genres;
					genres = FXCollections.observableArrayList(Game.getAllGenres());
					genresCombo.setItems(genres);
					genList = FXCollections.observableArrayList();

					for (Genre genre : game.getGenres()) {
						genList.add(genre.getName());
					}
					genresList.setItems(genList);
				});
			}
		};

		final Runnable chargeDevList = new Runnable() {
			public void run() {
				javafx.application.Platform.runLater(() -> {
					devList = FXCollections.observableArrayList();

					for (Developer dev : game.getDevelopers()) {
						devList.add(dev.getName());
					}
					developersList.setItems(devList);
				});
			}
		};
		final Thread pickerLoad = new Thread() {
			public void run() {
				try {
					SwingUtilities.invokeLater(chargePlatforms);
					SwingUtilities.invokeLater(chargeGenres);
					SwingUtilities.invokeLater(chargeDevList);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		};

		pickerLoad.start();

	}

	@FXML
	void cancel(MouseEvent event) {
		try {
			FXMLLoader loader;
			AdminInfoGameController controller;
			Scene scene;
			AnchorPane pane;
			AnchorPane newPane;

			scene = date.getScene();
			pane = (AnchorPane) scene.lookup("#anchorPaneRight");

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/AdminInfoGame.fxml"));
			newPane = loader.load();

			controller = loader.getController();
			controller.initialize(game.getName());

			pane.getChildren().setAll(newPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void update(MouseEvent event) {
		try {
			ArrayList<String> platformsString = new ArrayList<String>();
			ArrayList<String> genresString = new ArrayList<String>();
			ArrayList<String> developersString = new ArrayList<String>();
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/mm/yyyy");

			if (!Pattern.matches("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d", date.getText())) {
				setErrorMessage("Please insert a valid date.");
			} else if (platList.size() == 0) {
				setErrorMessage("Please insert at least a platform.");
			} else if (genList.size() == 0) {
				setErrorMessage("Please insert at least a genre.");
			} else if (devList.size() == 0) {
				setErrorMessage("Please insert at least a developer.");
			} else {
				message.setText("");

				game.setDescription(description.getText());
				game.setReleased(formatDate.parse(date.getText()));

				platformsString.addAll(platList);
				genresString.addAll(genList);
				developersString.addAll(devList);

				game.setPlatforms(platformsString);
				game.setGenres(genresString);
				game.setDevelopers(developersString);

				Game.updateGame(game);
				Game.updateIndexes();
				
				AlertBoxController.display("Game correctly updated!");
				FXMLLoader loader;
				Scene scene;
				AnchorPane pane;
				AnchorPane newPane;

				scene = gameTitle.getScene();
				pane = (AnchorPane) scene.lookup("#anchorPaneRight");

				loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/fxml/AdminHomepage.fxml"));
				newPane = loader.load();

				pane.getChildren().setAll(newPane);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void insertPlatform(ActionEvent event) {
		platList.add(platformCombo.getValue());
		platformCombo.setValue(null);
	}

	@FXML
	void insertGenre(ActionEvent event) {
		genList.add(genresCombo.getValue());
		genresCombo.setValue(null);
	}

	@FXML
	void insertDeveloper(ActionEvent event) {
		devList.add(developersCombo.getText());
		developersCombo.setText(null);
	}

	@FXML
	void removePlatform(ActionEvent event) {
		platList.remove(platformList.getSelectionModel().getSelectedItem());
	}

	@FXML
	void removeGenre(ActionEvent event) {
		genList.remove(genresList.getSelectionModel().getSelectedItem());
	}

	@FXML
	void removeDeveloper(ActionEvent event) {
		devList.remove(developersList.getSelectionModel().getSelectedItem());
	}

	private void setErrorMessage(String msg) {
		message.setText(msg);
		message.setFill(Color.web("#db524b"));
	}

}