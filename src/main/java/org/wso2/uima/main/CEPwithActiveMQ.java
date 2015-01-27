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

package org.wso2.uima.main;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import java.io.IOException;
import java.util.Scanner;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by farazath on 1/22/15.
 */
public class CEPwithActiveMQ {
    private static Logger logger = getLogger(CEPwithActiveMQ.class);

    public static void main(String[] args) throws IOException, InvalidXMLException, InterruptedException {

        org.apache.log4j.PropertyConfigurator.configure("conf/log4j.properties");

        XMLInputSource in = new XMLInputSource("descriptors/collection-processing-engine/twitterCPE.xml");
        CpeDescription cpe_desc = UIMAFramework.getXMLParser().parseCpeDescription(in);

        CollectionProcessingEngine cpe = null;

        logger.info("Application Initiated");


        try {
            cpe = UIMAFramework.produceCollectionProcessingEngine(cpe_desc);
            cpe.addStatusCallbackListener(new StatusCallBackCPE());
            cpe.process();

            Scanner scanner = new Scanner(System.in);

            while(!scanner.nextLine().equals("exit")){

            }
            System.out.println(cpe.getPerformanceReport().toString());
            System.exit(0);
        } catch (ResourceInitializationException e) {
            logger.error("Error Initializing the CPE",e);
        }



        }

    }



class StatusCallBackCPE implements StatusCallbackListener {

    public StatusCallBackCPE(){
        org.apache.log4j.PropertyConfigurator.configure("conf/log4j.properties");

    }

    @Override
    public void aborted() {
        // TODO Auto-generated method stub
        getLogger(CEPwithActiveMQ.class).info("CPE aborted");
        throw new RuntimeException("CPE Aborted Abruptly");
    }

    @Override
    public void batchProcessComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void collectionProcessComplete() {
        // TODO Auto-generated method stub
        getLogger(CEPwithActiveMQ.class).info("CPE Processing Completed");

    }

    @Override
    public void initializationComplete() {
        // TODO Auto-generated method stub
        getLogger(CEPwithActiveMQ.class).info("CPE Initialization Completed");
    }

    @Override
    public void paused() {
        // TODO Auto-generated method stub
        System.out.println("CPE is paused");
    }

    @Override
    public void resumed() {
        // TODO Auto-generated method stub
        System.out.println("CPE is resumed");
    }

    @Override
    public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
        // TODO Auto-generated method stub

    }
}
