package it.unipi.giar;

import java.io.File;
import java.util.ArrayList;
import com.vdurmont.emoji.EmojiParser;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UserMentionEntity;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;

public class TwitterConnector {
	public static void searchTweets(String searchTerm) {
		try {
			Twitter twitter;
			Query query;
			QueryResult result;
			ArrayList<String> tweets;
			
			twitter = TwitterFactory.getSingleton();
			tweets = new ArrayList<String>();
			query = new Query(searchTerm);
			query.setLang("en");
			query.setCount(1000);
			result = twitter.search(query);
			
			while (tweets.size() < 10) {
				for (Status status : result.getTweets()) {
					if (!status.isRetweet()) {
						tweets.add(tweetCleaning(status));
					}
				}
				
				//Query for the next pages
				query = result.nextQuery();
				result = twitter.search(query);
			}
			
			System.out.println(tweets.size());
			createDataset(tweets);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	private static String tweetCleaning(Status tweet) {
		String cleanedTweet;
		HashtagEntity[] hashtags;
		UserMentionEntity[] mentions;
		
		//Remove emojis
		cleanedTweet = EmojiParser.removeAllEmojis(tweet.getText());
		
		//Remove hashtags
		hashtags = tweet.getHashtagEntities();
	
		for (HashtagEntity h: hashtags) {
			cleanedTweet = cleanedTweet.replaceAll("#" + h.getText() + "\\b", " ");
		}
	
		//Remove mentions
		mentions = tweet.getUserMentionEntities();
		
		for (UserMentionEntity m: mentions) {
			cleanedTweet = cleanedTweet.replaceAll("@" + m.getScreenName() + "\\b", " ");
		}
	
		//Remove links
		cleanedTweet = cleanedTweet.replaceAll("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", " ");
		
		//Collapse multiple spaces
		cleanedTweet = cleanedTweet.replaceAll("\\s+", " ");
		
		return cleanedTweet;
	}
	
	private static void createDataset(ArrayList<String> tweets) {
		try {
			ArrayList<String> classLabels;
			Attribute text;
			Attribute clas;
			ArrayList<Attribute> attributes;
			Instances dataset;
			Instance inst;
			FilteredClassifier classifier;
						
			classLabels = new ArrayList<String>();
			classLabels.add("positive");
			classLabels.add("negative");
			classLabels.add("none");
			
			text = new Attribute("text", true);
			clas = new Attribute("class", classLabels);
			attributes = new ArrayList<Attribute>();
			attributes.add(text);
			attributes.add(clas);
			
			dataset = new Instances("tweets", attributes, tweets.size());
			dataset.setClassIndex(dataset.numAttributes()-1);
		
			for(int i = 0; i < tweets.size(); i++) {
				double[] value = new double[dataset.numAttributes()];
				value[0] = dataset.attribute(0).addStringValue(tweets.get(i));
				inst = new DenseInstance(1.0, value);
				dataset.add(inst);
				dataset.instance(i).setClassMissing();
			}
						
			classifier = (FilteredClassifier)SerializationHelper.read("./src/main/resources/classifier.model");
			
			for(int i = 0; i < tweets.size(); i++) {
				dataset.instance(i).setClassValue(classifier.classifyInstance(dataset.instance(i)));
				System.out.println(dataset.instance(i).toString());
			}
			
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataset);
			saver.setFile(new File("./tweets.arff"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
