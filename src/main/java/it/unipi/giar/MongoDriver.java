package it.unipi.giar;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDriver {

	MongoClient mongoClient;
	
	public void start() {
		
		mongoClient = MongoClients.create("mongodb://172.16.0.70:27017");
	}
	
	public void close() {
		mongoClient.close();
	}
	
}
