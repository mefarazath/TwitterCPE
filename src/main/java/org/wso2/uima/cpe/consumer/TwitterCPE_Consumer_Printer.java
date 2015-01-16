package org.wso2.uima.cpe.consumer;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.uima.types.LocationIdentification;

import java.util.Iterator;

public class TwitterCPE_Consumer_Printer extends CasConsumer_ImplBase{


	@Override
	public void processCas(CAS cas) throws ResourceProcessException {
		JCas jcas = null;

		try {
			jcas = cas.getJCas();
		} catch (CASException e) {
			e.printStackTrace();
		}

		Iterator iterator = jcas.getAnnotationIndex(LocationIdentification.type).iterator();

		System.out.println("\nTweet : "+jcas.getDocumentText());
		while(iterator.hasNext()){
			LocationIdentification tag = (LocationIdentification)iterator.next();
			System.out.println("\nAnnotation : "+tag.getCoveredText());
		}
	}
}