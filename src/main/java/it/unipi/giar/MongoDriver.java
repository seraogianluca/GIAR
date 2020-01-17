package it.unipi.giar;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDriver {
	private static MongoDriver driver = null;
	private MongoClient client;
	private MongoDatabase database;
	
	private MongoDriver() {
		client = MongoClients.create("mongodb://172.16.0.70,172.16.0.71,172.16.0.72/?"
				+ "replicaSet=res0&"
				+ "w=3&wtimeoutMS=5000&"
				+ "readPreference=nearest");
		database = client.getDatabase("giar");
	}
	
	public static MongoDriver getInstance() {
		if(driver == null)
			driver = new MongoDriver();
		
		return driver;
	}
	
	public MongoCollection<Document> getCollection(String collection) {
		if(driver == null)
			throw new RuntimeException("Connection doesn't exist.");
		else
			return driver.database.getCollection(collection);
	}
	
	public void close() {
		if(driver == null)
			throw new RuntimeException("Connection doesn't exist.");
		else
			driver.client.close();
	}
	
}
