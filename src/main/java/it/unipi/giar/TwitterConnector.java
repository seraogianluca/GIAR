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
			query.setCount(10);
			QueryResult result = twitter.search(query);

			for (Status status : result.getTweets()) {
				if(!status.isRetweet()) {
					System.out.println("Original: \n" + status.getText() + "\n");
					String tweet = EmojiParser.removeAllEmojis(status.getText()).replace("RT", "");
					HashtagEntity[] hashtags = status.getHashtagEntities();
					UserMentionEntity[] mentions = status.getUserMentionEntities();
					
					for(HashtagEntity h: hashtags) {
						tweet = tweet.replace(h.getText(), "");
					
					}
				
					for(UserMentionEntity m: mentions) {
						tweet = tweet.replace(m.getScreenName(), "");
					}
				
					tweet = tweet.replaceAll("@", "");
					tweet = tweet.replaceAll("#", "");
					tweet = tweet.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\\b", "");
					tweet = tweet.replaceAll("\\s+", " ");
				
					System.out.println("Cleaned:\n" + tweet + "\n");
				}
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}
