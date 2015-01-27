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

/**
 * Created by vidura on 1/19/15.
 */

import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.uima.collectionProccesingEngine.consumers.util.KeyStoreUtil;
import org.wso2.uima.collectionProccesingEngine.consumers.util.TweetScanner;

import javax.xml.soap.*;
import java.sql.Timestamp;
import java.util.Date;

public class SoapCasConsumer extends CasConsumer_ImplBase {

    private static Logger logger = Logger.getLogger(SoapCasConsumer.class);
    private String username;
    private String password;
    private String soapEndPoint;

    @Override
    public void processCas(CAS cas) throws ResourceProcessException {

        //properties
        String tweetText = TweetScanner.getTweetText(cas);
        String locationString = TweetScanner.getLocationString(cas);
        String trafficLevel = TweetScanner.getTrafficLevel(cas);

        if(locationString.isEmpty()){
            return;
        }

        Logger.getLogger(SoapCasConsumer.class).info("Annotated Location :  " + locationString.trim());
        Logger.getLogger(SoapCasConsumer.class).info("Annotated Traffic Level :  " + trafficLevel);

        //creating soap message
        Date date = new Date();
        String time = new Timestamp(date.getTime()).toString();
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPHeader header = envelope.getHeader();

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction",soapEndPoint);

            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement operation = soapBody.addChildElement("trafficUpdate");
            SOAPElement eventsElement = operation.addChildElement("events");
            SOAPElement eventElement = eventsElement.addChildElement("event");
            SOAPElement metaDataElement = eventElement.addChildElement("metaData");
            SOAPElement timeStampElement = metaDataElement.addChildElement("Timestamp");
            timeStampElement.addTextNode(time);
            SOAPElement payloadDataElement = eventElement.addChildElement("payloadData");
            SOAPElement locationElement = payloadDataElement.addChildElement("Traffic_Location");
            locationElement.addTextNode(locationString);
            SOAPElement trafficLevelElement = payloadDataElement.addChildElement("Traffic_Level");
            trafficLevelElement.addTextNode(trafficLevel);
            SOAPElement tweetTextElement = payloadDataElement.addChildElement("Twitter_Text");
            tweetTextElement.addTextNode(tweetText);

           // System.out.println(soapMessage.getSOAPBody().toString());
            this.publish(soapMessage);

        } catch (Exception e) {
            logger.error("Error occurs when creating the SOAP message",e);
        }
    }


    @Override
    public void initialize() throws ResourceInitializationException {

        KeyStoreUtil.setTrustStoreParams();

        username = (String)getConfigParameterValue("username");
        password = (String)getConfigParameterValue("password");
        soapEndPoint = (String)getConfigParameterValue("SOAPEndPoint");

    }


    public void publish(SOAPMessage soapMessage) {
        try {

            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(soapMessage, soapEndPoint);

            // close soap connection
            soapConnection.close();

        } catch (Exception e) {
            logger.error("Error occurred while sending SOAP Request to Server",e);
        }
    }
}