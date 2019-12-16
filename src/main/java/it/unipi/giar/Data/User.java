package it.unipi.giar.Data;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;

import it.unipi.giar.MongoDriver;

public class User {
	
	private String type;
	private String nickname;
	private String email;
	private String password;
	private String country;
	private ArrayList<Game> wishlist;
	private ArrayList<Game> myGames;
	private ArrayList<Rating> ratings;

	public User(String type, String nickname, String email, String password, String country) {
		this.type = type;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.country = country;
	}
	
	public static boolean checkEmail(String email) {
		MongoDriver md = MongoDriver.getInstance();
		MongoCollection<Document> collection = md.getCollection("users");
		Document user = collection.find(eq("email", email)).first();
		if(user == null)
			return true;
		return false;	
	}
	
	public void register() {
		Document user = new Document("nickname", nickname)
				.append("email", email)
				.append("password", password)
				.append("type", type)
				.append("country", country);
		
		MongoDriver md = MongoDriver.getInstance();
		MongoCollection<Document> collection = md.getCollection("users");
		collection.insertOne(user);
	}
}
