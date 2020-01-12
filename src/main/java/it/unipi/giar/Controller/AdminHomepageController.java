package it.unipi.giar.Controller;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXProgressBar;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.application.Platform;


public class AdminHomepageController {

    private ObservableList<GameTable> games;
	
    @FXML
    private AnchorPane PaneRight;

    @FXML
    private JFXTextField searchGames;

    @FXML
    private JFXTreeTableView<GameTable> gamesTable;

    @FXML
    private Hyperlink searchAllGames;

    @FXML
    private JFXProgressBar progressBar;

    @SuppressWarnings("unchecked")
	public void initialize() {
        searchAllGames.setVisible(false);
        progressBar.setVisible(false);

        JFXTreeTableColumn<GameTable, String> gameName = new JFXTreeTableColumn<GameTable, String>("Name");
        gameName.prefWidthProperty().bind(gamesTable.widthProperty().divide(4).multiply(3));
        gameName.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                        return param.getValue().getValue().name;
                    }
                });

        JFXTreeTableColumn<GameTable, String> gameRating = new JFXTreeTableColumn<GameTable, String>("Rating");
        gameRating.prefWidthProperty().bind(gamesTable.widthProperty().divide(4));
        gameRating.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                        return param.getValue().getValue().rating;
                    }
                });

        gamesTable.setRowFactory(tv -> {
            JFXTreeTableRow<GameTable> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked(event -> {
                GameTable rowData = row.getItem();
                openGameInfo(rowData.name.get());
            });
            return row;
        });

        games = FXCollections.observableArrayList();

        final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
        gamesTable.getColumns().setAll(gameName, gameRating);
        gamesTable.setRoot(root);
        gamesTable.setShowRoot(false);
    }
    
    @FXML
    void searchGames(KeyEvent event) {
        games.clear();
        searchAllGames.setVisible(false);

        if (searchGames.getText().isEmpty()
                || (event.getText().isEmpty() && !event.getCode().name().equals("BACK_SPACE"))) {
            games.clear();
        } else if (event.getCode().name().equals("BACK_SPACE") 
        || (searchGames.getText().charAt(searchGames.getText().length() - 1) == (event.getText().charAt(0)))){
            
            ArrayList<Game> searchResult = Game.searchGames(searchGames.getText(), false);

            if (searchResult.size() >= 10) {
                searchAllGames.setVisible(true);
            } else {
                searchAllGames.setVisible(false);
            }

            for (Game game : searchResult) {
                games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
            }
        }
    }

    @FXML
    void searchAllGames() {
        final Runnable chargeAll = new Runnable() {
            public void run() {
                Platform.runLater(() -> {
                    try {
                        games.clear();

                        ArrayList<Game> searchResult = Game.searchGames(searchGames.getText(), true);

                        for (Game game : searchResult) {
                            games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
                        }

                        progressBar.setVisible(false);
                        searchAllGames.setVisible(true);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("index exception");
                    }
                });
            }
        };

        Thread stockPicker = new Thread() {
            public void run() {
                try {
                    progressBar.setVisible(true);
                    searchAllGames.setVisible(false);

                    SwingUtilities.invokeAndWait(chargeAll);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        stockPicker.start();
    }

    void openGameInfo(String gameName) {
        try {
            FXMLLoader loader;
            AdminInfoGameController controller;
            Scene scene;
            AnchorPane pane;
            AnchorPane newPane;

            scene = searchGames.getScene();
            pane = (AnchorPane) scene.lookup("#anchorPaneRight");

            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/AdminInfoGame.fxml"));
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
