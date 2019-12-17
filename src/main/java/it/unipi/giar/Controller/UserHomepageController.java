package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import it.unipi.giar.Data.Game;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class UserHomepageController {
	
	private ObservableList<GameTable> games;

    @FXML
    private JFXTextField searchGames;
    
    @FXML
    private JFXTreeTableView<GameTable> gamesTable;
    
    public void initialize() {
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
        
        games = FXCollections.observableArrayList();
  
        final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
        gamesTable.getColumns().setAll(gameName, gameRating);
        gamesTable.setRoot(root);
        gamesTable.setShowRoot(false);
    }
    
    @FXML
    void searchGames(KeyEvent event) {
    	ArrayList<Game> searchResult = Game.searchGames(searchGames.getText());
    	System.out.println(searchResult);
    	
    	games.clear();
    	
    	for(Game game : searchResult) {
    		System.out.println(game.getName());
    		games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
    	}
    }
    
    class GameTable extends RecursiveTreeObject<GameTable> {

        StringProperty name;
        StringProperty rating;

        public GameTable(String name, String rating) {
            this.name = new SimpleStringProperty(name);
            this.rating = new SimpleStringProperty(rating);
        }
        
        public String getName() {
        	return this.getName();
        }

    }
}
