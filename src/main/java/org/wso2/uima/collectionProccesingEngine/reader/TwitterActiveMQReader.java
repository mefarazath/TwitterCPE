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
import org.wso2.uima.types.TimeStamp;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by farazath on 1/22/15.
 */
public class TwitterActiveMQReader extends CollectionReader_ImplBase {

    private ArrayList<TextMessage> tweets;
    private static Logger logger = Logger.getLogger(TwitterActiveMQReader.class);


    private static final String PARAM_JMS_URL = "JMSUrL";
    private static final String PARAM_JMS_QUEUE_NAME = "queueName";
    private static final String PARAM_MAX_DEQUEUE_VALUE = "maxMessagesToDequeue";

    private String JMSUrl;
    private String queueName;
    private int maxCount;
    private int currentIndex;

    @Override
    public void initialize() throws ResourceInitializationException{
        PropertyConfigurator.configure("conf/log4j.properties");

        JMSUrl = (String)getConfigParameterValue(PARAM_JMS_URL);
        queueName = (String)getConfigParameterValue(PARAM_JMS_QUEUE_NAME);
        maxCount = (Integer)getConfigParameterValue(PARAM_MAX_DEQUEUE_VALUE);

        tweets = new ArrayList<>();
        currentIndex = 0;

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(JMSUrl);
        logger.debug("Factory created Successful for "+JMSUrl);

        Connection connection = null;
        MessageConsumer consumer = null;
        try {
            connection = factory.createQueueConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);

            logger.debug("Consumer Created Successfully");
            consumer = session.createConsumer(queue);

            Message message;
            int count = 1;

            while (((message = consumer.receive(1000)) != null) && (message instanceof TextMessage) ){

                TextMessage txtMsg = (TextMessage)message;

                tweets.add(txtMsg);
                logger.debug("Message Dequeued #" + (count++) + ": " + txtMsg.getText()+" from "+JMSUrl);

               if(count > maxCount)
                    break;

            }
            logger.info(TwitterActiveMQReader.class.getSimpleName()+" initilialized Successfully");
            logger.info("Number of Messages Dequeued by Reader : " + tweets.size()+" from "+JMSUrl);

        } catch (JMSException e) {
            //TODO enter the correct log
            //logger.error();

        }finally {

                try {
                    if (connection != null) {
                        connection.close();
                    }
                    if(consumer != null){
                        consumer.close();
                    }
                } catch (JMSException e) {
                    //TODO enter the correct log
                    //logger.error();
                }
            }
        }



    @Override
    public void getNext(CAS aCAS) throws IOException, CollectionException {
        JCas jcas;
        try {
            jcas = aCAS.getJCas();
        } catch (CASException e) {
            logger.error("CAS passed in did not contain a JCas ", e);
            throw new CollectionException(e);
        }

        // get the tweet text of the currentIndex
        TextMessage textMsg = null;
        try {
            textMsg = tweets.get(currentIndex++);
            // set the documentation text
            jcas.setDocumentText(textMsg.getText());

            TimeStamp timeStamp = new TimeStamp(jcas);
            timeStamp.setTimeStamp(textMsg.getJMSTimestamp());

        } catch (JMSException e) {
            logger.error("Error when retrieving text from JMS Text Message ",e);
        }

        logger.debug("CAS DocText: "+jcas.getDocumentText());
    }

    @Override
    public boolean hasNext() throws IOException, CollectionException {
        return currentIndex < tweets.size();
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl( currentIndex, tweets.size(), Progress.ENTITIES) };
    }

    @Override
    public void close() throws IOException {

    }
}
