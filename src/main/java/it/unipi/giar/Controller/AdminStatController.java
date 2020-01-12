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
    	
//    	 double maxBarWidth=40;
//         double minCategoryGap=100;
//        Scene scene = chart.getScene();
//        scene.widthProperty().addListener((obs,n,n1)->{
//             if(chart.getData().size()==0) return;
//
//             if(n!=null && (n1.doubleValue()>n.doubleValue())){
//                 double barWidth=0;
//                 do{
//                     double catSpace = xAxe.getCategorySpacing();
//                     double avilableBarSpace = catSpace - (chart.getCategoryGap() + chart.getBarGap());
//                     barWidth = (avilableBarSpace / chart.getData().size()) - chart.getBarGap();
//                     if (barWidth >maxBarWidth){
//                         avilableBarSpace=(maxBarWidth + chart.getBarGap())* chart.getData().size();
//                         chart.setCategoryGap(catSpace-avilableBarSpace-chart.getBarGap());
//                     }
//                 } while(barWidth>maxBarWidth);
//             }
//
//             if(n!=null && (n1.doubleValue()<n.doubleValue()) && chart.getCategoryGap()>minCategoryGap){
//                 double barWidth=0;
//                 do{
//                     double catSpace = xAxe.getCategorySpacing();
//                     double avilableBarSpace = catSpace - (minCategoryGap + chart.getBarGap());
//                     barWidth = Math.min(maxBarWidth, (avilableBarSpace / chart.getData().size()) - chart.getBarGap());
//                     avilableBarSpace=(barWidth + chart.getBarGap())* chart.getData().size();
//                     chart.setCategoryGap(catSpace-avilableBarSpace-chart.getBarGap());
//                 } while(barWidth < maxBarWidth && chart.getCategoryGap()>minCategoryGap);
//             }
//         });
//         
    	
    	   
    	
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
