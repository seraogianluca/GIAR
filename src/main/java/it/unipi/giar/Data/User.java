package it.unipi.giar.Data;


import java.io.IOException;
import java.security.MessageDigest;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;

import it.unipi.giar.MongoDriver;
import it.unipi.giar.Neo4jDriver;
import it.unipi.giar.Controller.SignInController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class User {

	private String type;
	private String nickname;
	private String email;
	private String password;
	private String country;
	private boolean deleted = false;
	private ArrayList<Game> wishlist;
	private ArrayList<Game> myGames;
	//private ArrayList<Rating> ratings;

	
	
	public User(String nickname) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			Document user = collection.find(eq("nickname", nickname)).first();
			this.nickname = user.getString("nickname");
			this.type = user.getString("type");
			this.email=user.getString("email");
			this.password=user.getString("password");
			this.country=user.getString("country");
			
//			List<Document> items = new ArrayList<>();			
//			items = (List<Document>)user.get("wishlist");
//			
//			for(Document d: items) {				
//				addGameToList(d, "wishlist");			
//			}
//			
//			items = new ArrayList<>();			
//			items = (List<Document>)user.get("myGames");
//			
//			for(Document d: items) {				
//				addGameToList(d, "myGames");			
//			}
//		    
//			System.out.println(wishlist);
//			System.out.println(myGames);
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addGameToList(Document d, String list) {
		if(list.equals("wishlist"))
			wishlist.add(new Game(d));
		else if(list.equals("myGames"))
			myGames.add(new Game(d));
		else
			System.out.println("write list name correctly");
	}
	
	public User(String type, String nickname, String email, String password, String country) {
		this.type = type;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.country = country;
	}
	
	public String getNickname() {
		return nickname;
	}
	


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public static boolean checkNickname(String nick) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			MongoCursor<Document> cursor = collection.find(eq("nickname", nick)).iterator();

			return(cursor.hasNext());

		} catch(Exception e) {
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean checkEmail(String email) {
		try {
			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("users");
			Document user = collection.find(eq("email", email)).first();
			if (user == null)
				return true;
		} catch (Exception e) {
			// TODO: handle exception
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

	public void register() {
		try {
			Document user = new Document("nickname", nickname)
					.append("email", email)
					.append("password", password)
					.append("type", type)
					.append("country", country);

			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("users");
			collection.insertOne(user);
			
			addPerson(nickname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double getGameRate(long gameid) {
		//TODO(MATILDE): this function goes inside the logged user and takes the rating of the user for the gameid game and return his rating  
		double rating = 2.4; ///per prova
		return rating;
	}
	
	public void delete() {
		try {
			//FIXME: 
			// * Check if works even if exists wishlist and mygames;
			// * Works only if connection string of MongoDriver is "mongodb://user:password@172.16.0.70:27017"
			
			this.deleted = true;
			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("users");
			collection.deleteOne(eq("nickname", nickname));
			removePerson(nickname);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private void addPerson(final String name) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
	    try (Session session = nd.getDriver().session()) {
	    	
	        session.writeTransaction(
	        	new TransactionWork<Integer>() {
	        		@Override
	        		public Integer execute(Transaction tx) {
	        			return createPersonNode(tx, name);
	        		}
	        	}
	        );
	    }
	}
	
	private void removePerson(final String name) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
	    try (Session session = nd.getDriver().session()) {
	    	
	        session.writeTransaction(
	        	new TransactionWork<Integer>() {
	        		@Override
	        		public Integer execute(Transaction tx) {
	        			return deletePersonNode(tx, name);
	        		}
	        	}
	        );
	    }
	}

	private static int createPersonNode(Transaction tx, String name) {
	    tx.run("CREATE (n:Player {nickname: $nickname, pro: $pro})", parameters("nickname", name, "pro", false));
	    return 1;
	}
	
	private static int deletePersonNode(Transaction tx, String name) {
		tx.run("MATCH (n:Player {nickname: $nickname}) DETACH DELETE n", parameters("nickname", name));
		return 1;
	}
}
