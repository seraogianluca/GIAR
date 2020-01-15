# Work-group activity report - Implementation document of the graph database

## Table of contents
1. [Introduction](#1-introduction)
2. [Architecture](#2-architecture)
3. [Main Classes](#3-main-classes)
4. [CRUD operations](#4-CRUDoperations)
5. [On-graph queries](#5-On-graph)

## 1. Introduction
In this document are described the highlights of the implementation. Starting from a description of the main functionalities of the solution and then discussing about the package and the main classes providing some example of code.

## 2. Architecture
The Graph Database used is Neo4j. The Neo4j server instance is deployed on a single server (standalone instance) with no replicas.
This database supports the social network in the GIAR application. The graph database is consistent with the document database.

## 3. Main Classes
The solution contains three packages:
- it.unipi.giar that contains the javaFX main class, the session class and the databases drivers.
- it.unipi.Data that contains the classes of the application objects such as User, Games, Platform, Genre and Developers.
- it.unipi.Controller that contains the javaFx controllers for the GUI.

The `Neo4jDriver` class contains the Driver instance. Since the application need only one Driver instance (Driver is a pool of connections to Neo4j), the class is written using the Singleton pattern.

The `User` class contains all the informations about a user such as the profile informations (nickname, email and country), the games lists (wishlist and mygames) and the list of the rating of the user. The class contains methods for support the social network, such as assign the pro badge or get the friends list. 

The `Game` class contains all the information about a game such as name, release date, description, average rating, list of available platforms, list of genres and list of developers. The class contains methods for supporting the social network, such as retrieving a friend wishlist.

## 4.CRUD operations
### 4.1 Create
When a new user sign-up to the application, a new node labeled as Player in the database is created. An auto-commit transaction is used to create the node.
```java
private static void createUserNode(String nick) {
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.run("CREATE (n:Player {nickname: $nickname, pro: false})", parameters("nickname", nick));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
``` 

### 4.2 Read
The method allows a user to get a list of all the players he follows. A transaction is used to read the list.
```java
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
``` 

## 4.3 Update
The method above set the PRO badge to the users that deserve it. The PRO badge is a boolean property of Players nodes. If a user is recognized as a PRO, the pro property is set to true. A transaction is used to update the value.
```java
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
```

## 4.4 Delete
The method is called everytime an admin delete a game. The system have to delete also the corresponding game node to ensure consistency between the two databases. The `MATCH (n:Game {name: $name}) DETACH DELETE n` query provides also to delete all the relations connected to the node.
```java
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
```

## 5.On-graph queries

N°| Domain specific | Graphic-centric
--|------------ | -------------
1|Which games has `player_nickname` in his Wishlist? |Which vertices with `wished` edges are incident to `player_nickname` vertex?
2|How important is `player_nickname` in the system? |What is the degree centrality of `player_nickname`?


### 5.1 Query 1
 The first query is used to retrive all the games that a friend of a user has in his wishlist. A user can see the list of the players he follows and query for the wished games of those players.

```java
 public static ArrayList<Game> getFriendWishlist(String friendNickname) {
		ArrayList<Game> games = new ArrayList<Game>();
		Neo4jDriver nd = Neo4jDriver.getInstance();
		try (Session session = nd.getDriver().session()) {
			session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					StatementResult result = tx.run("MATCH (p:Player)-[:WISHED]-(game) "
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
```

### 5.2 Query 2
The second query provides to check if a player is a PRO or not. The centrality measure is calculated as the degree of a node, considering the incoming edges.

The method below calculate the overall number of the players in the database.
```java	
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
```
The method below calculate the overall number of the following relations (edges) in the database.
```java
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
```
The method below counts the incoming relations to a player vertex.
```java
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
```
The method below uses the previous results to calculate a `threshold` as the ratio between the number of the total relations and the number of the players. If the degree of a node Player is greater or equal than the threshold, the Player is a PRO, and the setPro function is called in order to set the pro property to true.
```java
	public static boolean checkPro(String nickname) {
		int players = getPlayers();
		int follow = getFollow();	//total relations in the graphdb
		int following = getFollowing(nickname);	//degree of the node (incoming edges, followers)
		double threshold = (double)follow / (double)players;	//ratio

		if(following >= threshold) {	//if my followers > threshold -> PRO
			return setPro(nickname);
		}
		
		return false;
	}
```