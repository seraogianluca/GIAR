# Work-group activity report - Implementation

## Table of contents
1. [Introduction](#1-introduction)
2. [Architecture](#2-architecture)
3. [Main Classes](#3-main-classes)

## 1. Introduction
In this document are described the highlights of the implementation. Starting from a description of the main functionalities of the solution, describing the use cases and then discussing about the package and the main classes providing some example of code.

## 2. Architecture
The solution is implemented as a client-server application. The client side, that is a desktop application, includes the solution logic exchanging messages with the server side only for performing operation on the databases (i.e. CRUD operations). The server side contains the databases instances (a document database and a graph database). The document database used is MongoDB.

The MongoDB server instance is deployed on a cluster of three nodes as a replica set with one primary and two secondary nodes (two replicas). A write operation on the database, due to the availability requirements expressed in [design document](./Design.md), will return the control to the application after two acknowledgement of write operations (write concern with two writes). This means that, a primary and at least a secondary is updated. A read operation on the database is directed to the primary server and on the secondary server in case of primary fails. The replica set will elect a new primary server if the primary fails. 

The settings on the replica set made the system available and partition tolerant (AP on the CAP theorem). Indeed, if the primary server fails a new primary will be elected having an eventual consistency.

For information about the graph database see [GRAPH DATABASE IMPLEMENTATION].

## 3. Main Classes
The solution contains three packages:
- it.unipi.giar that contains the javaFX main class, the session class and the databases drivers.
- it.unipi.Data that contains the classes of the application objects such as User, Games, Platform, Genre and Developers.
- it.unipi.Controller that contains the javaFx controllers for the GUI.

The `GiarSession` class contains the logged user object. The object is used for various operations during an application session (i.e. save a game to the wishlist). So, the object is often needed in the application controllers, for this reason this class is written using the Singleton pattern.

The `MongoDriver` class contains the MongoClient instance. Since the application need only one MongoClient instance (MongoClient is a pool of connections to MongoDB), the class is written using the Singleton pattern.

For information about the `Neo4jDriver` see [GRAPH DATABASE IMPLEMENTATION].

The `User` class contains all the informations about a user such as the profile informations (nickname, email and country), the games lists (wishlist and mygames) and the list of the rating of the user. The class contains, along with the methods for managing the games lists and the rating list, static methods for checking user input during sign-in/sign-up phase (nickname, email, password), for registering a new user and for analytics about users (retrieving the users owned games per country).

The `Game` class contains all the information about a game such as name, release date, description, average rating, list of available platforms, list of genres and list of developers. The class contains, along with the methods for managing the platforms, genres and developers lists, static methods for searching games, browse games by one field of the lists (platform, genre, developers), and for analytics about games (Top games per platform).

The `Platform`, `Genre` and `Developer` classes contain informations about the related entities.

The classes belonging to the Controller package contains functions and object for the GUI.