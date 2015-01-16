package org.wso2.uima.cpe.reader;

import org.wso2.uima.cpe.reader.data.Tweet;
import twitter4j.*;

import java.util.ArrayList;

public class StatusHandler implements StatusListener{
	
	private ArrayList<Tweet> storage;

	public StatusHandler(ArrayList<Tweet> list) {
		storage = list;
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
	public void onStatus(Status status) {
		Logger.getLogger(StatusHandler.class).info("Tweet Recieved : "+status.getText());
		
		if(storage == null){
			throw new NullPointerException("Storage not set for status listener");
		}
		
		Tweet tweet = new Tweet(status.getId(), status.getCreatedAt(), status.getText());
		storage.add(tweet);	
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		Logger.getLogger(StatusHandler.class).error("Track Limitations Exceeded");

	}

}
