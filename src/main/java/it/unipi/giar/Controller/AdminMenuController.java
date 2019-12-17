package it.unipi.giar.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AdminMenuController {

    @FXML
    private SplitPane splitPaneLeft;

    @FXML
    private AnchorPane anchorPaneLeft;

    @FXML
    private AnchorPane logoMenuPanel;

    @FXML
    private AnchorPane homepageMenuPane;

    @FXML
    private JFXButton adminNameMenuPanel;

    @FXML
    private JFXButton statisticMenuPanel;

    @FXML
    private JFXButton newgameMenuPanel;

    @FXML
    private ImageView logout;

    @FXML
    private SplitPane splitPaneRight;

    @FXML
    private AnchorPane anchorPaneRight;

    @FXML
    void logout(MouseEvent event) {
    	System.out.println("BYE");  
    }

    @FXML
    void openAdmin(ActionEvent event) {

    }

    @FXML
    void openHomepage(MouseEvent event) {

    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/AdminHomepage.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }

    @FXML
    void openInsertNewGame(ActionEvent event) {
    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/AdminInsertGame.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
    }

    @FXML
    void openStatistics(ActionEvent event) {
    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/AdminStat.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    public void initialize() {
    		
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/AdminHomepage.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

    }

}