package it.unipi.giar.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoCollection;

import it.unipi.giar.MongoDriver;

public class Game {
	private final long id;
	private String slug;
	private String name;
	private String nameOriginal;
	private String description;
	private int metacritic;
	private Date released;
	private String backgroundImage;
	private double rating;
	private ArrayList<Rating> ratings;
	private long added;
	private long addedWishlist;
	private long addedMyGames;
	private ArrayList<Platform> platforms;
	private ArrayList<Developer> developers;
	private ArrayList<Genre> genres;
	
	public static List<String> getAllPlatformsList() {
		//MATILDE, i need this function to populate the fields of the combobox for platforms
		//this function returns the list of the platforms existing in the database. distinct.
	
	  }
	
	public static List<String> getAllYearsList() {
		//MATILDE, i need this function to populate the fields of the combobox for years
		//this function returns the list of the years existing in the database. distinct.

	  }
	
	public static List<String> getAllGenresList() {
		//MATILDE, i need this function to populate the fields of the combobox for genres
		//this function returns the list of the genres existing in the database. distinct.
		
	    //MongoDriver md = MongoDriver.getInstance();
	    //MongoCollection<Document> collection = md.getCollection("games");
	  }
}
