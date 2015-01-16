package org.wso2.uima.cpe.reader;

import twitter4j.FilterQuery;
import twitter4j.Logger;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamer {
	
	private final String consumerKey = "IxEX6LoI3Hcp91JX6KHEVECKu";
    private final String consumerSecret = "vr4RRmrT7UvKQO703Z3K9U5MsFzc7G8N7M8IBLzWQn3BrCQGIE";
    private final String accessToken = "711930980-dThMw79BL0i33dOpfCZBqDYH8FWIeQYXdkKOsvfa";
    private final String accessTokenSecret = "kgVIchdQrkjKPZoReBMECSLoyPegEHm2Y8mi8BLqDQEtP";
    
    private ConfigurationBuilder cb;
    private TwitterStream twitterStream;
    private FilterQuery filter;
    
    private long[] users;

    private Logger logger;
    
    public TwitterStreamer(long[] users){
    	buildConfiguration();
    	TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        twitterStream = tf.getInstance();
        this.users = users;
        createFilter();

        logger = Logger.getLogger(TwitterStreamer.class);


    }
    
    // build the configuration using the API keys
    private void buildConfiguration(){
        cb = new ConfigurationBuilder();
        Logger.getLogger(TwitterStreamer.class).debug("Building Configuration");
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey(consumerKey)
    	  .setOAuthConsumerSecret(consumerSecret)
    	  .setOAuthAccessToken(accessToken)
    	  .setOAuthAccessTokenSecret(accessTokenSecret);	
    }
    
    
    // create a filter for the streamer
    private void createFilter(){
    	if(users == null)
    		throw new NullPointerException("User list to follow not set");
    	
    	filter = new FilterQuery();
    	filter.follow(users);
    }
    
    // start streaming for tweets
    public void startStream(StatusHandler handler){
    	twitterStream.addListener(handler);
    	if(filter == null){
    		throw new NullPointerException("Filter Not Set");
    	}
    	twitterStream.filter(filter);
    }
    

}
