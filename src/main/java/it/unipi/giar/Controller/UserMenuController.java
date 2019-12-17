package it.unipi.giar.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import it.unipi.giar.Data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class UserMenuController {
	
	private User user;

    @FXML
    private SplitPane splitPaneLeft;

    @FXML
    private AnchorPane logoMenuPanel;

    @FXML
    private JFXButton userNameMenuPanel;

    @FXML
    private JFXButton socialMenuPanel;

    @FXML
    private JFXButton wishlistMenuPanel;

    @FXML
    private JFXButton myGamesMenuPanel;

    @FXML
    private JFXComboBox<String> topPerPlatformMenuPanel;

    @FXML
    private JFXComboBox<String> platformBrowseMenuPanel;

    @FXML
    private JFXComboBox<String> yearBrowseMenuPanel;

    @FXML
    private JFXComboBox<String> genreBrowseMenuPanel;

    @FXML
    private ImageView logout;

    @FXML
    private SplitPane splitPaneRight;
    
    @FXML
    private AnchorPane anchorPaneRight;
    
    @FXML
    private AnchorPane anchorPaneLeft;

    @FXML
    void logout(MouseEvent event) {
    	System.out.println("BYE");
    }    
    
    @FXML
    void openTopPlatform(ActionEvent event) {
    	String platform = topPerPlatformMenuPanel.getValue();
    
    	genreBrowseMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	
    	//query a mongo per listare i giochi di quella platform
    	//topPerPlatformMenuPanel.valueProperty().set(null);//  questa va chiamata o con un altro evento o dopo aver caricato la pagina nuova
     	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserTopPerPlatform.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    
    }
    
    @FXML
    void openPlatformBrowse(ActionEvent event) {
    	String platform = platformBrowseMenuPanel.getValue();
    	
    	genreBrowseMenuPanel.valueProperty().set(null);
    	topPerPlatformMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	
    	
    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserBrowse.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }
    
    @FXML
    void openGenreBrowse(ActionEvent event) {
    	String genre = genreBrowseMenuPanel.getValue();

    	topPerPlatformMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	
     	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserBrowse.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    
    @FXML
    void openYearBrowse(ActionEvent event) {
    	String year = yearBrowseMenuPanel.getValue();
    	
    	genreBrowseMenuPanel.valueProperty().set(null);
    	topPerPlatformMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	
     	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserBrowse.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    
    @FXML
    void openMyGames(ActionEvent event) {
    	genreBrowseMenuPanel.valueProperty().set(null);
    	topPerPlatformMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	
    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserMyGames.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }
    
    @FXML
    void openUser(ActionEvent event) {
    	genreBrowseMenuPanel.valueProperty().set(null);
    	topPerPlatformMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	
    	
    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserProfile.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    @FXML
    void openWishlist(ActionEvent event) {
    	genreBrowseMenuPanel.valueProperty().set(null);
    	topPerPlatformMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	

    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserWishlist.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }

    @FXML
    void openSocial(ActionEvent event) {
    	genreBrowseMenuPanel.valueProperty().set(null);
    	topPerPlatformMenuPanel.valueProperty().set(null);
    	platformBrowseMenuPanel.valueProperty().set(null);
    	yearBrowseMenuPanel.valueProperty().set(null);
    	
    	try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/UserSocial.fxml"));
			anchorPaneRight.getChildren().setAll(pane);
    	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }


    public void initialize() {
    	try {
    		//populate the combobox for platforms
    		//ObservableList<String> platforms = FXCollections.observableArrayList(Game.getAllPlatformsList()); //mongo   		
    		List<String> platforms1 = Files.readAllLines(new File("src/main/resources/platforms.txt").toPath(), Charset.defaultCharset());
			ObservableList<String> platforms = FXCollections.observableArrayList(platforms1);
    		topPerPlatformMenuPanel.setItems(platforms);
			platformBrowseMenuPanel.setItems(platforms);
			
			//populate the combobox for years
			//ObservableList<String> years = FXCollections.observableArrayList(Game.getAllYearsList());			
			List<String> years1 = Files.readAllLines(new File("src/main/resources/years.txt").toPath(), Charset.defaultCharset());
			ObservableList<String> years = FXCollections.observableArrayList(years1);
	        yearBrowseMenuPanel.setItems(years);
			
			//populate the combobox for genres
	        //ObservableList<String> genres = FXCollections.observableArrayList(Game.getAllGenresList());	        
			List<String> genres1 = Files.readAllLines(new File("src/main/resources/genres.txt").toPath(), Charset.defaultCharset());
			ObservableList<String> genres = FXCollections.observableArrayList(genres1);
			genreBrowseMenuPanel.setItems(genres);
			
			
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    }
    
    public void initData(User user) {
    	this.user = user;
    	userNameMenuPanel.setText(user.getNickname());
    }

}
