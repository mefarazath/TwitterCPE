package org.wso2.uima.cpe.ae;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.wso2.uima.types.HashTag;
import twitter4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class HashtagAnnotator extends JCasAnnotator_ImplBase {


		// create regular expression pattern for Hawthorne room number
		private Pattern hashTagPattern  =
				Pattern.compile("[#]+[a-zA-Z_]+");
		
		@Override
		public void process(JCas jcas) throws AnalysisEngineProcessException {
			
			// get document text from JCas
			String docText = jcas.getDocumentText();
			
			// search for Yorktown room numbers
			Matcher matcher = hashTagPattern.matcher(docText);
			int pos = 0;
			
			while(matcher.find(pos)){
					
					HashTag annotation = new HashTag(jcas);
					annotation.setBegin(matcher.start());
					annotation.setEnd(matcher.end());
					annotation.setHashtag(matcher.group());
					annotation.addToIndexes();
					Logger.getLogger(HashtagAnnotator.class).info("Annotation Detected : "+annotation.getCoveredText());
					pos = matcher.end();
					
			}
			
			
		}

}
