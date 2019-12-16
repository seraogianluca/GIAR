package it.unipi.giar;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDriver {
	private static MongoDriver mongoDb = null;
	private MongoClient mongoClient;
	
	private MongoDriver() {
		mongoClient = MongoClients.create("mongodb://172.16.0.70:27017");
	}
	
	public static MongoDriver getInstance() {
		if(mongoDb == null)
			mongoDb = new MongoDriver();
		
		return mongoDb;
	}
	
	public void close() {
		if(mongoDb == null)
			throw new RuntimeException("Connection doesn't exist.");
		else
			mongoDb.mongoClient.close();
	}
	
}
