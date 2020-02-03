# Sentiment analysis

## Index

1. [Intoduction](#1-intoduction)
2. [Dataset description](#2-dataset-description)
3. [Data Preprocessing](#3-data-preprocessing)
4. [Classification](#4-classification)


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


The data come from the same source, so we don't need to process this phase.


The dataset we prepared cointains a lot of different instances of the 3 classes. Because we find only 333 instances of postive class (minority class), in order to build a balanced dataset, we decided to reduce numerosity of the data applying a random sampling without replacement to the other 2 classes till we have the same amount of istances for each class.

(immagine di weka con le 3 colonne uguali?)

We need now to transform our stream of characters into a stream of processing units, called `tokens`. During this step, after removing punctuation marks, non-text characters and special symbols, each text is represented as a set of words. We use the `AlphabeticTokenizer` as tokenizer in weka.

(immagine di esempio prima-dopo?)

At this point we need to handle noise removing `stop-words`. Those are words providing little or no useful information to the text analysis, for this reason they can be considered as noise. Stop-words are:
- Common stop-words include articles, conjunctions, prepositions, pronouns...
- Other stop-words are those typically appearing very often in sentences of the considered language (language-specific stop-words), or in the particular context
analyzed (domain-specific stop-words);
- At the end of this step, each text is cleaned from stop-words, and thus reduced to a sequence of relevant tokens.

We choose to use a list of most common stop-words we found on internet instead of use the proposed list in weka. We also decided to dont't remove badwords because they can be usefull for our analysis as they might be very expressive regarding an opionion.

(immagine di esempio prima-dopo?)

We operate now `stemming`: the process of reducing
each token to its stem or root form, by removing its suffix, in order to group words having closely
related semantics. At the end of this step each text
is represented as a sequence of stems. We installed the `snowballStemmer` and used it with "english" as language.

(immagine di esempio prima-dopo?)

(immagine schematica di tutti i passaggi ?)

## 4.Classification



