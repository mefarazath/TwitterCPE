package org.wso2.uima.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.wso2.uima.demo.data.Tweet;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import javax.jms.*;

public class StatusHandler implements StatusListener{
	
	private MessageProducer producer;
	private Session session;
	private static Logger logger = Logger.getLogger(StatusHandler.class);


	/***
	 *
	 * @param JMSUrl String | URL of the JMS Broker to send the messages to
	 */
	public StatusHandler(String JMSUrl, String queueName) throws JMSException {
		// create the factory for ActiveMQ connection
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(JMSUrl);
		Connection connection = factory.createQueueConnection();
		connection.start();
		logger.info("ActiveMQ connection established for StatusHandler successfully");

		// Create a non-transactional session with automatic acknowledgement
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Create a reference to the queue test_queue in this session.
		Queue queue = session.createQueue(queueName);

		// Create a producer for queue
		producer = session.createProducer(queue);
		logger.info("ActiveMQ producer successfully created for TwitterStreamer");
	}


	/***
	 * Method to handle arrival of a status to the stream
	 * @param status Recieved Tweet as a Status Object
	 */
	@Override
	public void onStatus(Status status) {
		Logger.getLogger(StatusHandler.class).info("Tweet Recieved : "+status.getText());

		if(producer == null){
			throw new NullPointerException("ActiveMQ producer not set for StatusHandler");
		}


		Tweet tweet = new Tweet(status.getId(), status.getCreatedAt(), status.getText());

		// send the tweet to the queue
		try {
			TextMessage tweetMessage = session.createTextMessage(tweet.getText());
			producer.send(tweetMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void onException(Exception arg0) {
		Logger.getLogger(StatusHandler.class).error("Exception occured while streaming for tweets : "+arg0.getMessage());
		arg0.printStackTrace();
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		Logger.getLogger(StatusHandler.class).error("Track Limitations Exceeded");

	}

}
