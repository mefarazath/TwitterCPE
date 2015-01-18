package org.wso2.uima.demo;

import org.wso2.uima.cpe.reader.data.Tweet;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by farazath on 12/22/14.
 */
public class TweetExtractor {
    private static ArrayList<Tweet> tweetList;
    private static String user_to_follow;

    public TweetExtractor(String user_to_follow){
        this.user_to_follow = user_to_follow;
    }

    public static void main(String[] args) throws IOException {
        // Oauth keys -- should not be exposed plain in code --
    	String consumerKey = "IxEX6LoI3Hcp91JX6KHEVECKu";
        String consumerSecret = "vr4RRmrT7UvKQO703Z3K9U5MsFzc7G8N7M8IBLzWQn3BrCQGIE";
        String accessToken = "711930980-dThMw79BL0i33dOpfCZBqDYH8FWIeQYXdkKOsvfa";
        String accessTokenSecret = "kgVIchdQrkjKPZoReBMECSLoyPegEHm2Y8mi8BLqDQEtP";



        // build the authentication token
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitterApp = tf.getInstance();

        Paging paging;
        // twitter username of the user to get the timeline of

        tweetList = new ArrayList<Tweet>();
        long min_id = Long.MAX_VALUE - 1;

        int count;
        int index = 0;
        List<Long> check = new ArrayList<Long>();


        while (true) {
            try {

                count = tweetList.size();
                paging = new Paging(1, 50);

                if (count != 0)
                    paging.setMaxId(min_id - 1);


                List<Status> temp = twitterApp.getUserTimeline(user_to_follow, paging);
                // System.out.println("Tweets for the request : " + temp.size());
                // statuses.addAll(temp);
                // System.out.println("Tweet Count : " + count);


                for (Status s : temp) {
                    if (!check.contains(s.getId())) {
                        check.add(s.getId());
                        Tweet tweet = new Tweet(s.getId(),s.getCreatedAt(),s.getText());
                        tweetList.add(tweet);
                        Logger.getLogger(TweetExtractor.class).info(" " + (index++) + " " + tweet.toString());
                    } else {
                        throw new Exception("Duplicate Found");
                    }
                    if (s.getId() < min_id) {
                        min_id = s.getId();
                    }
                }

                if (temp.size() == 0) {
                    break;
                }

            } catch (TwitterException e) {
                e.printStackTrace();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String filename =  "road_lk.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));


        JSONArray array = new JSONArray();

        for (Tweet tweet : tweetList) {
           // writeTextFile(writer,tweet.getText());
            array.put(tweet);
        }

      //  writer.close();
        Logger.getLogger(TweetExtractor.class).info("Total Tweets Extracted: " + tweetList.size());
      //  writeToFile(array, "tweets-road.lk.json");

    }


    public ArrayList<Tweet> getTweetList(){
        try {
            main(new String[]{});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tweetList;
    }


    /**
     * Method to write extracted tweets as a JSON Array to a file
     *
     * @param statusesArray Json Array of tweets
     * @param fileName      Name of the file to save the tweets(should end with .json)
     */
    private static void writeToFile(JSONArray statusesArray, String fileName) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(statusesArray.toString(5));
            writer.close();
        } catch (IOException e) {
            Logger.getLogger(TweetExtractor.class).error("Error Writing to file: " + e.getMessage());
        } catch (JSONException e) {
            Logger.getLogger(TweetExtractor.class).error("Error while converting json array to string: " + e.getMessage());
        }

    }

    private static void writeTextFile(BufferedWriter writer, String data){

        try {
            writer.append(data+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
