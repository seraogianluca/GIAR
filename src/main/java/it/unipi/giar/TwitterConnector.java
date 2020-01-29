package it.unipi.giar;

import com.vdurmont.emoji.EmojiParser;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UserMentionEntity;

public class TwitterConnector {
	public static void searchTweets(String searchTerm) {
		try {
			Twitter twitter = TwitterFactory.getSingleton();

			Query query = new Query(searchTerm);
			query.setLang("en");
			query.setCount(50);
			QueryResult result = twitter.search(query);

			for (Status status : result.getTweets()) {
				if(!status.isRetweet()) {
					System.out.println("Original: \n" + status.getText() + "\n");					
					System.out.println("Cleaned:\n" + tweetCleaning(status) + "\n");
				}
			}
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
}
