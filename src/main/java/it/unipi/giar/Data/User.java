package it.unipi.giar.Data;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import it.unipi.giar.GiarSession;
import it.unipi.giar.MongoDriver;
import it.unipi.giar.Neo4jDriver;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Sorts.descending;

import static org.neo4j.driver.v1.Values.parameters;

public class User {

	private String nickname;
	private String email;
	private String country;
	private ArrayList<Document> wishlist;
	private ArrayList<Document> myGames;
	private ArrayList<Document> ratings;
	
	public User(String type, String nickname, String email, String password, String country) {
		this.nickname = nickname;
		this.email = email;
		this.country = country;
		this.wishlist = new ArrayList<Document>();
		this.myGames = new ArrayList<Document>();
		this.ratings = new ArrayList<Document>();
	}
	
	@SuppressWarnings("unchecked")
	public User(String nickname) {
		try {
			MongoDriver driver = null;
			MongoCollection<Document> collection = null;
			Document user;
			ArrayList<Document> list;
			
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			
			user = collection.find(eq("nickname", nickname)).first();
			
			this.nickname = user.getString("nickname");
			this.email = user.getString("email");
			this.country = user.getString("country");
			this.wishlist = new ArrayList<>();
			this.myGames = new ArrayList<>();
			this.ratings = new ArrayList<>();
			
			list = new ArrayList<>();
			list = (ArrayList<Document>)user.get("wishlist");
						
			if(list != null) {				
				this.wishlist.addAll(list);
			}
			
			list = new ArrayList<>();
			list = (ArrayList<Document>)user.get("mygames");
			
			if(list != null) {
				this.myGames.addAll(list);
			}	
			
			list = new ArrayList<>();
			list = (ArrayList<Document>)user.get("ratings");

			if(list != null) {
				this.ratings.addAll(list);
			}	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
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
	
	public boolean isInMyGames(String gameName) {
		Document game = new Document();
		game.append("name", gameName);
		
		if (this.myGames.size() == 0) {
			return false;
		} else {
			return this.myGames.contains(game);
		}
	}
	
	public boolean isInWishlist(String gameName) {
		Document game = new Document();
		game.append("name", gameName);
		
		if (this.wishlist.size() == 0) {
			return false;
		} else {
			return this.wishlist.contains(game);
		}
	}
	
	public void addGameToList(String gameName, String list) {
		Document game = new Document();
		game.append("name", gameName);
		
		switch(list) {
			case "wishlist":
				wishlist.add(game);
				addToMongoList(game, "wishlist");
				addToGraph(gameName, this.nickname);				
				
				if(isInMyGames(gameName)) {
		    		removeGameFromList(gameName, "myGames");
		    	}
				
				break;
				
			case "myGames":
				myGames.add(game);
				addToMongoList(game, "myGames");
				
				if(isInWishlist(gameName)) {
		    		removeGameFromList(gameName, "wishlist");
		    	}
				
				break;
				
			default:
				break;
		}
	}
	
	private void addToMongoList(Document game, String list) {		
		try {
			MongoDriver driver;
			MongoCollection<Document> collection;
			MongoCollection<Document> collectionGames;
			
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			collectionGames = driver.getCollection("games");

			if(list.equals("wishlist")) {
				collection.updateOne(eq("nickname", this.nickname),Updates.addToSet("wishlist",game));
				collectionGames.updateOne(eq("name", game.getString("name")), Updates.inc("added_by_status.wishlist", 1));
			} else if(list.equals("myGames")) {
				collection.updateOne(eq("nickname", this.nickname),Updates.addToSet("mygames",game));	
				collectionGames.updateOne(eq("name", game.getString("name")), Updates.inc("added_by_status.mygames", 1));
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addToGraph(String gameName, String nickname) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							StatementResult result = tx.run( 
									"MATCH (n:Game) "
											+ "WHERE n.name = $name "
											+ "RETURN n"
											,parameters ("name", gameName));

							if(!result.hasNext()) {
								tx.run("CREATE (n:Game {name: $name})"
										,parameters("name", gameName));
							}
							
							tx.run("MATCH (p:Player) "
									+ "WHERE p.nickname = $nickname "
									+ "MATCH (g:Game) "
									+ "WHERE g.name = $gameName "
									+ "CREATE (p)-[:WISHED]->(g)"
									,parameters("nickname", nickname, "gameName", gameName));
							return true;
						};
					}
			);
		}
	}
	
	public void removeGameFromList(String gameName, String list) {
		Document game = new Document();
		game.append("name", gameName);
		
		switch(list) {
			case "wishlist":
				wishlist.remove(game);
				removeFromMongoList(game, "wishlist");
				deleteFromGraph(gameName);
				break;
				
			case "myGames":
				myGames.remove(game);
				removeFromMongoList(game, "myGames");
				break;
				
			default:
				break;
		}
	}

	private void removeFromMongoList(Document game, String list) {
		try {
			MongoDriver driver;
			MongoCollection<Document> collection;
			MongoCollection<Document> collectionGames;
			Bson filter;
			Bson delete;
			
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			collectionGames = driver.getCollection("games");
			filter = Filters.eq("nickname", this.nickname);

			if(list.equals("wishlist"))	{		
				delete = Updates.pull("wishlist", game);
				collection.updateOne(filter, delete);
				collectionGames.updateOne(eq("name", game.getString("name")), Updates.inc("added_by_status.wishlist", -1));
				
			} else if (list.equals("myGames")) {
				delete = Updates.pull("mygames", game);
				collection.updateOne(filter, delete);
				collectionGames.updateOne(eq("name", game.getString("name")), Updates.inc("added_by_status.mygames", -1));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteFromGraph(final String game) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							tx.run("MATCH (p: Player{nickname: $nickname})-[r:WISHED]->(g: Game{name:$name}) "
									+ "DELETE r"
									,parameters("nickname", nickname, "name", game));

							//Delete the node if it has no relationships.
							tx.run("MATCH (g:Game{name: $name}) "
									+ "WHERE NOT ()-[:WISHED]->(g) "
									+ "DELETE g"
									,parameters("name", game)); 
							return true;
						}
					}
			);
		}
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

	public static void register(String registNick, String registEmail, String registPwd, String registCnt) {
		try {
			MongoDriver md;
			MongoCollection<Document> collection;
			Document user;
			
			user = new Document("nickname", registNick)
					.append("email", registEmail)
					.append("password", registPwd)
					.append("type", "player")
					.append("country", registCnt);

			md = MongoDriver.getInstance();
			collection = md.getCollection("users");
			collection.insertOne(user);
			
			createUserNode(registNick);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private static void createUserNode(String nick) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.run("CREATE (n:Player {nickname: $nickname, pro: false})", parameters("nickname", nick));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void delete(String nick) {
		try {
			MongoDriver md;
			MongoCollection<Document> collection;
			
			GiarSession session = GiarSession.getInstance();
			session.setDeleted(true);
			
			md = MongoDriver.getInstance();
			collection = md.getCollection("users");
			collection.deleteOne(eq("nickname", nick));
			
			deleteUserNode(nick);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private static void deleteUserNode(String nick) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							tx.run("MATCH (n:Player {nickname: $nickname}) "
									+ "DETACH DELETE n"
									, parameters("nickname", nick));
							//Delete the node if it has no relationships.
							tx.run("MATCH (g:Game) "
									+ "WHERE NOT ()-[:WISHED]->(g) "
									+ "DELETE g"); 
							return true;
						}
					}
			);
		}
	}
	
	public static void followUser(String follower, String toFollow) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							tx.run("MATCH (p:Player) "
									+ "WHERE p.nickname = $nickname "
									+ "MATCH (n:Player) "
									+ "WHERE n.nickname = $toFollow "
									+ "CREATE (p)-[:FOLLOW]->(n)"
									,parameters("nickname", follower, "toFollow", toFollow));
							return true;
						};
					}
			);
		}
	}
	
	public static void unfollowUser(String follower, String toUnfollow) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try(Session session = nd.getDriver().session()) {
			session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							tx.run("MATCH (n:Player {nickname: $follower })-[r:FOLLOW]->(p:Player {nickname: $toUnfollow }) "
									+ "DELETE r"
									,parameters("follower", follower, "toUnfollow", toUnfollow));
							return true;
						}
					}
			);
		}
	}
	
	public static ArrayList<User> getFollowingList(String nickname) {
		ArrayList<User> following = new ArrayList<User>();		
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.readTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							StatementResult result = tx.run("MATCH (p: Player)-[:FOLLOW]->(n:Player) "
									+ "WHERE p.nickname = $nickname "
									+ "RETURN n.nickname AS nickname",parameters ("nickname", nickname));
							
							while(result.hasNext()) {
								Record record = result.next();
								following.add(new User(record.get("nickname").asString()));
							}
							
							return true;
						};
					}
			);
		}
		
		return following;
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
					if(!isAdmin(document.getString("nickname"))) {
						listUsers.add(new User(document.getString("nickname")));
					}
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
  
	public ArrayList<Game> getWishlist() {
		ArrayList<Game> result = new ArrayList<Game>();
		for(Document game : wishlist) {
    		String name = game.getString("name");
    		result.add(Game.findGame(name));	
    	}
		return result;
	}
	
	public ArrayList<Game> getMyGames() {
		
		ArrayList<Game> result = new ArrayList<Game>();
		for(Document game : myGames) {
    		String name = game.getString("name");
    		result.add(Game.findGame(name));	
    	}
		return result;
	}
	
	public boolean rated(String gameName) {
		Document game = new Document();
		game.append("name", gameName);
		
		if (this.ratings.size() == 0) {
			return false;
		} else {
			for (Document d: ratings) {
				if(d.getString("game").equals(gameName))
					return true;					
			}
		}
		return false;
	}
	
	public String getRate(String gameName) {
		for (Document d: ratings) {
			if(d.getString("game").equals(gameName))
				return d.getString("ratingid");
		}
		return null;
	}
	
	public void rate(String gameName, String value) {
		MongoDriver md;
		MongoCollection<Document> collection;
		Document rate;
		Document toDelete = null;
		Long modified = 0L;
		
		md = MongoDriver.getInstance();
		collection = md.getCollection("users");
		
		rate = new Document();
		rate.append("game", gameName);
		rate.append("ratingid", value);
		
		modified = collection.updateOne(and(eq("nickname", this.nickname), eq("ratings.game", gameName)), 
							Updates.set("ratings.$.ratingid", value)).getModifiedCount();
		
		if(modified == 0) {
			collection.updateOne(eq("nickname", this.nickname), Updates.addToSet("ratings", rate));
		} else {
			for(Document r : ratings) {
				if(r.getString("game").equals(gameName)) {
					toDelete = r;
					break;
				}
			}
			
			ratings.remove(toDelete);
		}
		
		ratings.add(rate);
	}
	
	public static MongoIterable<Document> gameDistributionPerCountry(String country) {
		
		MongoDriver md;
		MongoCollection<Document> collection;
		MongoIterable<Document> total;

		md = MongoDriver.getInstance();
		collection = md.getCollection("users");

		total = collection.aggregate( Arrays.asList(match(eq("country", country)), unwind("$mygames"), group("$mygames.name", sum("count", 1L)), sort(descending("count")), limit(10)));
		
		return total;	
	}
	
	public static boolean isPro(String nickname) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			return session.readTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							StatementResult result = tx.run("MATCH (p:Player) "
									+ "WHERE p.nickname = $nickname "
									+ "RETURN p.pro AS pro"
									,parameters("nickname", nickname));
							return result.next().get("pro").asBoolean();		
						};
					}
			);
		}
	}
	
	private static int getPlayers() {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			return session.readTransaction(
					new TransactionWork<Integer>() {
						@Override
						public Integer execute(Transaction tx) {
							StatementResult result = tx.run("MATCH (n:Player) "
									+ "RETURN COUNT(n) AS players");	
							return result.next().get("players").asInt();
						};
					}
			);
		}
	}
	
	private static int getFollow() {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			return session.readTransaction(
					new TransactionWork<Integer>() {
						@Override
						public Integer execute(Transaction tx) {
							StatementResult result = tx.run("MATCH ()-[r:FOLLOW]->() "
									+ "RETURN COUNT(r) AS follow");	
							return result.next().get("follow").asInt();
						};
					}
			);
		}
	}
	
	private static int getFollowing(String nickname) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			return session.readTransaction(
					new TransactionWork<Integer>() {
						@Override
						public Integer execute(Transaction tx) {
							StatementResult result = tx.run("MATCH ()-[r:FOLLOW]->(p:Player)"
									+ "WHERE p.nickname = $nickname"
									+ " RETURN count(r) AS follow"
									, parameters("nickname", nickname));	
							return result.next().get("follow").asInt();
						};
					}
			);
		}
	}
	
	private static boolean setPro(String nickname) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			return session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							StatementResult result = tx.run("MATCH (p:Player) "
									+ "WHERE p.nickname = $nickname "
									+ "SET p.pro = true " 
									+ "RETURN p.pro AS pro"
									, parameters("nickname", nickname));	
							return result.next().get("pro").asBoolean();
						};
					}
			);
		}
	}
	
	public static boolean checkPro(String nickname) {
		int players = getPlayers();
		int follow = getFollow();	//tot relations in the graphdb
		int following = getFollowing(nickname);	//followers of this user
		double threshold = (double)follow / (double)players;	//ratio

		if(following > threshold) {	//if my followers > threshold -> PRO
			return setPro(nickname);
		}
		
		return false;
	}
}
