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

package org.wso2.uima.collectionProccesingEngine.reader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class TwitterActiveMQReader extends CollectionReader_ImplBase {

    private ArrayList<TextMessage> tweets;
    private static Logger logger = Logger.getLogger(TwitterActiveMQReader.class);


    private static final String PARAM_JMS_URL = "JMSUrL";
    private static final String PARAM_JMS_TOPIC_NAME = "topicName";

    private String jmsURL;
    private String topicName;
    private int currentIndex;
    private MessageConsumer consumer;
    int count = 0;

    @Override
    public void initialize() throws ResourceInitializationException {
        PropertyConfigurator.configure("conf/log4j.properties");

        jmsURL = (String) getConfigParameterValue(PARAM_JMS_URL);
        topicName = (String) getConfigParameterValue(PARAM_JMS_TOPIC_NAME);

        tweets = new ArrayList<>();
        currentIndex = 0;

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(jmsURL);
        logger.debug("Factory created Successful for " + jmsURL);

        Connection connection;
        consumer = null;
        try {
            connection = factory.createQueueConnection();

            String clientID = getClientID();
            connection.setClientID(clientID);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);

            logger.debug("Consumer Created Successfully");
            consumer = session.createDurableSubscriber(topic,clientID);

        } catch (JMSException e) {
             logger.error("Error Initializing the Subscriber for ActiveMQReader",e);
            throw new RuntimeException("Unable to initialize the CAS Reader");

        }

        logger.info("ActiveMQ Cas Reader Initialized Successfully");
    }


    @Override
    public void getNext(CAS cas) throws IOException, CollectionException {
        JCas jCas;
        try {
            jCas = cas.getJCas();
        } catch (CASException e) {
            logger.error("CAS passed in did not contain a JCas ", e);
            throw new CollectionException(e);
        }

        Message message;
        while (true) {
            try {
                message = consumer.receive();

                if (!(message == null) && message instanceof TextMessage) {
                    jCas.setDocumentText(((TextMessage) message).getText());
                    logger.info("Tweet Relieved to Reader: " + jCas.getDocumentText() + "  " + count++);
                    break;
                }


            } catch (JMSException e) {
                logger.error("Error when receiving message from the topic: " + topicName + " from url: " + jmsURL, e);
                System.exit(0);

            }
        }

    }


    @Override
    public boolean hasNext() throws IOException, CollectionException {
        return true;
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[]{new ProgressImpl(currentIndex, tweets.size(), Progress.ENTITIES)};
    }

    @Override
    public void close() throws IOException {

    }

    public String getClientID() {
        return this.hashCode() + "";
    }
}
