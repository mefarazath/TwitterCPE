package org.wso2.uima.demo;

import org.wso2.uima.cpe.reader.data.Tweet;
import twitter4j.Logger;

import java.util.ArrayList;

/**
 * Created by farazath on 1/17/15.
 */
public class DemoClient extends Thread {
    int currentIndex = 0;
    String user;
    ArrayList<Tweet> demoTweets ;
    ArrayList<Tweet> payLoadDestination;

    /***
     *
     * @param tweetStorage destination to which tweets should be stored
     *
     */
    public DemoClient(ArrayList<Tweet> tweetStorage, String user){
        this.payLoadDestination = tweetStorage;
        this.user = user;
        initDemoTweets();
    }

    private void initDemoTweets() {
        demoTweets = new TweetExtractor(this.user).getTweetList();
    }

    /***
     *
     * @param amount Number of tweets to fetched for this cycle
     */
   public void fetchNext(int amount){
       int current = currentIndex;
       int count = amount;

       while((current != demoTweets.size()) && (count>0)){

           Tweet t = demoTweets.get(current);
           payLoadDestination.add(t);
           Logger.getLogger(DemoClient.class).info((current + 1) + " Tweet added to payload dest.");
           Logger.getLogger(DemoClient.class).info(t.getText());
           current++;
           count--;

       }
       currentIndex = current;
   }

}
