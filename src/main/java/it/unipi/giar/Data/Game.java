package it.unipi.giar.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import it.unipi.giar.GiarSession;
import it.unipi.giar.MongoDriver;
import it.unipi.giar.Neo4jDriver;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Sorts.ascending;
import static org.neo4j.driver.v1.Values.parameters;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.skip;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.regex;

public class Game {
	private int id;
	private String slug;
	private String name;
	private String nameOriginal;
	private String description;
	private int metacritic;
	private Date released;
	private String backgroundImage;
	private double rating;
	private long added;
	private long addedWishlist;
	private long addedMyGames;
	private ArrayList<Platform> platforms;
	private ArrayList<Developer> developers;
	private ArrayList<Genre> genres;

	public Game(int id, String slug, String name, String nameOriginal, String description, int metacritic,
			Date released, String backgroundImage, double rating, long added, long addedWishlist, long addedMyGames,
			ArrayList<Platform> platforms, ArrayList<Developer> developers, ArrayList<Genre> genres) {
		this.id = id;
		this.slug = slug;
		this.name = name;
		this.nameOriginal = nameOriginal;
		this.description = description;
		this.metacritic = metacritic;
		this.released = released;
		this.backgroundImage = backgroundImage;
		this.rating = rating;
		this.added = added;
		this.addedWishlist = addedWishlist;
		this.addedMyGames = addedMyGames;
		this.platforms = platforms;
		this.developers = developers;
		this.genres = genres;
	}

	@SuppressWarnings("unchecked")
	public Game(Document document) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-mm-dd");

		this.id = (document.get("id") == null) ? 0 : document.getInteger("id");
		this.slug = (document.get("slug") == null) ? "" : document.getString("slug");
		this.name = (document.get("name") == null) ? "" : document.getString("name");
		this.nameOriginal = (document.get("name_original") == null) ? "" : document.getString("name_original");
		this.description = (document.get("description_raw") == null) ? "" : document.getString("description_raw");
		this.metacritic = (document.get("metacritic") == null) ? 0 : document.getInteger("metacritic");
		this.backgroundImage = (document.get("background_image") == null) ? "" : document.getString("background_image");
		this.rating = (document.get("rating") == null) ? 0 : document.getDouble("rating");
		// this.addedWishlist = (document.get("added_by_status") == null) ? 0 :
		// document.getLong("wishlist");
		// this.addedMyGames = (document.get("mygames") == null) ? 0 :
		// document.getLong("mygames");
		// this.added = (this.addedWishlist + this.addedMyGames);

		List<Document> platformDoc = new ArrayList<Document>();
		platformDoc = (List<Document>) document.get("platforms");

		if (platformDoc.isEmpty()) {
			this.platforms = new ArrayList<Platform>();
		} else {
			this.platforms = new ArrayList<Platform>();

			for (Document doc : platformDoc) {
				this.platforms.add(new Platform((Document) doc.get("platform")));
			}
		}

		List<Document> developDoc = new ArrayList<Document>();
		developDoc = (List<Document>) document.get("developers");

		if (developDoc.isEmpty()) {
			this.developers = new ArrayList<Developer>();
		} else {
			this.developers = new ArrayList<Developer>();

			for (Document doc : developDoc) {
				this.developers.add(new Developer(doc));
			}
		}

		List<Document> genresDoc = new ArrayList<Document>();
		genresDoc = (List<Document>) document.get("genres");

		if (genresDoc.isEmpty()) {
			this.genres = new ArrayList<Genre>();
		} else {
			this.genres = new ArrayList<Genre>();

			for (Document doc : genresDoc) {
				this.genres.add(new Genre(doc));
			}
		}

		try {
			this.released = (document.get("released") == null) ? new Date()
					: formatDate.parse(document.getString("released"));
		} catch (ParseException e) {
			this.released = new Date();
		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameOriginal(String nameOriginal) {
		this.nameOriginal = nameOriginal;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMetacritic(int metacritic) {
		this.metacritic = metacritic;
	}

	public void setReleased(Date released) {
		this.released = released;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setAdded(long added) {
		this.added = added;
	}

	public void setAddedWishlist(long addedWishlist) {
		this.addedWishlist = addedWishlist;
	}

	public void setAddedMyGames(long addedMyGames) {
		this.addedMyGames = addedMyGames;
	}

	public int getId() {
		return this.id;
	}

	public String getSlug() {
		return this.slug;
	}

	public String getName() {
		return this.name;
	}

	public String getNameOriginal() {
		return this.nameOriginal;
	}

	public String getDescription() {
		return this.description;
	}

	public int getMetacritic() {
		return this.metacritic;
	}

	public Date getReleased() {
		return this.released;
	}

	public String getBackgroundImage() {
		return this.backgroundImage;
	}

	public double getRating() {
		return this.rating;
	}

	public long getAdded() {
		return this.added;
	}

	public long getAddedWishlist() {
		return this.addedWishlist;
	}

	public long getAddedMyGames() {
		return this.addedMyGames;
	}

	public ArrayList<Platform> getPlatforms() {
		return this.platforms;
	}

	public ArrayList<Developer> getDevelopers() {
		return this.developers;
	}

	public ArrayList<Genre> getGenres() {
		return this.genres;
	}

	public static List<String> getAllPlatform() {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.aggregate(
					Arrays.asList(unwind("$platforms"), group("$platforms.platform.name"), sort(ascending("_id"))))
					.iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public static List<String> getAllYears() {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection
					.aggregate(Arrays.asList(group("$year"), sort(ascending("_id")), skip(1))).iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public static List<String> getAllGenres() {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection
					.aggregate(Arrays.asList(unwind("$genres"), group("$genres.name"), sort(ascending("_id"))))
					.iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public static ArrayList<Game> browseGamesPerPlatform(String value, Boolean searchAll) {
		return searchGames("platforms.platform.name", value, searchAll);
	}

	public static ArrayList<Game> browseGamesPerGenre(String value, Boolean searchAll) {
		return searchGames("genres.name", value, searchAll);
	}

	public static ArrayList<Game> browseGamesPerYear(String value, Boolean searchAll) {
		return searchGames("year", value, searchAll);
	}

	public static ArrayList<Game> searchGames(String search, Boolean searchAll) {
		return searchGames("name", search, searchAll);
	}

	// Flag SearchAll. True return all the results without limit
	public static ArrayList<Game> searchGames(String key, String search, Boolean searchAll) {
		ArrayList<Game> listGames = new ArrayList<Game>();
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> cursor;
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			if (searchAll) {
				cursor = collection.find(regex(key, search, "i")).batchSize(1500).iterator();
			} else {
				cursor = collection.find(regex(key, search, "i")).limit(10).batchSize(10).iterator();
			}

			try {
				while (cursor.hasNext()) {
					Document document = cursor.next();
					listGames.add(new Game(document));
				}
			} finally {
				cursor.close();
			}

			return listGames;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Game findGame(String name) {
		try {
			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("games");
			Document game = collection.find(eq("name", name)).first();

			return new Game(game);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<Game> getFriendWishlist(String friendNickname) {
		ArrayList<Game> games = new ArrayList<Game>();
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					StatementResult result = tx.run("MATCH ()-[:FOLLOW]-(p:Player)-[:WISHED]-(game) "
							+ "WHERE p.nickname = $friend " + "RETURN DISTINCT game.name AS game",
							parameters("friend", friendNickname));

					while (result.hasNext()) {
						Record record = result.next();
						Game game = Game.findGame(record.get("game").asString());
						if (game != null) {
							games.add(game);
						}
					}

					return true;
				};
			});
		}

		return games;
	}

	private int getTotalRating() {
		MongoDriver md;
		MongoCollection<Document> collection;
		Document total;

		md = MongoDriver.getInstance();
		collection = md.getCollection("games");

		total = collection.aggregate(Arrays.asList(match(eq("name", this.name)), unwind("$ratings"),
				group("$_id", sum("count", "$ratings.count")))).first();

		return total.getInteger("count");
	}

	@SuppressWarnings("unchecked")
	private void updatePercentage(String ratingid) {
		MongoDriver md;
		MongoCollection<Document> collection;
		ArrayList<Document> ratings;
		Document game;
		int ratingCount;
		int totalRatingCount;

		md = MongoDriver.getInstance();
		collection = md.getCollection("games");

		game = collection.find(eq("name", this.name)).first();

		ratings = (ArrayList<Document>) game.get("ratings");

		for (Document r : ratings) {
			ratingCount = r.getInteger("count");
			totalRatingCount = getTotalRating();

			collection.updateOne(and(eq("name", this.name), eq("ratings.title", ratingid)),
					Updates.set("ratings.$.percent", (ratingCount / totalRatingCount) * 100));
		}
	}

	@SuppressWarnings("unchecked")
	private void calculateRating() {
		MongoDriver md;
		MongoCollection<Document> collection;
		Document game;
		ArrayList<Document> ratings;
		int num = 0;
		int den = 0;

		md = MongoDriver.getInstance();
		collection = md.getCollection("games");
		game = collection.find(eq("name", this.name)).first();

		ratings = (ArrayList<Document>) game.get("ratings");

		for (Document r : ratings) {
			num += (Integer.parseInt(r.getString("title")) * r.getInteger("count"));
			den += r.getInteger("count");
		}

		this.rating = num / den;

		collection.updateOne(eq("name", this.name), Updates.set("rating", this.rating));
	}

	public void rate(String newRate, String oldRate) {
		MongoDriver md;
		MongoCollection<Document> collection;
		Document rate;
		Long modified = 0L;

		md = MongoDriver.getInstance();
		collection = md.getCollection("games");

		if (oldRate != null) {
			collection.updateOne(and(eq("name", this.name), eq("ratings.title", oldRate)),
					Updates.inc("ratings.$.count", -1));
		}

		modified = collection
				.updateOne(and(eq("name", this.name), eq("ratings.title", newRate)), Updates.inc("ratings.$.count", 1))
				.getModifiedCount();

		if (modified == 0) {
			rate = new Document();
			rate.append("id", Integer.parseInt(newRate));
			rate.append("title", newRate);
			rate.append("count", 1);
			rate.append("percent", 1);

			collection.updateOne(eq("name", this.name), Updates.addToSet("ratings", rate));
		}

		updatePercentage(newRate);
		calculateRating();
	}
	
	
	public void deleteGame(String gameName) {
		
		try {
			MongoDriver md;
			MongoCollection<Document> collection;
			
			GiarSession session = GiarSession.getInstance();
			session.setDeleted(true);
			
			md = MongoDriver.getInstance();
			collection = md.getCollection("games");
			collection.deleteOne(eq("name", gameName));
			
			deleteGameNode(gameName);	//delete from graph
			deleteFromLists(gameName);	//delete from users lists
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFromLists(String gameName){
		try {
			Document game = new Document();
			game.append("name", gameName);
			
			MongoDriver driver;
			MongoCollection<Document> collection;
			Bson filter;
			Bson filter1;
			Bson delete;
			Bson delete1;
			
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("users");
			
			filter = Filters.eq("wishlist.name", gameName);
			filter1 = Filters.eq("mygames.name", gameName);
			
			delete = Updates.pull("wishlist", game);
			delete1 = Updates.pull("mygames", game);
			
			collection.updateMany(filter, delete);
			collection.updateMany(filter1, delete1);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void deleteGameNode(String gameName){
		
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(
					new TransactionWork<Boolean>() {
						@Override
						public Boolean execute(Transaction tx) {
							tx.run("MATCH (n:Game {name: $name}) "
									+ "DETACH DELETE n"
									, parameters("name", gameName));
							return true;
						}
					}
			);
		}
	}

  
	public static List<String> getAllDevelopers() {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();		
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind("$developers"), group("$developers.name"), sort(ascending("_id")))).iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return items;	
	}
	
	private static ArrayList<Document> createPlatformList(ArrayList<String> platforms) {
		ArrayList<Document> platformDoc = new ArrayList<Document>();
		
		for(String name : platforms) {
			Document plat = new Document();
			plat.append("platform", new Document().append("name", name));
			platformDoc.add(plat);
		}
		
		return platformDoc;
	}
	
	private static ArrayList<Document> createGenresList(ArrayList<String> genres) {
		ArrayList<Document> genreDoc = new ArrayList<Document>();
		
		for(String name : genres) {
			genreDoc.add(new Document().append("name", name));
		}
		
		return genreDoc;
	}
	
	private static ArrayList<Document> createDevelopersList(ArrayList<String> developers) {
		ArrayList<Document> developerDoc = new ArrayList<Document>();
		
		for(String name : developers) {
			developerDoc.add(new Document().append("name", name));
		}
		
		return developerDoc;
	}
	
	public static void insertGame(String name, String date, String description, 
			ArrayList<String> platforms, ArrayList<String> genres, ArrayList<String> developers) {
		MongoDriver md;
		MongoCollection<Document> collection;
		ArrayList<Document> platformList;
		ArrayList<Document> genresList;
		ArrayList<Document> developersList;
		Double rating = 0.0;
		Document game;
		
		platformList = createPlatformList(platforms);
		genresList = createGenresList(genres);
		developersList = createDevelopersList(developers);
		
		game = new Document();
		game.append("name", name);
		game.append("released", date);
		game.append("description", description);
		game.append("rating", rating);
		game.append("platforms", platformList);
		game.append("genres", genresList);
		game.append("developers", developersList);
		
		md = MongoDriver.getInstance();
		collection = md.getCollection("games");
		collection.insertOne(game);	
	}


}