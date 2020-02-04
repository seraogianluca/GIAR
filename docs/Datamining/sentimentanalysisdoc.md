# Sentiment analysis

## Index

1. [Intoduction](#1-intoduction)
2. [Dataset description](#2-dataset-description)
3. [Application](#3-application)
4. [Classification](#4-classification)


## 1. Introduction
GIAR (Games information and Ratings) is an application that collects information and ratings about videogames. A user can search a game by name using the search bar on the top of the homepage and then access to a page that contain informations about the game. 

The purpose of the project is to implement a sentiment analysis feature that gives to the user information about the players community opinions on the game. The opinions are obtained through tweets of the players around the world.

For each videogame selected by the user, the application will show the amount of `positive` and `negative` opinons the players express in their tweets regarding the game. The sentiment analysis is performed as a classification task on on-demand fetched tweets. Thus, the following steps are performed:
- Fetching tweets about the selected game through the Twitter API.
- Tweets preprocessing: Cleaning, Tokenization, Stop Words Removal, Stemming, BOW representation.
- Classification: positive, negative and none.

## 2 Dataset
The dataset used for train the model contains tweets about several games. First, we've retrieved tweets with the python library `GetOldTweets3`, searching for the game as keywords. The query is performed selecting english tweets. The result of the query contains several information, such as: 
- id 
- permalink
- username
- to
- text
- date
- retweets
- favorites
- mentions
- hashtags
- geo.

For the purpose of our analysis we stored only the text, that contains the effective tweet as displayed on Twitter.

Then, we labeled by hand the tweets in three classes: positive for tweets that contains good opinions about the game, negative for the tweets that contains bad opinions and none for the tweets that contains neutral opinion and/or spam. Since the dataset was strongly unbalanced, to obtain a balanced dataset we selected 333 instances per each minority class (positive and negative) from the labeled tweets and than we randomly picked 333 instances from the majority class (none) discarding the others. The dataset is so composed by 999 instances.

The instances were cleaned and stored together in an arff file. The cleaning process was performed with the python library `tweet-preprocessor`, removing useless informations such as: mentions, hashtags and links.

Here's a word cloud representation of the dataset created:
![Word-Cloud-1](./word_cloud_1.png)

We noticed that the words composing the title of the games we looked for were more frequent that the ones that express an opinion (good, bad, etc.). To avoid the biasing of the classification model on that games we decided to remove the word composing the games title. The resulting dataset is:

![Word-Cloud-2](./word_cloud_2.png)


## 4 Classification

Once the dataset is ready we perform several tests using different classifiers. We used a `filteredClassifier` to perform tests.
We used a multi-filter with the following filters:
- **attributeSelection**, with evaluator `InfoGainAttributeEval` and search `Ranker` with a threshold of -1000.
- **StringToWordVector**, setting as stemmer, stopwordshandler and tokenizer respectively english `SnowBallStemmer`, the NLTK's list of common english stop words, and `AlphabeticTokenizer`.

For each test we performed the `10-Fold-Cross-Validation`. A summarization of each test is below shown:

### 4.1 J48

**Correctly Classified Instances:** 672 (67.2673%)

**Incorrectly Classified Instances:** 327 (32.7327%)

**Kappa statistic:** 0.509 

**Mean absolute error:** 0.2498

**Root mean squared error:** 0.3922

**Relative absolute error:** 56.2143%

**Root relative squared error:** 83.1879%

**Total Number of Instances:** 999     

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
 -|0,625  |  0,135 |   0,698  |    0,625 |   0,659   |   0,504 |   0,804 |     0,670   |  positive
-|0,679  |  0,242  |  0,584   |   0,679 |   0,628   |   0,423 |   0,794 |     0,641  |   negative
  -|0,715  |  0,114  |  0,758   |   0,715   | 0,736  |    0,610 |   0,877   |  0,795  |   none
Weighted Avg. |   0,673 |   0,164   | 0,680  |    0,673  |  0,674   |   0,512 |   0,825    | 0,702     

**Confusion Matrix**

   positive|  negative|  none |  <-- classified as
   --|--|--|-------|
 208 | 96 | 29 |    positive
  60 | 226 | 47 |  negative
  30 | 65 | 238 |  none

### 4.2 Naive Bayes Multinominal Text

**Correctly Classified Instances:** 811 (81.1812%)

**Incorrectly Classified Instances:** 188 (18.8188%)

**Kappa statistic:** 0.7177

**Mean absolute error:** 0.1612

**Root mean squared error:** 0.2988

**Relative absolute error:** 36.2742%

**Root relative squared error:** 63.3739%

**Total Number of Instances:** 999     

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
 . | 0,874  |  0,176  |  0,713   |   0,874 |   0,785  |    0,670 |   0,927  |   0,847   |  positive
 . | 0,730  |  0,084   | 0,813  |    0,730  |  0,769     | 0,665  |  0,919  |   0,873   |  negative
. | 0,832 |   0,023  |  0,949   |   0,832  |  0,886   |   0,839  |  0,967  |   0,957  |   none
Weighted Avg.  |  0,812  |  0,094  |  0,825  |    0,812  |  0,814 |     0,724 |   0,938  |   0,892     

**Confusion Matrix**

   positive |  negative |  none |  <-- classified as
   --|--|--|-------|
291 | 33  | 9 |    positive
  84 | 243  | 6 |  negative
  33 | 23  | 277 |   none

### 4.3 3-Nearest-Neighbors

**Correctly Classified Instances:** 695 (69.5696%)

**Incorrectly Classified Instances:** 304 (30.4304%)

**Kappa statistic:** 0.5435

**Mean absolute error:** 0.2199

**Root mean squared error:** 0.3935

**Relative absolute error:** 49.4705%

**Root relative squared error:** 83.468%

**Total Number of Instances:** 999    

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
  .|0,742 |   0,194  |  0,657   |   0,742  |  0,697   |   0,533  |  0,849   |  0,713  |   positive
 . |0,456  |  0,042  |  0,844   |   0,456  |  0,593   |   0,508  |  0,823  |   0,716  |   negative
 . | 0,889 |   0,221 |   0,668   |   0,889  |  0,763  |    0,634  |  0,916   |  0,870   |  none
Weighted Avg. |   0,696  |  0,152  |  0,723  |    0,696  |  0,684   |   0,559  |  0,863   |  0,766     

**Confusion Matrix**

   positive |  negative |  none |  <-- classified as
   --|--|--|-------|
   247 | 20 | 66 | positive
 100 | 152 | 81 | negative
  29 |  8  | 296 | none

### 4.4 RandomForest

**Correctly Classified Instances:** 804 (80.4805%)

**Incorrectly Classified Instances:** 195 (19.5195%)

**Kappa statistic:** 0.7072

**Mean absolute error:** 0.1303

**Root mean squared error:** 0.3445

**Relative absolute error:** 29.3178%

**Root relative squared error:** 73.0853%

**Total Number of Instances:** 999  

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
  . |0,862  |  0,155  |  0,736   |   0,862  |  0,794  |    0,683  |  0,910  |   0,802   |  positive
 .| 0,712 |   0,066  |  0,843  |    0,712 |   0,772  |    0,677  |  0,897  |   0,839  |   negative
 . | 0,841  |  0,072  |  0,854   |   0,841   | 0,847    |  0,772   | 0,942  |   0,916  |   none
Weighted Avg. |   0,805  |  0,098 |   0,811  |    0,805  |  0,804   |   0,711  |  0,916   |  0,852 

**Confusion Matrix**

   positive |  negative |  none |  <-- classified as
   --|--|--|-------|
   287 | 27 | 19 | positive
  67 | 237  | 29 | negative
  36 | 17  | 280 | none

### 4.5 HoeffdingTree

**Correctly Classified Instances:** 429 (42.9429%)

**Incorrectly Classified Instances:** 570 (57.0571%)

**Kappa statistic:** 0.1441

**Mean absolute error:** 0.3801

**Root mean squared error:** 0.6163

**Relative absolute error:** 85.524%

**Root relative squared error:** 130.7381%

**Total Number of Instances:** 999  

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
. | 0,222 |    0,054  |  0,673  |    0,222  |  0,334   |   0,253  |  0,793  |   0,605 |    positive
 . | 0,069  |  0,005  |  0,885  |    0,069  |  0,128   |   0,191  |  0,649  |   0,506  |   negative
 . | 0,997  |  0,797 |   0,385   |   0,997  |   0,555  |    0,275  |   0,604 |    0,387 |    none
Weighted Avg.  |  0,429 |   0,285 |   0,647   |   0,429   | 0,339   |   0,240 |   0,682  |   0,500     

**Confusion Matrix**

   positive |  negative |  none |  <-- classified as
   --|--|--|-------|
   74  | 3  | 256 |positive
  35 |  23 | 275 |negative
   1 |  0 | 332 |none

### 4.6 Naive Bayes


**Correctly Classified Instances:** 753 (75.3754%)

**Incorrectly Classified Instances:** 246 (24.6246%)

**Kappa statistic:** 0.6306

**Mean absolute error:** 0.194 

**Root mean squared error:** 0.3501

**Relative absolute error:** 43.6583%

**Root relative squared error:** 74.2731%

**Total Number of Instances:** 999  

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
 . | 0,769 |   0,140 |   0,734 |     0,769 |   0,751   |   0,622  |  0,897 |    0,814    | positive
. |0,835  |  0,228   | 0,647  |    0,835 |   0,729    |  0,578  |  0,889   |  0,814  |   negative
. | 0,658 |   0,002 |   0,995  |    0,658 |   0,792   |   0,746 |   0,931  |   0,910  |   none
Weighted Avg. |   0,754  |  0,123 |   0,792  |    0,754  |  0,757 |     0,649  |  0,905  |   0,846     

**Confusion Matrix**

   positive |  negative |  none |  <-- classified as
   --|--|--|-------|
   256 |  76  | 1 |positive
  55 | 278 |  0 |negative
  38  | 76 | 219 |none

### 4.7 Support Vector Machine


**Correctly Classified Instances:** 770 (77.0771%)

**Incorrectly Classified Instances:** 229 (22.9229%)

**Kappa statistic:** 0.6562

**Mean absolute error:** 0.1528

**Root mean squared error:** 0.3909

**Relative absolute error:** 34.384%

**Root relative squared error:** 82.9261%

**Total Number of Instances:** 999   

**Detailed Accuracy By Class**

 .|TP Rate | FP Rate|  Precision | Recall  | F-Measure | MCC  |    ROC Area | PRC Area | Class
 ---|----|----|----|----|----|----|----|----|----|
. | 0,745 |   0,144  |  0,721  |    0,745  |  0,733   |   0,596  |  0,800  |   0,622  |   positive
. | 0,721  |  0,108 |   0,769   |   0,721 |   0,744    |  0,623  |  0,806  |   0,647   |  negative
  . |0,847 |   0,092 |   0,822   |   0,847 |   0,834  |    0,750  |  0,878  |   0,747  |   none
Weighted Avg.  |  0,771  |  0,115  |  0,771   |   0,771  |  0,770  |    0,656 |   0,828   |  0,672 

**Confusion Matrix**

positive |  negative |  none |  <-- classified as
   --|--|--|-------|  
   248 |  53 | 32 |positive
  64  | 240 |  29 |negative
  32 | 19 | 282 |none

### 4.8 Comparison of classification models

![Classifiers-Comparison](./classifiers.png)

## 3 Application

### 3.1 Data Preprocessing

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

At the end of this step, each text is cleaned from stop-words, and thus reduced to a sequence of relevant tokens.

We choose to use a list of most common stop-words we found on internet instead of use the proposed list in weka. We also decided to dont't remove badwords because they can be usefull for our analysis as they might be very expressive regarding an opionion.

(immagine di esempio prima-dopo?)

We operate now `stemming`: the process of reducing
each token to its stem or root form, by removing its suffix, in order to group words having closely
related semantics. At the end of this step each text
is represented as a sequence of stems. We installed the `snowballStemmer` and used it with "english" as language.

(immagine di esempio prima-dopo?)

(immagine schematica di tutti i passaggi ?)