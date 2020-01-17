# Work-group activity report - Implementation document

## Table of contents
1. [Introduction](#1-introduction)
2. [Architecture](#2-architecture)
3. [Main Classes](#3-main-classes)
4. [Indexes](#4-indexes)
5. [Aggregations](#5-aggregations)
6. [Analytics and statistics](#6-analytics-and-statistics)
7. [CRUD Operations](#7-CRUD-operations)

## 1. Introduction
In this document are described the highlights of the implementation. Starting from a description of the main functionalities of the solution and then discussing about the package and the main classes providing some example of code.

The application is implemented as client-server. The client side, that is a desktop application, includes the application logic and exchange messages with the server side only to perform operations on the databases (i.e. CRUD operations). The server side contains the databases instances (a document database and a graph database).

For information about the graph database see [graph database implementation document](./ImplementationGraph.md).

## 2. Architecture
The document database used is MongoDB, deployed as a replica set with three data bearing nodes (no arbiters). Let's consider a list of tipical use cases to choose the most suitable write and read options for the replica set:

**Player:**
Query | Read | Write
----- | ---- | -----
Sign-up | Once each nickname and email character pressed | One
Sign-in | Two | None
Search a game | One each character pressed | None
Get game informations | One | None
Rate a game | None | Ten
Browse a list | One | None
Add game to a list | None | Four
Remove game from a list | None | Two
Browse games | One each index entry | None
Top ten per platform | Pipeline execution | None

**Administrator:**
Query | Read | Write
----- | ---- | -----
Search a game | One each character pressed | None
Get game informations | One | None
Insert a game | None | One
Delete a game | None | One
Update a game | None | One
Owned game distribution per country | Pipeline execution | None

**N.B.**
Read and write volumes are calculated considering the worst case.

The application has less frequent and more simple write operations (at most one document created or updated). Contrarily, read operations are more frequent and performed on a huge number of documents. Furthermore the system has availability and data lost tolerance requirements (expressed in design document](./Design.md)).

Therefore the system is tuned as follow:
- Write concern: 3, Write timeout: 5s. Since write operations are fast and simple the system has a strict consistency (each write updates all the replicas), this let the system to accept read operations on all the nodes of the cluster (writes are accepted only by the primary node).
- Read preference: nearest. Read operations are performed on the node with the lowest network latency to have the fastest response.

## 3. Main Classes
The solution contains three packages:
- it.unipi.giar that contains the javaFX main class, the session class and the databases drivers.
- it.unipi.Data that contains the classes of the application objects such as User, Games, Platform, Genre and Developers.
- it.unipi.Controller that contains the javaFx controllers for the GUI.

The `GiarSession` class contains the logged user object. The object is used for various operations during an application session (i.e. save a game to the wishlist). So, the object is often needed in the application controllers, for this reason this class is written using the Singleton pattern.

The `MongoDriver` class contains the MongoClient instance. Since the application need only one MongoClient instance (MongoClient is a pool of connections to MongoDB), the class is written using the Singleton pattern.

For information about the `Neo4jDriver` see [graph database implementation document](./ImplementationGraph.md).

The `User` class contains all the information about a user such as the profile information (nickname, email and country), the games lists (wishlist and mygames) and the list of the rating of the user. The class contains, along with the methods for managing the games lists and the rating list, static methods for checking user input during sign-in/sign-up phase (nickname, email, password), for registering a new user and for analytics about users (retrieving the users owned games per country).

The `Game` class contains all the information about a game such as name, release date, description, average rating, list of available platforms, list of genres and list of developers. The class contains, along with the methods for managing the platforms, genres and developers lists, static methods for searching games, browse games by one field of the lists (platform, genre, developers), and for analytics about games (Top games per platform).

The `Platform`, `Genre` and `Developer` classes contain information about the related entities.

The classes belonging to the Controller package contains functions and object for the GUI.

## 4. Indexes

Indexes support the efficient execution of queries. MongoDB defines indexes at the collection level and supports them on any field, sub-field or set of fields of the documents. Indexes come with a performance cost but are more than worth the cost for frequent queries on large data sets.

Indexes are created on the most used fields during read operations, such as game name, platform, year of release and genre.

The Mongo shell commands to create the indexes are shown below.

In the `games` collection, four single field indexes were created apart from the default `_id` one:

- `name`: the index is created on the `name` field in order to increase the performances during the search operation. The search operation is performed on `OnKeyPress` event, so it occurs every time a letter that compose the game name is typed in the search bar. 

Mongo shell:
```shell
db.games.createIndex({ name: 1 })
```

- `platform`: the index is created on the `platforms.platform.name` field of the game. It is used every time a user performs a browse by platform. 

Mongo shell:
```shell
db.games.createIndex({ platforms.platform.name: 1 })
```

- `genres`: the index is created on the `genres.name` field of the game. It is used every time a user performs a browse by genre.

Mongo shell:
```shell
db.games.createIndex({ genres.name: 1 })
```

- `year`: the index is created on the `year` field of the game. It is used every time a user performs a browse by year of release.

Mongo shell:
```shell
db.games.createIndex( { year: 1 } )
```

### 4.1 Indexes Query Performance
To read about the performance of the system exploiting or not the indexes please refer to [Indexes Performance Study](./IndexesStudy.md)

## 5. Aggregations
Several aggregations are used in the application. These aggregations are useful to obtain a list of values used for supporting browse functions. Aggregations are used for having a list of `year`, `platform` and `genres` field values. 

In the following an example of aggregation for the `platforms` field:

First stage: Deconstructs an array field from the game documents to output a game document for each element. Each output document is the input document with the value of the `platforms` array field replaced by the element.
```
[{
    $unwind: {
        path: "$platforms",
    }
},
```
Second stage: Groups by platform name.
```
 {
    $group: {
        _id: "$platforms.platform.name",

    }
},
```
Third stage: Sorts for platform name value in alfabetical order.
```
 {
    $sort: {
        _id: 1
    }
}]
```

In the following the code with the Mongo java driver:

```java
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
```	

## 6. Analytics and statistics
In the following the two pipelines aggregations used to extract interesting information from data are described. In particular, the top ten games per platform gives information about the most rated games grouped by platform (i.e. PC, Xbox and PlayStation). Then, the distribution of games per country gives information about the ten most owned games in a selected country.

### 6.1 Top 10 games per platform
This pipeline is performed on the `games` collection by the player side of the application. It returns the list of the ten most voted games between the games with rating greater than three.

First stage: Selects games of a specific platform with a rate greater than three.
```
[{
    $match: {
        'platforms.platform.name': 'platform_name',
        rating: {
            $gt: 3
        }
    }
},
```

Second stage: Deconstructs an array field from the game documents to output a game document for each element. Each output document is the input document with the value of the `ratings` array field replaced by the element.

**N.B.**
The `ratings` field is an array of embedded document that contains a document for each possible rating value (from 1 to 5). Each document contains the rating value, and the number of times that the rating was given to the game.
```
 {
    $unwind: {
        path: '$ratings'
    }
},
``` 

Third stage: Groups games by `_id` and Projects useful fields for the next stages. In particular, the game name (`name`), the total number of votes expressed (`ratings_count`) and the average rating (`rating`).
```
{
    $group: {
        _id: '$_id',

        ratings_count: {
            $sum: '$ratings.count'
        },

        rating: {
            $first: '$rating'
        },

        name: {
            $first: '$name'
        }
    }
},
```

Forth stage: Sorts for total count value in decreasing order.
```
 {
    $sort: {
        ratings_count: -1
    }
}, 
```

Fifth stage: Selects the first 10 games.
```
{
    $limit: 10
},
```

Sixth stage: Sorts for average rating value in decreasing order. 
```
{
    $sort: {
        rating: -1
    }
}]
```

In the following the code with the Mongo java driver is shown:

The variable `value` is the name of the platform requested by the user on which the pipeline must be executed.
```java
	public static ArrayList<Document> TopPerPlatform(String value) {
		ArrayList<Document> listGames = new ArrayList<Document>();
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match(and(eq("platforms.platform.name", value), gt("rating", 3L))), unwind("$ratings"), group("$id", sum("ratings_count", "$ratings.count"), first("rating", "$rating"), first("name", "$name")), sort(descending("ratings_count")), limit(10), sort(descending("rating")))).iterator();
			
			try {
				while (cursor.hasNext()) {
					Document document = cursor.next();
					listGames.add(document);
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
```			

### 6.2 Distribution of games per country
This pipeline is performed on the `users` collection by the admin side of the application. It returns the list of the ten most added games in the myGames list of users coming from a specific country.

First stage: Selects all the users of a specific Country.
```
[{
    $match: {
        country: 'country_name'
    }
},
```

Second stage: Deconstructs an array field from the user documents to output a user document for each element. Each output document is the input document with the value of the `mygames` array field replaced by the element.

```
 {
    $unwind: {
        path: '$mygames'
    }
},
```

Third stage: Groups and counts users by `mygames.name`. 
```
 {
    $group: {
        _id: '$mygames.name',
        count: {
            $sum: 1
        }
    }
},
```
Fourth stage: Sorts for count in decreasing order.
```
 {
    $sort: {
        count: -1
    }
},
```

Fifth stage: Selects the first ten results games.
```
{
    $limit: 10
}]
```

In the following the code with the Mongo java driver:

```java
	public static MongoIterable<Document> gameDistributionPerCountry(String country) {
		
		MongoDriver md;
		MongoCollection<Document> collection;
		MongoIterable<Document> total;

		md = MongoDriver.getInstance();
		collection = md.getCollection("users");

		total = collection.aggregate( Arrays.asList(match(eq("country", country)), unwind("$mygames"), group("$mygames.name", sum("count", 1L)), sort(descending("count")), limit(10)));
		
		return total;	
	}
```	

## 7. CRUD Operations

### 7.1 Create
The method allows a user to register into the system. The function takes the parameters specified by the user.

```java
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

			md = MongoDriver.getInstance(); //Singleton
			collection = md.getCollection("users");
			collection.insertOne(user);
			
			createUserNode(registNick);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```	

### 7.2 Read
The method is called every time a user inserts a character into the SearchBar in the graphic interface. The `collection.find(regex(key, search, "i")).batchSize(1500).iterator()` function makes a query on the database in order to retrive games that contains the key in the name. The option `i` allows the case-insensitive matching. The results will appear in the table below the SearchBar.

```java
public static ArrayList<Game> searchGames(String key, String search, int searchAll) {
		ArrayList<Game> listGames = new ArrayList<Game>();
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> cursor;
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			if (searchAll > 0) {
				cursor = collection.find(regex(key, search, "i")).limit(10*(searchAll+1)).iterator();
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
```

### 7.3 Update
The method allows an admin to modify some fields of an existing game. The admin has to specify the fields to update. An updated Game object is passed to this method.

```java
public static void updateGame(Game game) {
		MongoDriver driver;
		MongoCollection<Document> collection;
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Document> platList;
		ArrayList<Document> devList;
		ArrayList<Document> genList;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String released = dateFormat.format(game.released);
		String[] dateString = released.split("-");
		String year = dateString[0];
		 
		for(Platform p: game.platforms) {
			names.add(p.getName());
		}
		
		platList = createPlatformList(names);
		names.clear();
		
		for(Genre g: game.genres) {
			names.add(g.getName());
		}
		
		genList = createGenresList(names);
		names.clear();
		
		for(Developer d: game.developers) {
			names.add(d.getName());
		}
		
		devList = createDevelopersList(names);
		names.clear();	
		
		driver = MongoDriver.getInstance();
		collection = driver.getCollection("games");
		
		
		collection.updateOne(eq("name", game.name), 
				Updates.combine(
						Updates.set("description_raw", game.description),
						Updates.set("released", released),
						Updates.set("platforms", platList),
						Updates.set("developers", devList),
						Updates.set("year", year),
						Updates.set("genres", genList)));	
    }
```

### 7.4 Delete
The method allows an admin to delete a game.
```java
 public void deleteGame(String gameName) {
		
		try {
			MongoDriver md;
			MongoCollection<Document> collection;
			
			md = MongoDriver.getInstance();
			collection = md.getCollection("games");
			collection.deleteOne(eq("name", gameName));
			
			deleteGameNode(gameName);	//delete from graph
			deleteFromLists(gameName);	//delete from users lists
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
```