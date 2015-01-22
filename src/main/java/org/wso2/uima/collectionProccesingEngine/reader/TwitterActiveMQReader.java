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
        maxCount = (int)getConfigParameterValue(PARAM_MAX_DEQUEUE_VALUE);

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

            Message message = null;
            int count = 1;

            while (((message = consumer.receive(1000)) != null) && (message instanceof TextMessage) ){

                TextMessage txtMsg = (TextMessage)message;

                tweets.add(txtMsg);
                logger.debug("Message Dequeued #" + (count++) + ": " + txtMsg.getText());

               if(count > maxCount)
                    break;

            }
            logger.info(TwitterActiveMQReader.class.getSimpleName()+" initilialized Successfully");
            logger.info("Number of Messages Dequeued by Reader : " + tweets.size());

        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(0);
        }finally {
            if (connection != null) {
                try {
                    consumer.close();
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void getNext(CAS aCAS) throws IOException, CollectionException {
        JCas jcas;
        try {
            jcas = aCAS.getJCas();
        } catch (CASException e) {
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
            e.printStackTrace();
            logger.error("Error when retrieving text from JMS Text Message");
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
