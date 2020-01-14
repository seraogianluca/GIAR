package it.unipi.giar.Controller;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javax.swing.SwingUtilities;

import it.unipi.giar.Data.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.application.Platform;

public class AdminInsertGameController {

	@FXML
	private JFXTextField name;

	@FXML
	private JFXTextField date;

	@FXML
	private JFXTextArea description;

	@FXML
	private JFXButton insertGame;

	@FXML
	private Text message;

	@FXML
	private Pane patformsPane;

	@FXML
	private Label platformLabel;

	@FXML
	private JFXComboBox<String> platformCombo;

	@FXML
	private JFXListView<String> platformList;
	private ObservableList<String> platList;

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

	public void initialize() {
		final Runnable chargePlatforms = new Runnable() {
			public void run() {
				Platform.runLater(() -> {
					ObservableList<String> platforms;
					platforms = FXCollections.observableArrayList(Game.getAllPlatform());
					platformCombo.setItems(platforms);
					platList = FXCollections.observableArrayList();
					platformList.setItems(platList);
				});
			}
		};

		final Runnable chargeGenres = new Runnable() {
			public void run() {
				Platform.runLater(() -> {
					ObservableList<String> genres;
					genres = FXCollections.observableArrayList(Game.getAllGenres());
					genresCombo.setItems(genres);
					genList = FXCollections.observableArrayList();
					genresList.setItems(genList);
				});
			}
		};

		final Runnable chargeDevList = new Runnable() {
			public void run() {
				Platform.runLater(() -> {
					devList = FXCollections.observableArrayList();
					developersList.setItems(devList);
				});
			}
		};
		Thread pickerLoad = new Thread() {
			public void run() {
				try {
					SwingUtilities.invokeLater(chargePlatforms);
					SwingUtilities.invokeLater(chargeGenres);
					SwingUtilities.invokeLater(chargeDevList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		pickerLoad.start();
	}

	@FXML
	void insertNewGame(MouseEvent event) {
		ArrayList<String> platformsString = new ArrayList<String>();
		ArrayList<String> genresString = new ArrayList<String>();
		ArrayList<String> developersString = new ArrayList<String>();
		String dateIns;
		String nameIns;
		String descIns;
		String yearIns;

		nameIns = name.getText();
		dateIns = date.getText();
		descIns = description.getText();

		if (!Pattern.matches("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d", dateIns)) {
			setErrorMessage("Please insert a valid date.");
		} else if (platList.size() == 0) {
			setErrorMessage("Please insert at least a platform.");
		} else if (genList.size() == 0) {
			setErrorMessage("Please insert at least a genre.");
		} else if (devList.size() == 0) {
			setErrorMessage("Please insert at least a developer.");
		} else {
			message.setText("");

			String[] dateString = dateIns.split("/");
			String mongoDateString = dateString[2] + "-" + dateString[1] + "-" + dateString[0];
			yearIns = dateString[2];
			platformsString.addAll(platList);
			genresString.addAll(genList);
			developersString.addAll(devList);

			Game.insertGame(nameIns, mongoDateString, descIns, platformsString, genresString, developersString,
					yearIns);
			Game.updateIndexes();
			setAcknowledgement("Game correctly added.");
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

	private void setAcknowledgement(String msg) {
		message.setText(msg);
		message.setFill(Color.web("#7bd500"));
	}

}