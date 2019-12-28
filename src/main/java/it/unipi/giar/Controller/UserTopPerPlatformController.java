package it.unipi.giar.Controller;

import java.util.ArrayList;

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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class UserTopPerPlatformController {
	
    private ObservableList<GameTable> games;
    
    @FXML
    private Text top;

    @FXML
    private Text plat;
    
    @FXML
    private JFXTreeTableView<GameTable> gamesTable3;
    
    @SuppressWarnings("unchecked")
	public void initialize(String value) {
    	//this.user = UserMenuController.user;
    	plat.setText(value);
    	JFXTreeTableColumn<GameTable, String> gameName = new JFXTreeTableColumn<GameTable, String>("Name"); 
    	gameName.prefWidthProperty().bind(gamesTable3.widthProperty().divide(2));
        gameName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                return param.getValue().getValue().name;
            }
        });        
        JFXTreeTableColumn<GameTable, String> gameRating = new JFXTreeTableColumn<GameTable, String>("Rating"); 
        gameRating.prefWidthProperty().bind(gamesTable3.widthProperty().divide(2));
        gameRating.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                return param.getValue().getValue().rating;
            }           
        });
        
        gamesTable3.setRowFactory(tv->{
            JFXTreeTableRow<GameTable> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked(event -> {
                    GameTable rowData = row.getItem();             
                    openGameInfo(rowData.name.get());                    
            });
            return row ;
        });
        
        games = FXCollections.observableArrayList();
  
        final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
        gamesTable3.getColumns().setAll(gameName, gameRating);
        gamesTable3.setRoot(root);
        gamesTable3.setShowRoot(false);
        
        
        ArrayList<Game> topResult = null;
       // topResult = Game.TopPerPlatform(value);	//TO DO 
        topResult = Game.searchGames("uo");//	this is a test to see if worked the table. to be deleted
                
    	for(Game game : topResult) {
    		games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
    	}
    }
    
    void openGameInfo(String name) {
    	try { 		
    		Scene scene = top.getScene();
    		AnchorPane pane = (AnchorPane)scene.lookup("#anchorPaneRight");
    	    		
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/InfoGame.fxml"));
            AnchorPane newPane = loader.load();
    		
    	    //InfoGameController controller = loader.getController();            
    	    //controller.initialize(user, Game.findGame(name));
            
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