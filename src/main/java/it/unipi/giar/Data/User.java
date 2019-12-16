package it.unipi.giar.Data;


import java.security.MessageDigest;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import it.unipi.giar.MongoDriver;

enum USERTYPE {admin, player, pro};

public class User {
//	private final long id;
//	private final USERTYPE type;
	private final String nickname;
//	private final String email;
	private final String password;
//	private final String country;
//	private ArrayList<Game> wishlist;
//	private ArrayList<Game> myGames;
//	private ArrayList<Rating> ratings;
	
	
	
	
	public User(String nickname, String password) {
		this.nickname = nickname;
		this.password = password;
	}
	
	
	public static boolean checkNickname(String nick) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			MongoCursor<Document> cursor = collection.find(eq("nickname", nick)).iterator();
			
			return(cursor.hasNext());
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	public static boolean checkPassword(String password) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		
		try {		
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
		    byte[] digest = md.digest();
		    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		    MongoCursor<Document> cursor = collection.find(eq("password", myHash)).iterator();
		    return(cursor.hasNext());
	}	catch(Exception e) {
		e.printStackTrace();
	}
		return false;
}
	public static boolean isAdmin(String nickName) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			MongoCursor<Document> cursor = collection.find(and(eq("nickname", nickName),eq("type", "admin"))).iterator();
			
			return(cursor.hasNext());
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
