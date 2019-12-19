package it.unipi.giar.Data;


import java.io.IOException;
import java.security.MessageDigest;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

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
	private static String nickname;
	private String email;
	private String password;
	private String country;
	private boolean deleted = false;
	private ArrayList<Document> wishlist;
	private ArrayList<Document> myGames;
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
			
			List<Document> items = new ArrayList<>();			
			items = (List<Document>)user.get("wishlist");
			if(items!=null) {
				wishlist = new ArrayList<>();
				
				for(Document d: items) {				
					addGameToList(d, "wishlist");			
				}
			}
			
			items = new ArrayList<>();			
			items = (List<Document>)user.get("mygames");
			if(items!=null) {
				myGames = new ArrayList<>();
				for(Document d: items) {				
					addGameToList(d, "myGames");			
				}
			}
		    			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public User(String type, String nickname, String email, String password, String country) {
		this.type = type;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.country = country;
	}
	public boolean checkDuplicate(Document d, String list) {
		if(list.equals("wishlist")){
			for(Document k: wishlist) {
				if(d.get("name").equals(k.get("name"))) {				
					return true;
				}
			}
		}
		else if(list.equals("myGames")) {
			for(Document k: myGames) {
				if(d.get("name").equals(k.get("name"))) {					
					return true;
				}
			}
		}
		return false;
		}
			
		
	
	public boolean checkListNull(String list) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			Document user = collection.find(eq("nickname", nickname)).first();
			List<Document> items = new ArrayList<>();  
			if(list.equals("myGames"))
				items = (List<Document>)user.get("mygames");
			else
				items = (List<Document>)user.get("wishlist");
			if(items==null)
				return true;
			}
		catch(Exception e) {e.printStackTrace();}
		return false;
	}
	public void addGameToList(Document d, String list) {

		if(list.equals("wishlist")) {
				if(checkListNull("wishlist"))
					wishlist = new ArrayList<>();
				wishlist.add(d);
				
			}
		
			else if(list.equals("myGames")) {
				if(checkListNull("myGames"))
					myGames = new ArrayList<>();
				myGames.add(d);
				
			}
		

	}
	public void removeGameFromList(Document d, String list) {
		if(list.equals("wishlist")) {					
			wishlist.remove(d);
			deleteRelation(d);
		}
		else if(list.equals("myGames")) {					
			myGames.remove(d);
		}
		
	}
		
		public void addToMongoList(Document d , String list) {
			
			
			MongoDriver driver = null;
			MongoCollection<Document> collection = null;
			Document docForList = new Document();
			try {
			driver = MongoDriver.getInstance();
			
			String name = d.getString("name");			
			Double rating = d.getDouble("rating");
			
			docForList.append("name", name);
			docForList.append("rating", rating);
			
			collection = driver.getCollection("users");
			if(list.equals("wishlist"))		
				collection.updateOne(eq("nickname", nickname),Updates.addToSet("wishlist",docForList));
			else
				collection.updateOne(eq("nickname", nickname),Updates.addToSet("mygames",docForList));
		
			}
		catch(Exception e) {e.printStackTrace();}
		}
	
		public void removeFromMongoList(Document d, String list) {
			MongoDriver driver = null;
			MongoCollection<Document> collection = null;
			
			try {
			driver = MongoDriver.getInstance();
		
			collection = driver.getCollection("users");
			
			if(list.equals("wishlist"))	{	
				
				Bson filter = Filters.eq("nickname", nickname);
				Bson delete = Updates.pull("wishlist", new Document("name", d.getString("name")));
				collection.updateOne(filter, delete);
				return;
				
			}
			else if (list.equals("myGames")) {
				Bson filter = Filters.eq("nickname", nickname);
				Bson delete = Updates.pull("mygames", new Document("name", d.getString("name")));
				collection.updateOne(filter, delete);
				return;
			}
				  
		
			}
		catch(Exception e) {e.printStackTrace();}
			
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
	
	public static ArrayList<User> searchUsers(String search) {
		ArrayList<User> listUsers = new ArrayList<User>();

		try {
			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("users");
			
			BasicDBObject query = new BasicDBObject();
			query.put("nickname", Pattern.compile(search, Pattern.CASE_INSENSITIVE));
			
			MongoCursor<Document> cursor = collection.find(query).limit(10).iterator();
			
			try {
				while (cursor.hasNext()) {
					Document document = cursor.next();
					listUsers.add(new User(document.getString("nickname")));
				}
			} finally {
				cursor.close();
			}
			
			return listUsers;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listUsers;
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
	
	public String inList(String name) {
		for(Document d: wishlist) {
			if(d.get("name").equals(name)) {
				return "wishlist";
			}
		}
		for(Document d: myGames) {
			if(d.get("name").equals(name)) {
				return "myGames";
			}
		}
		return "noList";
	}
	
	public boolean isInMyGames(Game game) {
		Document listDoc = new Document();
		listDoc.append("name", game.getName());
		listDoc.append("rating", game.getRating());
		if(myGames == null)
			return false;
		return myGames.contains(listDoc);
	}
	public boolean isInWishlist(Game game) {
		Document listDoc = new Document();
		listDoc.append("name", game.getName());
		listDoc.append("rating", game.getRating());
		if(wishlist == null)
			return false;
		return wishlist.contains(listDoc);
	}
	public double getGameRate(long gameid) {
		//TO DO
		//MATILDE,  this function goes inside the logged user and takes the rating of the user for the gameid game and return his rating
		double rating = 2.4; ///per prova
		return rating;
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
	
	public void createGame(final String name) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
	    try (Session session = nd.getDriver().session()) {
	    	
	        session.writeTransaction(
	        	new TransactionWork<Boolean>() {
	        		@Override
	        		public Boolean execute(Transaction tx) {
	        			return findGameNode(tx, name);
	        		}
	        	}
	        );
	    }
	}
	
	private static boolean findGameNode(Transaction tx, String name) {
		
       StatementResult result = tx.run( "MATCH (n:Game) WHERE n.name = $name RETURN n", parameters ("name", name) );
       
       if(result.hasNext()) {
    	   System.out.println("game exists!");
    	   tx.run("MATCH (p:Player) WHERE p.nickname = $nickname MATCH (g:Game) WHERE g.name = $gameName  CREATE (p)-[:WISHED]->(g)"
    			   ,parameters("nickname", nickname, "gameName", name));
    	   return true;
       }
       else {
    	   System.out.println("game NOT exists!"); 
    	   //create
    	   tx.run("CREATE (n:Game {name: $name})", parameters("name", name));
    	   
    	   tx.run("MATCH (p:Player) WHERE p.nickname = $nickname MATCH (g:Game) WHERE g.name = $gameName  CREATE (p)-[:WISHED]->(g)"
    			   ,parameters("nickname", nickname, "gameName", name));
    	   System.out.println("creato!");
    	   return false;
       }
   
	}
	
	public void deleteRelation(final Document d) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
	    try (Session session = nd.getDriver().session()) {
	    	
	        session.writeTransaction(
	        	new TransactionWork<Boolean>() {
	        		@Override
	        		public Boolean execute(Transaction tx) {
	        			return deleteWishRelation(tx, d);
	        		}
	        	}
	        );
	    }
	}
	
	private static boolean deleteWishRelation(Transaction tx, Document d) {
		
		
	    tx.run("MATCH (p: Player{nickname: $nickname})-[r:WISHED]->(g: Game{name:$name}) DELETE r", 
	    		parameters("nickname", nickname, "name", d.get("name")));
	    tx.run("MATCH (g:Game{name: $name}) WHERE NOT ()-[:WISHED]->(g) DELETE g",
	    		parameters("name", d.get("name"))); //delete the node if it has no relationships
	    return true;
	}
	
	private static int deletePersonNode(Transaction tx, String name) {
		tx.run("MATCH (n:Player {nickname: $nickname}) DETACH DELETE n", parameters("nickname", name));
		return 1;
	}
}
