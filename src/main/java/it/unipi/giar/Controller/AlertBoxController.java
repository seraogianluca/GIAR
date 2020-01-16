package it.unipi.giar.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBoxController {


	public static void display(String msg) {
		Stage window= new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Alert");
		window.setMinWidth(300);
		window.setResizable(false);
		
		Text label = new Text();
		label.setText(msg);
		
		JFXButton closeButton = new JFXButton ("Close");
		closeButton.setOnAction(e-> window.close());
		closeButton.setStyle("-fx-background-color: YELLOW");
		closeButton.setFocusTraversable(false);
		
		
		VBox layout= new VBox(45);
		layout.getChildren().add(label);
		layout.getChildren().add(closeButton);
		layout.setAlignment(Pos.CENTER);
				
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
