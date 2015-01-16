/**
 * 
 */
package org.wso2.uima.cpe.reader;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.wso2.uima.cpe.reader.data.Tweet;
import org.wso2.uima.main.RunCPE;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Farasath Ahamed
 *
 */
public class TwitterCollectionReader extends CollectionReader_ImplBase {

	private ArrayList<Tweet> tweets;	
	public static final String PARAM_USER_TO_FOLLOW = "user_to_follow";
	

	private String userToFollow;
	private int currentIndex;
	
	@Override
	public void initialize() throws ResourceInitializationException {
		//System.out.println("Reader Initialized");
		userToFollow = (String)getConfigParameterValue(PARAM_USER_TO_FOLLOW);
		currentIndex = 0;
		tweets = RunCPE.sharedList;
		//Logger.getLogger(TwitterCollectionReader.class).info("Tweets to process : "+tweets.size());
	}

	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		JCas jcas;
	    try {
	      jcas = aCAS.getJCas();
	    } catch (CASException e) {
	      throw new CollectionException(e);
	    }

	    // open input stream to file
	    String text = RunCPE.sharedList.get(currentIndex++).getText();
	      // put document in CAS
	    jcas.setDocumentText(text);

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Progress[] getProgress() {
		
		return new Progress[] { new ProgressImpl( currentIndex, RunCPE.sharedList.size(), Progress.ENTITIES) };
		
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		// Logger.getLogger(TwitterCollectionReader.class).info("I am working here :"+RunCPE.sharedList.size());
		return currentIndex < RunCPE.sharedList.size();
	}

	// retrieve Streamed Tweets from the shared List
	private ArrayList<Tweet> getTweets() {
		return RunCPE.sharedList;
	}

}
