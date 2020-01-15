


# Indexes updating
In order to maintain updated indexes when adding or updating a game, the following operations are done every time that a game is:
- Added
- Updated
- Deleted

## Modify an Index
To modify an existing index, you need to drop and recreate the index. 

### Remove All Indexes
We use the db.collection.dropIndexes() to remove all indexes except for the _id index from a collection.
the following command removes all indexes from the accounts collection:
db.collection.dropIndexes();

### Indexes creation

```java
db.collection.createIndex( { fieldname: 1 } )
```
our code:

```java
MongoDriver driver = MongoDriver.getInstance();
MongoCollection<Document> collection = driver.getCollection("games");	

//remove all
collection.dropIndexes();

//recreate all
collection.createIndex(Indexes.ascending("year"));
collection.createIndex(Indexes.ascending("genres.name"));
collection.createIndex(Indexes.ascending("platforms.platform.name"));
```

# Aggregations

list of the aggregation used in the overall app.

## Year
aggr to retrieve the list of distinct years in which games were released, in order to execute the browse per year(there a field fro every document)
```
[{
    $group: {
        _id: "$year"
    }
}, {
    $sort: {
        _id: 1
    }
}, {
    $skip: 1
}]
```

java driver:

```java
MongoCollection<Document> collection = driver.getCollection("games");
MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group("$year"), sort(ascending("_id")), skip(1))).iterator();
```
## Platforms
aggr to retrieve the list of distinct platforms names, in order to execute the browse per platform
```
[{
    $unwind: {
        path: "$platforms",
    }
}, {
    $group: {
        _id: "$platforms.platform.name",

    }
}, {
    $sort: {
        _id: 1
    }
}]
```
java driver:

```java
MongoCollection<Document> collection = driver.getCollection("games");
MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind("$platforms"), group("$platforms.platform.name"), sort(ascending("_id")))).iterator();
```

## Genres
aggr to retrieve the list of distinct genres names, in order to execute the browse per genres
```
[{
    $unwind: {
        path: "$genres"
    }
}, {
    $group: {
        _id: "$genres.name",
    }
}, {
    $sort: {
        _id: 1
    }
}]
```
java driver:

```java
MongoCollection<Document> collection = driver.getCollection("games");
MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind("$genres"), group("$genres.name"), sort(ascending("_id")))).iterator();
```
## Developers

aggr to retrieve the list of distinct developers names, in order to retrieve a list of the develpers to add a new game or update it
```
[{
    $unwind: {
        path: "$developers"
    }
}, {
    $group: {
        _id: "$developers.name",
    }
}, {
    $sort: {
        _id: 1
    }
}]
```
java driver:

```java
MongoCollection<Document> collection = driver.getCollection("games");
MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind("$developers"), group("$developers.name"), sort(ascending("_id")))).iterator();
```		

## Top per platform
aggr to make a statistic on the top games in the overal database
```
[
    {
    $match: {
        'platforms.platform.name': 'PC',
        rating: {
            $gt: 3
        }
    }
}, {
    $unwind: {
        path: '$ratings'
    }
}, {
    $group: {
        _id: '$id',

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
}, {
    $sort: {
        ratings_count: -1
    }
}, {
    $limit: 10
}, {
    $sort: {
        rating: -1
    }
}]
```
java driver:

```java
MongoCollection<Document> collection = driver.getCollection("games");
MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match(and(eq("platforms.platform.name", value), gt("rating", 3L))), unwind("$ratings"), group("$id", sum("ratings_count", "$ratings.count"), first("rating", "$rating"), first("name", "$name")), sort(descending("ratings_count")), limit(10), sort(descending("rating")))).iterator();
```			

## Country stat

aggr to make a statistic for the admin, the most added in the mygames list games per country
```
[{
    $match: {
        country: 'Afghanistan'
    }
}, {
    $unwind: {
        path: '$mygames'
    }
}, {
    $group: {
        _id: '$mygames.name',
        count: {
            $sum: 1
        }
    }
}, {
    $sort: {
        count: -1
    }
}, {
    $limit: 10
}]
```
java driver:

```java
MongoCollection<Document> collection = driver.getCollection("games");
MongoIterable<Document>  total = collection.aggregate( Arrays.asList(match(eq("country", country)), unwind("$mygames"), group("$mygames.name", sum("count", 1L)), sort(descending("count")), limit(10)));
```	