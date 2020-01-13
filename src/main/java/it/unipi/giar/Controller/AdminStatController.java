package it.unipi.giar.Controller;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import org.bson.Document;

import com.jfoenix.controls.JFXComboBox;
import com.mongodb.client.MongoIterable;

import it.unipi.giar.Data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class AdminStatController {


    @FXML
    private Text stats;

    @FXML
    private JFXComboBox<String> comboCountry;
    
    @FXML
    private BarChart<String, Number> chart;
    
    @FXML
    private CategoryAxis xAxe;

    @FXML
    private NumberAxis yAxe;
    
    ObservableList<String> games = FXCollections.observableArrayList();
    
    MongoIterable<Document> total;
    
    @FXML
    void statistics(ActionEvent event) {
   	
    	String country;
    	
    	games.clear();
    	
    	if(total!=null) {
       		chart.getData().clear();
    		chart.layout();
    		
    	}
	
    	country = comboCountry.getValue();
    	total = User.gameDistributionPerCountry(country);
        
    	XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName(country + " statistics");
        
        

        for(Document d: total) {
        	
        	dataSeries1.getData().add(new XYChart.Data(d.getString("_id"), d.getLong("count")));
        	games.add(d.getString("_id"));
        }
        
        xAxe.setCategories(games);
        chart.getData().addAll(dataSeries1);
           	
    }
    
    
    public void initialize() {
    	try {
    		 chart.setCategoryGap(100);
    		 chart.setTitle("Country Stats");
    		 xAxe.setLabel("Games");   
             yAxe.setLabel("Number of games owned");
             
     	    
     	  
            
			List<String> countries1 = Files.readAllLines(
					new File("src/main/resources/countries.txt").toPath(), 
					Charset.defaultCharset());
			ObservableList<String> countries = FXCollections.observableArrayList(countries1);
			comboCountry.setItems(countries);
    	} catch (Exception e) {
			e.printStackTrace();
    	}	
    }
	
}
