package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import it.unipi.giar.Data.Game;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class UserSocialWishlistController {

	private ObservableList<GameTable> games;

	@FXML
	private Text title;

	@FXML
	private JFXButton back;

	@FXML
	private JFXTreeTableView<GameTable> gamesTable;

	public void initialize(String friendNick) {

		title.setText(friendNick + " : Wishlist");

		JFXTreeTableColumn<GameTable, String> gameName = new JFXTreeTableColumn<GameTable, String>("Name"); 
		gameName.prefWidthProperty().bind(gamesTable.widthProperty().divide(2));
		gameName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
				return param.getValue().getValue().name;
			}
		});        
		JFXTreeTableColumn<GameTable, String> gameRating = new JFXTreeTableColumn<GameTable, String>("Rating"); 
		gameRating.prefWidthProperty().bind(gamesTable.widthProperty().divide(2));
		gameRating.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
				return param.getValue().getValue().rating;
			}           
		});

		gamesTable.setRowFactory(tv->{
			JFXTreeTableRow<GameTable> row = new JFXTreeTableRow<>();
			row.setOnMouseClicked(event -> {
				GameTable rowData = row.getItem();             
				openGameInfo(rowData.name.get());                    
			});
			return row ;
		});

		games = FXCollections.observableArrayList();

		final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
		gamesTable.getColumns().setAll(gameName, gameRating);
		gamesTable.setRoot(root);
		gamesTable.setShowRoot(false);

		ArrayList<Game> wishlist = Game.getFriendWishlist(friendNick);  

		for(Game game : wishlist) {
			games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
		}
	}

	@FXML
	void back(ActionEvent event) {
		try { 		

			FXMLLoader loader;
			Scene scene;
			AnchorPane pane;
			AnchorPane newPane;

			scene = title.getScene();
			pane = (AnchorPane)scene.lookup("#anchorPaneRight");

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/UserSocial.fxml"));
			newPane = loader.load();

			pane.getChildren().setAll(newPane);      

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void openGameInfo(String gameName) {
		try { 		

			FXMLLoader loader;
			InfoGameController controller;
			Scene scene;
			AnchorPane pane;
			AnchorPane newPane;

			scene = title.getScene();
			pane = (AnchorPane)scene.lookup("#anchorPaneRight");

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/InfoGame.fxml"));
			newPane = loader.load();

			controller = loader.getController();            
			controller.initialize(gameName);

			pane.getChildren().setAll(newPane);      

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class GameTable extends RecursiveTreeObject<GameTable> {

		StringProperty name;
		StringProperty rating;

		public GameTable(String name, String rating) {
			this.name = new SimpleStringProperty(name);
			this.rating = new SimpleStringProperty(rating);
		}
	}
}