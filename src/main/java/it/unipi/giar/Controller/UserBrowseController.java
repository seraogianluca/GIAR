package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import it.unipi.giar.Data.Game;
import it.unipi.giar.Data.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class UserBrowseController {
	
	private User user;
	
    private ObservableList<GameTable> games;
    
    @FXML
    private Text browse;

    @FXML
    private Text browseType;

    @FXML
    private JFXTreeTableView<GameTable> gamesTable1;
    
    public void initialize(String value, String type) {
    	browseType.setText(value);
    	//this.user = UserMenuController.user;
    	
    	JFXTreeTableColumn<GameTable, String> gameName = new JFXTreeTableColumn<GameTable, String>("Name"); 
    	gameName.prefWidthProperty().bind(gamesTable1.widthProperty().divide(2));
        gameName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                return param.getValue().getValue().name;
            }
        });        
        JFXTreeTableColumn<GameTable, String> gameRating = new JFXTreeTableColumn<GameTable, String>("Rating"); 
        gameRating.prefWidthProperty().bind(gamesTable1.widthProperty().divide(2));
        gameRating.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                return param.getValue().getValue().rating;
            }           
        });
        
        gamesTable1.setRowFactory(tv->{
            JFXTreeTableRow<GameTable> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked(event -> {
                    GameTable rowData = row.getItem();             
                    openGameInfo(rowData.name.get());                    
            });
            return row ;
        });
        
        games = FXCollections.observableArrayList();
  
        final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
        gamesTable1.getColumns().setAll(gameName, gameRating);
        gamesTable1.setRoot(root);
        gamesTable1.setShowRoot(false);
        
        
        ArrayList<Game> browseResult = null;
        if (type == "platform") {
        	
        	//browseResult = Game.BrowseGamesPerPlatform(value);	//TO DO 
        	browseResult = Game.searchGames("fif");//	this is a test to see if worked the table. to be deleted
        } else if (type == "year") {
        	
        	//browseResult = Game.BrowseGamesPerYear(value);	//TO DO 
        	browseResult = Game.searchGames("de");//	this is a test to see if worked the table. to be deleted
        }else if (type == "genre") {
        	
        	//browseResult = Game.BrowseGamesPerGenre(value);	//TO DO 
        	browseResult = Game.searchGames("ac");//	this is a test to see if worked the table. to be deleted
        }
        
    	for(Game game : browseResult) {
    		games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
    	}
    }
    
    void openGameInfo(String name) {
    	try { 		
    		Scene scene = browse.getScene();
    		AnchorPane pane = (AnchorPane)scene.lookup("#anchorPaneRight");
    	    		
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/InfoGame.fxml"));
            AnchorPane newPane = loader.load();
    		
    	    InfoGameController controller = loader.getController();            
    	    //controller.initialize(user, Game.findGame(name));
            
            pane.getChildren().setAll(newPane);          

		} catch (Exception e) {
			// TODO Auto-generated catch block
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
