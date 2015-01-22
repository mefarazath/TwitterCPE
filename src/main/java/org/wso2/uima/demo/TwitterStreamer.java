package org.wso2.uima.demo;

import org.apache.log4j.Logger;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.jms.JMSException;

//import java.util.logging.Logger;

//import twitter4j.Logger;

public class TwitterStreamer {
	
	// API keys for the TwitterStreaming API
    private final String consumerKey = "IxEX6LoI3Hcp91JX6KHEVECKu";
    private final String consumerSecret = "vr4RRmrT7UvKQO703Z3K9U5MsFzc7G8N7M8IBLzWQn3BrCQGIE";
    private final String accessToken = "711930980-dThMw79BL0i33dOpfCZBqDYH8FWIeQYXdkKOsvfa";
    private final String accessTokenSecret = "kgVIchdQrkjKPZoReBMECSLoyPegEHm2Y8mi8BLqDQEtP";

    // Configuration parameters of the ActiveMQ Message Broker
    private String JMSUrl;

    private ConfigurationBuilder cb;
    private TwitterStream twitterStream;
    private FilterQuery filter;

    private static long[] usersToFilter; // userIDs of the users to filter when the streaming tweets
    private static Logger logger = Logger.getLogger(TwitterStreamer.class);


    public static void main(String[] args) throws JMSException {
        usersToFilter = new long[] { 711930980, 808888003, 4366881, 2984541727l };
        String JMSUrl = "tcp://localhost:61616";
        String queueName = "TweetFeed";

        // create a streamer object
        TwitterStreamer streamer = new TwitterStreamer(usersToFilter, JMSUrl);

        if(JMSUrl == null)
            throw new NullPointerException("ActiveMQ URL not set");

        // create the status handler of the stream
        StatusHandler statusHandler = new StatusHandler(JMSUrl, queueName);

        // start streaming for tweets
        streamer.startStream(statusHandler);
    }

    /***
     *
     * @param users long[] userIDs of the users to filer the stream for
     * @param JMSUrl String url of the ActiveMQ Server
     */
    public TwitterStreamer(long[] users, String JMSUrl){
        this.JMSUrl = JMSUrl;
        buildConfiguration();
    	TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        twitterStream = tf.getInstance();
        createFilter();

    }
    

    /***
     *  Method to set up the API keys for the configuration builder
     */
    private void buildConfiguration(){
        cb = new ConfigurationBuilder();
        Logger.getLogger(TwitterStreamer.class).debug("Building Configuration");
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey(consumerKey)
    	  .setOAuthConsumerSecret(consumerSecret)
    	  .setOAuthAccessToken(accessToken)
    	  .setOAuthAccessTokenSecret(accessTokenSecret);	
    }


    /***
     *  Method to Set up the filter for the Streaming API
     */
    private void createFilter(){
    	if(usersToFilter == null)
    		throw new NullPointerException("User list to follow not set");
    	
    	filter = new FilterQuery();
    	filter.follow(usersToFilter);

    }

    /***
     * Method to start the streaming of Tweets
     * @param handler
     */
    public void startStream(StatusHandler handler){
        twitterStream.addListener(handler);
    	if(filter == null){
    		throw new NullPointerException("Filter Not Set");
    	}
    	twitterStream.filter(filter);
    }
    

}
