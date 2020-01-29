package it.unipi.giar;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GiarGui extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));     
		 primaryStage.setTitle("GIAR");
		 primaryStage.setScene(new Scene(root));
		 primaryStage.setResizable(false);
		 primaryStage.show();
	}

	public static void main(String[] args) {
		TwitterConnector.searchTweets("death stranding");
		launch(args);
	}
	
	@Override
	public void stop() {
		MongoDriver md = MongoDriver.getInstance();
		Neo4jDriver nd = Neo4jDriver.getInstance();
		md.close();
		nd.close();
		Platform.exit();
	}
}
