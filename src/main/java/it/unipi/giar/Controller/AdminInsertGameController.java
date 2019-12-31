package it.unipi.giar.Controller;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

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
	private JFXComboBox<String> developersCombo;

	@FXML
	private JFXButton devAddButton;

	@FXML
	private JFXButton devRemoveButton;

	private boolean errFlag = false;

	public void initialize() {
		ObservableList<String> platforms;
		ObservableList<String> genres;
		ObservableList<String> developers;

		platforms = FXCollections.observableArrayList(Game.getAllPlatform());	
		genres = FXCollections.observableArrayList(Game.getAllGenres());		
		developers = FXCollections.observableArrayList(Game.getAllDevelopers());

		platformCombo.setItems(platforms);
		genresCombo.setItems(genres);
		developersCombo.setItems(developers);

		platList = FXCollections.observableArrayList();	
		genList = FXCollections.observableArrayList();
		devList = FXCollections.observableArrayList();
		platformList.setItems(platList);
		genresList.setItems(genList);
		developersList.setItems(devList);
	}

	@FXML
	void insertNewGame(MouseEvent event) {
		ArrayList<String> platformsString = new ArrayList<String>();
		ArrayList<String> genresString = new ArrayList<String>();
		ArrayList<String> developersString = new ArrayList<String>();
		String dateIns;
		String nameIns;
		String descIns;

		nameIns = name.getText();
		dateIns = date.getText();
		descIns = description.getText();

		if(!Pattern.matches("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d", dateIns)) {
			setErrorMessage("Please insert a valid date.");
		} else if(platList.size() == 0) {
			setErrorMessage("Please insert at least a platform.");
		} else if(genList.size() == 0) {
			setErrorMessage("Please insert at least a genre.");
		} else if(devList.size() == 0) {
			setErrorMessage("Please insert at least a developer.");
		} else {	
			message.setText("");
			errFlag = false;
			
			String[] dateString = dateIns.split("/");
			String mongoDateString = dateString[2] + "-" + dateString[1] + "-" + dateString[0];
			platformsString.addAll(platList);
			genresString.addAll(genList);
			developersString.addAll(devList);

			Game.insertGame(nameIns, mongoDateString, descIns, platformsString, genresString, developersString);
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
		devList.add(developersCombo.getValue());
		developersCombo.setValue(null);
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
		errFlag = true;
	}

	private void setAcknowledgement(String msg) {
		message.setText(msg);
		message.setFill(Color.web("#7bd500"));
		errFlag = false;
	}

}