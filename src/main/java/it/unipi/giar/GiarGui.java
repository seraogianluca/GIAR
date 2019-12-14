package it.unipi.giar;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import javafx.application.Application;
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
		 primaryStage.show();
		 primaryStage.setResizable(false);

	}

	public static void main(String[] args) {
		MongoClient mongoClient = MongoClients.create("mongodb://172.16.0.70:27017");
		MongoDatabase database = mongoClient.getDatabase("giar");
		MongoCollection<Document> collection = database.getCollection("games");
		MongoCursor<Document> cursor = collection.find(eq("slug", "fifa-20")).iterator();
		try {
			while(cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			
			}
		} finally {
			cursor.close();
		}
		
		mongoClient.close();
		//launch(args);
	}
}
