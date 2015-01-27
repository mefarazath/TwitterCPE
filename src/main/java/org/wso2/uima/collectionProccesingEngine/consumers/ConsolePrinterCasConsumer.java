/*
 *
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * /
 */

package org.wso2.uima.collectionProccesingEngine.consumers;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.uima.types.LocationIdentification;

import java.util.Iterator;

public class ConsolePrinterCasConsumer extends CasConsumer_ImplBase{

	// TODO remove this class
	@Override
	public void processCas(CAS cas) throws ResourceProcessException {
		JCas jcas = null;

		try {
			jcas = cas.getJCas();
		} catch (CASException e) {
			e.printStackTrace();
			throw new NullPointerException();
		}
		
		Iterator iterator = jcas.getAnnotationIndex(
					LocationIdentification.type).iterator();
			System.out.println("\nTweet : " + jcas.getDocumentText());
			while (iterator.hasNext()) {
				LocationIdentification tag = (LocationIdentification) iterator
						.next();
				System.out.println("\nAnnotation : " + tag.getCoveredText());
			}
		
	}
}