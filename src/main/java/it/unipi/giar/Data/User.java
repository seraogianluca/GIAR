package it.unipi.giar.Data;


import java.security.MessageDigest;
//import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;

import it.unipi.giar.MongoDriver;
import it.unipi.giar.Neo4jDriver;

public class User {

	private String type;
	private String nickname;
	private String email;
	private String password;
	private String country;
	//private ArrayList<Game> wishlist;
	//private ArrayList<Game> myGames;
	//private ArrayList<Rating> ratings;

	public User(String type, String nickname, String email, String password, String country) {
		this.type = type;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.country = country;
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
	
	public static boolean checkEmail(String email) {
		MongoDriver md = MongoDriver.getInstance();
		MongoCollection<Document> collection = md.getCollection("users");
		Document user = collection.find(eq("email", email)).first();
		if(user == null)
			return true;
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
		Document user = new Document("nickname", nickname)
				.append("email", email)
				.append("password", password)
				.append("type", type)
				.append("country", country);

		MongoDriver md = MongoDriver.getInstance();
		MongoCollection<Document> collection = md.getCollection("users");
		collection.insertOne(user);
		
		addPerson(nickname);
		
	}
	
	public void addPerson(final String name) {
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

	private static int createPersonNode(Transaction tx, String name) {
	    tx.run("CREATE (n:Player {nickname: $nickname, pro: $pro})", parameters("nickname", name, "pro", false));
	    return 1;
	}
}
