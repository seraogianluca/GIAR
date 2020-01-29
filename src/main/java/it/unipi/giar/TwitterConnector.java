package it.unipi.giar;

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

public class TwitterConnector {
	public static void searchTweets(String searchTerm) {
		try {
			Twitter twitter = TwitterFactory.getSingleton();

			Query query = new Query(searchTerm);
			query.setLang("en");
			query.setCount(50);
			QueryResult result = twitter.search(query);

			ArrayList<String> tweets = new ArrayList<String>();
			for (Status status : result.getTweets()) {
				if(!status.isRetweet()) {
					tweets.add(tweetCleaning(status));
				}
			}
			
			createDataset(tweets);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	private static String tweetCleaning(Status tweet) {
		String cleanedTweet = EmojiParser.removeAllEmojis(tweet.getText());
		HashtagEntity[] hashtags = tweet.getHashtagEntities();
		UserMentionEntity[] mentions = tweet.getUserMentionEntities();
		
		for(HashtagEntity h: hashtags) {
			cleanedTweet = cleanedTweet.replaceAll("#" + h.getText() + "\\b", " ");
		}
	
		for(UserMentionEntity m: mentions) {
			cleanedTweet = cleanedTweet.replaceAll("@" + m.getScreenName() + "\\b", " ");
		}
	
		cleanedTweet = cleanedTweet.replaceAll("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", " ");
		cleanedTweet = cleanedTweet.replaceAll("\\s+", " ");
		
		return cleanedTweet;
	}
	
	private static void createDataset(ArrayList<String> tweets) {
		Attribute text = new Attribute("text", true);
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("positive");
		labels.add("negative");
		labels.add("none");
		Attribute clas = new Attribute("class", labels);
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(text);
		attributes.add(clas);
		Instances dataset = new Instances("tweets", attributes, tweets.size());
		dataset.setClassIndex(dataset.numAttributes()-1);
		
		for(int i = 0; i < tweets.size(); i++) {
			double[] value = new double[dataset.numAttributes()];
			value[0] = dataset.attribute(0).addStringValue(tweets.get(i));
			Instance inst = new DenseInstance(1.0, value);
			dataset.add(inst);
			dataset.instance(i).setClassMissing();
		}
		
		try {
			FilteredClassifier classifier;
			classifier = (FilteredClassifier)SerializationHelper.read("/Users/gianluca/GitHub/GIAR_datamining/src/main/resources/classifier.model");
			
			for(int i = 0; i < tweets.size(); i++) {
				classifier.classifyInstance(dataset.instance(i));
				System.out.println(dataset.instance(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			ArffSaver saver = new ArffSaver();
//			saver.setInstances(dataset);
//			saver.setFile(new File("./tweets.arff"));
//			saver.writeBatch();
//		} catch(IOException io) {
//			io.printStackTrace();
//		}
		
	}
	
//	public static void loadModel() {
//		try {
//		
//			FilteredClassifier classifier;
//			classifier = (FilteredClassifier) SerializationHelper.read("C:\\Users\\bari9\\git\\GIAR_datamining\\src\\main\\resources\\classifier.model");
//			
//			
//		}catch(Exception e) {e.printStackTrace();}
//		
//	}
}
