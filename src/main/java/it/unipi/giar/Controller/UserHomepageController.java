package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXTextField;

import it.unipi.giar.Data.Game;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class UserHomepageController {

    @FXML
    private JFXTextField searchGames;
    
    @FXML
    void searchGames(KeyEvent event) {
    	Game.searchGames(searchGames.getText());
    }
}
