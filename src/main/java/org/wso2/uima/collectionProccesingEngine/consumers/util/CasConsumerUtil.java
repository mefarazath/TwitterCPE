/*
 *
 * Copyright (c) 2005-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.uima.collectionProccesingEngine.consumers.util;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.wso2.uima.types.LocationIdentification;
import org.wso2.uima.types.TrafficLevelIdentifier;

import java.util.Iterator;
/**
 * Scan the CAS object given and return the locations, trafficLevel and tweetText indicated within the cas.
 */

public class CasConsumerUtil {

    private CasConsumerUtil(){
        //no instances.
    }

    /**
     * @param cas gives the document to be scanned.
     * @return the list of locations indicated inside cas.
     */
    public static String getLocationString(CAS cas) {

        // run the sample document through the pipeline
        JCas output = null;
        try {
            output = cas.getJCas();
        } catch (CASException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();

        FSIndex locationIndex = output.getAnnotationIndex(LocationIdentification.type);
        for (Iterator<LocationIdentification> it = locationIndex.iterator(); it.hasNext(); ) {
            LocationIdentification annotation = it.next();
            if (!builder.toString().contains(annotation.getCoveredText())) {
                builder.append(annotation.getCoveredText() + " ");
            }
        }

        return builder.toString().trim();

    }

    /**
     * @param cas gives the document to be scanned.
     * @return the tweet text included inside cas.
     */
    public static String getTweetText(CAS cas) {

        // run the sample document through the pipeline
        JCas output = null;
        try {
            output = cas.getJCas();
        } catch (CASException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        String tweetText = "\n" + output.getDocumentText();

        return tweetText;
    }

    /**
     * @param cas gives the document to be scanned.
     * @return the trafficLevel indicated inside cas.
     */
    public static String getTrafficLevel(CAS cas) {

        // run the sample document through the pipeline
        JCas output = null;
        try {
            output = cas.getJCas();
        } catch (CASException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        String trafficLevel = "";
        FSIndex trafficLevelIndex = output.getAnnotationIndex(TrafficLevelIdentifier.type);

        for (Iterator<TrafficLevelIdentifier> it = trafficLevelIndex.iterator(); it.hasNext(); ) {
            TrafficLevelIdentifier level = it.next();
            trafficLevel = level.getTrafficLevel();
        }
        return trafficLevel;
    }
}
