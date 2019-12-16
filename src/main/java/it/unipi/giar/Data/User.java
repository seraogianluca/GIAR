package it.unipi.giar.Data;

import java.util.ArrayList;

enum USERTYPE {admin, player, pro};

public class User {
	private final long id;
	private final USERTYPE type;
	private final String nickname;
	private final String email;
	private final String password;
	private final String country;
	private ArrayList<Game> wishlist;
	private ArrayList<Game> myGames;
	private ArrayList<Rating> ratings;
	
}
