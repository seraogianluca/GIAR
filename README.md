# GIAR
[![CodeFactor](https://www.codefactor.io/repository/github/seraogianluca/GIAR/badge/master)](https://www.codefactor.io/repository/github/seraogianluca/GIAR/overview/master)

# Data Mining and Machine Learning
A service implemented for the workgroup project of the Datamining course of the Artificial Intelligence and Data Engineering Master Degree at University of Pisa.

1) [Porject Documentation](/docs/Datamining/sentimentanalysisdoc.md)
2) [Training-set](/docs/Datamining/dataset/training_set.arff)
3) [Training-set data distribution](/docs/Datamining/dataset/Tweets_distribution.xlsx)
4) [Final Test datasets](/docs/Datamining/dataset/classified_data/csv)
5) [Test datasets confusion matrices](/docs/Datamining/dataset/classified_data/Confusion_matrices.xlsx)
6) [Weka test result buffers](/docs/Datamining/weka_tests)

## Credits
Application designed and developed by Barigliano Lorenzo, Serao Gianluca.

# Large-Scale and Multi-Structured Databases
A service for the workgroup tasks of the Large-Scale and Multi-Structured Databases course of the Artificial Intelligence and Data Engineering Master Degree at University of Pisa.

## Task 2

1) [Design](/docs/Design.md)
2) [Implementation](/docs/Implementation.md)
3) [Indexes Performance Study](/docs/IndexesStudy.md)
4) [Tests](/docs/Test.md)
5) [User Manual](/docs/UserManual.md)

## Task 3

1) [Design Graph](/docs/DesignGraph.md)
2) [Implementation Graph](/docs/ImplementationGraph.md)
3) [User Manual (Same of task 2)](/docs/UserManual.md)

## Credits

Application designed and developed by Barigliano Lorenzo, Gómez Marsha, Mazzini Matilde, Serao Gianluca.

# Application installation

## Databases configuration

**Neo4j**: Install the database in your computer and connect as localhost replacing `username` and `password` with yours:
````java 
driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("username", "password"));
````

**MongoDB**: Install the database in your computer and create the giar database with the users and the game collections as described in the [Design document](/docs/Design.md).

The connection string is set to localhost:
````java
client = MongoClients.create("mongodb://localhost:27017/");
````

## Twitter API configuration
To run the sentiment analysis a Twitter API token is required. To load the token create a `twitter4j.properties` file in the `resources` folder. The file must contain the following lines:

```
http.useSSL=true
oauth.consumerKey =       Twitter_API_Token
oauth.consumerSecret =    Twitter_API_Token
oauth.accessToken =       Twitter_API_Token
oauth.accessTokenSecret = Twitter_API_Token
````
