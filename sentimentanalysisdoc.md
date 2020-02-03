# Sentiment analysis

## Index

1. [Intoduction](#1-intoduction)
2. [Dataset description](#2-dataset-description)
3. [Data Preprocessing](#3-data-preprocessing)


## 1. Introduction

We want to implement a sentiment analysis feature in the GIAR application, considering the opinions about videogames mined from tweets of the players around the world.
Our aim is, for each videogame selected in the user's interface, to show an estimation about what players express in their tweets regarding a game. The user will see how many `positive` and `negative` opinions most recent tweets contain.


## 2 Dataset description
The dataset we use is composed by tweets we've retrieved using the python library `GetOldTweets3`. This process allowed us to make a search setting two parameters:
- language of the tweets
- one or more keywords

We decided to set search's parameters using as keyword the name of the game we are interested in, and the english language. In particular we made few searches in order to get tweets for some recent games, in this way we ensured to have a good amount of tweets. The tweets were initially composed by two attributes:
- date
- text

The first containing the timestamp of the tweet, the second the text field of the tweet.
For our analysis purpose, we ignore the date fields.

## 3 Data Preprocessing
### 3.1 Data Cleaning
To mine a sentiment in tweets, raw text must be cleaned. In this process we need to discard some common elements you find usually in tweets that are not interesting for our aim, like:
- hashtags
- mentions
- links
- emoji

Those elements are our noise. To do this we built some java methods using regoular expressions to remove hastags, mentions and links. To discard emoji we use a library for java `Emoji-Java`
that allows us to find and remove emoji from text. 
Mettere esempio di prima-dopo?

At this point we need to assign a label to each tweet in order to build our training set. In particular we manually assign to every tweet a class:
- positive
- negative
- none (means that there is no opinion in that tweet)

### 3.2 Data Integration
The data come from the same source, so we don't need to process this phase.

### 3.3 Data Reduction
The dataset we prepared cointains a lot of different instances of the 3 classes. Because we find only 333 instances of postive class (minority class),, in order to build a balanced dataset, we decided to reduce numerosity of the data applying a random sampling without replacement to the other 2 classes till we have the same amount of istances for each class.



