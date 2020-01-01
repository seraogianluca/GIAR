package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import it.unipi.giar.GiarSession;
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

public class UserListController {
	
    private ObservableList<GameTable> games;
    
    @FXML
    private Text listType;
    @FXML
    private JFXTreeTableView<GameTable> gamesTable2;
    
    @SuppressWarnings("unchecked")
	public void initialize(String type) {
    	User user;
    	
    	GiarSession session = GiarSession.getInstance();
    	user = session.getLoggedUser();

    	listType.setText(type);
    	
    	JFXTreeTableColumn<GameTable, String> gameName = new JFXTreeTableColumn<GameTable, String>("Name"); 
    	gameName.prefWidthProperty().bind(gamesTable2.widthProperty().divide(4).multiply(3));
        gameName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                return param.getValue().getValue().name;
            }
        });        
        JFXTreeTableColumn<GameTable, String> gameRating = new JFXTreeTableColumn<GameTable, String>("Rating"); 
        gameRating.prefWidthProperty().bind(gamesTable2.widthProperty().divide(4));
        gameRating.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                return param.getValue().getValue().rating;
            }           
        });
        
        gamesTable2.setRowFactory(tv->{
            JFXTreeTableRow<GameTable> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked(event -> {
                    GameTable rowData = row.getItem();             
                    openGameInfo(rowData.name.get());                    
            });
            return row ;
        });
        
        games = FXCollections.observableArrayList();
  
        final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
        gamesTable2.getColumns().setAll(gameName, gameRating);
        gamesTable2.setRoot(root);
        gamesTable2.setShowRoot(false);     
        
        ArrayList<Game> browseResult = new ArrayList<Game>();
        
        if (type == "Wishlist") {      	
        	browseResult = user.getWishlist();	
        	
        } else if (type == "MyGames") {
        	browseResult = user.getMyGames();	  	   	
        }
        
    	for(Game game : browseResult) {
    		games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
    	}
    }
    
    void openGameInfo(String gameName) {
    	try { 		
    		
    		FXMLLoader loader;
    		InfoGameController controller;
    		Scene scene;
    		AnchorPane pane;
    		AnchorPane newPane;
    		
    		scene = listType.getScene();
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