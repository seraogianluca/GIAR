package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.application.Platform;
import javax.swing.SwingUtilities;

import it.unipi.giar.Data.Game;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class UserBrowseController {

    private ObservableList<GameTable> games;
    private String type;
    private String value;

    @FXML
    private Text browse;

    @FXML
    private Text browseType;

    @FXML
    private JFXTreeTableView<GameTable> gamesTable1;

    @FXML
    private Hyperlink searchAllGames;

    @FXML
    private JFXProgressBar progressBar;

    @SuppressWarnings("unchecked")
    public void initialize(String value, String type) {
        this.type = type;
        this.value = value; 

        searchAllGames.setVisible(false);
        progressBar.setVisible(false);
        browseType.setText(value);

        JFXTreeTableColumn<GameTable, String> gameName = new JFXTreeTableColumn<GameTable, String>("Name");
        gameName.prefWidthProperty().bind(gamesTable1.widthProperty().divide(4).multiply(3));
        gameName.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                        return param.getValue().getValue().name;
                    }
                });
        JFXTreeTableColumn<GameTable, String> gameRating = new JFXTreeTableColumn<GameTable, String>("Rating");
        gameRating.prefWidthProperty().bind(gamesTable1.widthProperty().divide(4));
        gameRating.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<GameTable, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GameTable, String> param) {
                        return param.getValue().getValue().rating;
                    }
                });

        gamesTable1.setRowFactory(tv -> {
            JFXTreeTableRow<GameTable> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked(event -> {
                GameTable rowData = row.getItem();
                if (rowData != null) {
                    openGameInfo(rowData.name.get());
                }
            });
            return row;
        });

        games = FXCollections.observableArrayList();

        final TreeItem<GameTable> root = new RecursiveTreeItem<GameTable>(games, RecursiveTreeObject::getChildren);
        gamesTable1.getColumns().setAll(gameName, gameRating);
        gamesTable1.setRoot(root);
        gamesTable1.setShowRoot(false);

        ArrayList<Game> browseResult = null;
        if (type == "platform") {
            browseResult = Game.browseGamesPerPlatform(value, false);

        } else if (type == "year") {
            browseResult = Game.browseGamesPerYear(value, false);

        } else if (type == "genre") {
            browseResult = Game.browseGamesPerGenre(value, false);
        }

        for (Game game : browseResult) {
            games.add(new GameTable(game.getName(), Double.toString(game.getRating())));
        }

        if (browseResult.size() >= 10) {
            searchAllGames.setVisible(true);
        } else {
            searchAllGames.setVisible(false);
        }
    }

    void openGameInfo(String gameName) {
        try {
            if (!gameName.isEmpty()) {
                FXMLLoader loader;
                InfoGameController controller;
                Scene scene;
                AnchorPane pane;
                AnchorPane newPane;

                scene = browse.getScene();
                pane = (AnchorPane) scene.lookup("#anchorPaneRight");

                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/InfoGame.fxml"));
                newPane = loader.load();

                controller = loader.getController();
                controller.initialize(gameName);

                pane.getChildren().setAll(newPane);
            }
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

    @FXML
    void searchAllGames() {
        final Runnable chargeAll = new Runnable() {
            public void run() {
                Platform.runLater(() -> {
                    try {
                        games.clear();

                        ArrayList<Game> browseResult = null;
                        if (type == "platform") {
                            browseResult = Game.browseGamesPerPlatform(value, true);

                        } else if (type == "year") {
                            browseResult = Game.browseGamesPerYear(value, true);

                        } else if (type == "genre") {
                            browseResult = Game.browseGamesPerGenre(value, true);
                        }

                        for (Game game : browseResult) {
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

}
