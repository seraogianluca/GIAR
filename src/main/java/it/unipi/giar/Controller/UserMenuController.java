package it.unipi.giar.Controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import it.unipi.giar.GiarSession;
import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserMenuController {

	@FXML
	private SplitPane splitPaneLeft;

	@FXML
	private AnchorPane homepageMenuPane;

	@FXML
	private AnchorPane logoMenuPanel;

	@FXML
	private JFXButton userNameMenuPanel;

	@FXML
	private JFXButton socialMenuPanel;

	@FXML
	private JFXButton wishlistMenuPanel;

	@FXML
	private JFXButton myGamesMenuPanel;

	@FXML
	private JFXComboBox<String> topPerPlatformMenuPanel;

	@FXML
	private JFXComboBox<String> platformBrowseMenuPanel;

	@FXML
	private JFXComboBox<String> yearBrowseMenuPanel;

	@FXML
	private JFXComboBox<String> genreBrowseMenuPanel;

	@FXML
	private ImageView logout;

	@FXML
	private SplitPane splitPaneRight;

	@FXML
	private AnchorPane anchorPaneRight;

	@FXML
	private AnchorPane anchorPaneLeft;

	public void initialize() {
		try {
			User user;
			AnchorPane pane;

			GiarSession session = GiarSession.getInstance();
			user = session.getLoggedUser();

			userNameMenuPanel.setText(user.getNickname());

			logoMenuPanel.setCursor(Cursor.HAND); // Change cursor to hand
			userNameMenuPanel.setCursor(Cursor.HAND);
			socialMenuPanel.setCursor(Cursor.HAND);
			wishlistMenuPanel.setCursor(Cursor.HAND);
			myGamesMenuPanel.setCursor(Cursor.HAND);
			topPerPlatformMenuPanel.setCursor(Cursor.HAND);
			platformBrowseMenuPanel.setCursor(Cursor.HAND);
			yearBrowseMenuPanel.setCursor(Cursor.HAND);
			genreBrowseMenuPanel.setCursor(Cursor.HAND);
			logout.setCursor(Cursor.HAND);

			pane = FXMLLoader.load(getClass().getResource("/fxml/UserHomepage.fxml"));
			anchorPaneRight.getChildren().setAll(pane);

			ObservableList<String> platforms = FXCollections.observableArrayList(Game.getAllPlatform()); // mongo
			topPerPlatformMenuPanel.setItems(platforms);
			platformBrowseMenuPanel.setItems(platforms);

			ObservableList<String> years = FXCollections.observableArrayList(Game.getAllYears());
			yearBrowseMenuPanel.setItems(years);

			ObservableList<String> genres = FXCollections.observableArrayList(Game.getAllGenres());
			genreBrowseMenuPanel.setItems(genres);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAllValueProperty() {
		genreBrowseMenuPanel.valueProperty().set(null);
		topPerPlatformMenuPanel.valueProperty().set(null);
		platformBrowseMenuPanel.valueProperty().set(null);
		yearBrowseMenuPanel.valueProperty().set(null);
	}

	@FXML
	private void openUser(ActionEvent event) {
		setAllValueProperty();

		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserProfile.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openHomepage(MouseEvent event) {
		setAllValueProperty();

		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserHomepage.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void logout(MouseEvent event) {
		try {
			Parent root;
			Stage stage;

			root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));

			stage = (Stage) logout.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openTopPlatform(ActionEvent event) {
		String platform = topPerPlatformMenuPanel.getValue();

		genreBrowseMenuPanel.valueProperty().set(null);
		platformBrowseMenuPanel.valueProperty().set(null);
		yearBrowseMenuPanel.valueProperty().set(null);

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/UserTopPerPlatform.fxml"));
			AnchorPane pane = loader.load();

			UserTopPerPlatformController controller = loader.getController();
			controller.initialize(platform);
			anchorPaneRight.getChildren().setAll(pane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void loadBrowsePage(String value, String browseType) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/UserBrowse.fxml"));
			AnchorPane pane = loader.load();

			UserBrowseController controller = loader.getController();
			controller.initialize(value, browseType);

			anchorPaneRight.getChildren().setAll(pane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openYearBrowse(ActionEvent event) {
		String yearValue = yearBrowseMenuPanel.getValue();

		if (yearValue != null) {
			genreBrowseMenuPanel.valueProperty().set(null);
			topPerPlatformMenuPanel.valueProperty().set(null);
			platformBrowseMenuPanel.valueProperty().set(null);

			loadBrowsePage(yearValue, "year");
		}
	}

	@FXML
	void openPlatformBrowse(ActionEvent event) {
		String platformValue = platformBrowseMenuPanel.getValue();

		if (platformValue != null) {
			genreBrowseMenuPanel.valueProperty().set(null);
			topPerPlatformMenuPanel.valueProperty().set(null);
			yearBrowseMenuPanel.valueProperty().set(null);

			loadBrowsePage(platformValue, "platform");
		}
	}

	@FXML
	void openGenreBrowse(ActionEvent event) {
		String genreValue = genreBrowseMenuPanel.getValue();

		if (genreValue != null) {
			topPerPlatformMenuPanel.valueProperty().set(null);
			platformBrowseMenuPanel.valueProperty().set(null);
			yearBrowseMenuPanel.valueProperty().set(null);

			loadBrowsePage(genreValue, "genre");
		}
	}

	@FXML
	void openWishlist(ActionEvent event) {
		loadListPage("Wishlist");
	}

	@FXML
	void openMyGames(ActionEvent event) {
		loadListPage("MyGames");
	}

	void loadListPage(String listType) {
		genreBrowseMenuPanel.valueProperty().set(null);
		topPerPlatformMenuPanel.valueProperty().set(null);
		platformBrowseMenuPanel.valueProperty().set(null);
		yearBrowseMenuPanel.valueProperty().set(null);

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/UserList.fxml"));
			AnchorPane pane = loader.load();

			UserListController controller = loader.getController();
			controller.initialize(listType);

			anchorPaneRight.getChildren().setAll(pane);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void openSocial(ActionEvent event) {
		genreBrowseMenuPanel.valueProperty().set(null);
		topPerPlatformMenuPanel.valueProperty().set(null);
		platformBrowseMenuPanel.valueProperty().set(null);
		yearBrowseMenuPanel.valueProperty().set(null);

		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserSocial.fxml"));
			anchorPaneRight.getChildren().setAll(pane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
