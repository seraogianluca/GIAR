package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import it.unipi.giar.Controller.UserHomepageController.GameTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class UserBrowseController {

    @FXML
    private Text Browse;

    @FXML
    private Text browseType;

   /* @FXML
    private JFXTreeTableView<GameTable> gamesTable;*/
    
    
   
    
    public void initialize(String type) {
    	browseType.setText(type);
    	
    }
    
}
