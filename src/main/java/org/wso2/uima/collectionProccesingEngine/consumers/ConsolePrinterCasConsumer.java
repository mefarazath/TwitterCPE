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

import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.uima.collectionProccesingEngine.consumers.util.CasConsumerUtil;

public class ConsolePrinterCasConsumer extends CasConsumer_ImplBase{

	private static Logger logger = Logger.getLogger(ConsolePrinterCasConsumer.class);

	@Override
	public void processCas(CAS cas) throws ResourceProcessException {

		String tweetText = CasConsumerUtil.getTweetText(cas);
		String locationString = CasConsumerUtil.getLocationString(cas);
		String trafficLevel = CasConsumerUtil.getTrafficLevel(cas);

		if(locationString.isEmpty()){
			return;
		}

		System.out.println("Annotated Location : " + locationString);
		System.out.println("Annotated Traffic : " + trafficLevel);
		
	}
}